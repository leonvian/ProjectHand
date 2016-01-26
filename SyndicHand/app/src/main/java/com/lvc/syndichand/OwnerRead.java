package com.lvc.syndichand;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.Register;
import com.lvc.syndichand.model.Unity;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OwnerRead extends SyndicHandActivity {

    private EditText editTextCurrentGasRegister;
    private EditText editTextCurrentColdWater;
    private EditText editTextCurrentHotWater;

    private EditText editTextCurrentGasRegisterOld;
    private EditText editTextCurrentColdWaterOld;
    private EditText editTextCurrentHotWaterOld;

    private Unity unity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_screen);

        unity = (Unity) getIntent().getExtras().getSerializable(Unity.class.getSimpleName());

        editTextCurrentColdWater = (EditText) findViewById(R.id.edit_text_cold_water);
        editTextCurrentHotWater = (EditText) findViewById(R.id.edit_text_hotwater);
        editTextCurrentGasRegister = (EditText) findViewById(R.id.edit_text_quantity_gas);

        editTextCurrentGasRegisterOld = (EditText) findViewById(R.id.edit_text_quantity_gas_old);
        editTextCurrentColdWaterOld = (EditText) findViewById(R.id.edit_text_cold_water_old);
        editTextCurrentHotWaterOld = (EditText) findViewById(R.id.edit_text_hotwater_old);

        TextView textViewName = (TextView) findViewById(R.id.text_view_name);
        textViewName.setText(unity.getResponsableName());

        TextView textViewApartmentNumber = (TextView) findViewById(R.id.text_view_apartament_number);
        textViewApartmentNumber.setText(unity.getApartamentNumber());

        prepareActionBarToBack(getString(R.string.read_title));

        findViewById(R.id.button_save_cold_water).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveRegisterIfNecessary(editTextCurrentColdWaterOld, Register.REGISTER_COLD_WATER, true);
                saveRegisterIfNecessary(editTextCurrentColdWater, Register.REGISTER_COLD_WATER, false);
            }
        });

        findViewById(R.id.button_save_gas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRegisterIfNecessary(editTextCurrentGasRegisterOld, Register.REGISTER_GAS, true);
                saveRegisterIfNecessary(editTextCurrentGasRegister, Register.REGISTER_GAS, false);
            }
        });

        findViewById(R.id.button_save_hot_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRegisterIfNecessary(editTextCurrentHotWaterOld, Register.REGISTER_HOT_WATER, true);
                saveRegisterIfNecessary(editTextCurrentHotWater, Register.REGISTER_HOT_WATER, false);
            }
        });

        loadOldRegisters();
        atualizeLabel();
    }

    private void saveRegisterIfNecessary(EditText editText, int registerCode, boolean isOld) {
        if (editText.isEnabled() && editText.getText().toString().length() > 0) {
            double consume = getDoubleValueInEditText(editText);
            if(isOld) {
                saveOldRegister(consume, registerCode);
            } else {
                saveRegister(consume, registerCode);
            }
        }
    }

    private void atualizeLabel() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        setHint(editTextCurrentColdWater,getStringMonth(currentMonth,year));
        setHint(editTextCurrentHotWater,getStringMonth(currentMonth,year));
        setHint(editTextCurrentGasRegister, getStringMonth(currentMonth, year));

        calendar.add(Calendar.MONTH, -1);
        currentMonth = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        setHint(editTextCurrentGasRegisterOld,getStringMonth(currentMonth,year));
        setHint(editTextCurrentColdWaterOld,getStringMonth(currentMonth,year));
        setHint(editTextCurrentHotWaterOld, getStringMonth(currentMonth, year));
    }

    private void setHint(EditText editText, String text) {
        TextInputLayout textInputLayout = (TextInputLayout)editText.getParent();
        textInputLayout.setHint(text);
    }

    private void loadOldRegisters() {
        showProgressDialog();
        String unityId = unity.getParseUniqueID();
        WebFacade.loadRegister(unityId, new WebFacade.QueryWebCallback<Register>() {
            @Override
            public void onQueryResult(List<Register> data, Exception e) {
                if (e == null) {
                    treatOldRegisters(data);
                } else {
                    showMessageToast(getString(R.string.fail_to_retrieve_data));
                }

                dismissProgressDialog();
            }
        });
    }

    private void treatOldRegisters(List<Register> data) {

        for (Register register : data) {
            int type = register.getType();
            switch (type) {
                case Register.REGISTER_COLD_WATER:
                    loadEditTextColdWater(register);
                    break;
                case Register.REGISTER_HOT_WATER:
                   loadEditTextHotWater(register);
                    break;
                case Register.REGISTER_GAS:
                    loadEditTextGas(register);
                    break;
            }
        }
    }

    private void loadEditTextHotWater(Register register) {
        if (register.isLastMonth()) {
            loadEditText(editTextCurrentHotWaterOld, register);
        } else {
            loadEditText(editTextCurrentHotWater, register);
        }
    }

    private void loadEditTextColdWater(Register register) {
        if (register.isLastMonth()) {
            loadEditText(editTextCurrentColdWaterOld, register);
        } else {
            loadEditText(editTextCurrentColdWater, register);
        }
    }

    private void loadEditTextGas(Register register) {
        if (register.isLastMonth()) {
            loadEditText(editTextCurrentGasRegisterOld, register);
        } else {
            loadEditText(editTextCurrentGasRegister, register);
        }
    }

    private void loadEditText(EditText editText, Register register) {
        loadEditText(editText, register, register.isNotProcessing());
    }

    private void loadEditText(EditText editText, Register register, boolean enable) {
        String consume = String.valueOf(register.getCurrentConsume());
        editText.setText(consume);
        editText.setEnabled(enable);
    }

    private void saveRegister(double consume, int type) {
        saveRegister(consume, type, new Date());
    }

    private void saveOldRegister(double consume, int type) {
        Date date = generateLastMonthDate();
        saveRegister(consume, type, date);
    }

    private Date generateLastMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        return date;
    }

    private void saveRegister(double consume, int type, Date date) {
        showProgressDialog();
        String idCondominium = CondominiumDAO.retrieveCondominiumIdentifier();
        String user = ParseUser.getCurrentUser().getObjectId();
        String unityId = unity.getParseUniqueID();

        final Register register = new Register(type, consume, date, unityId, idCondominium, user);
        register.save();

        WebFacade.saveOrUpdateRegister(register, new WebFacade.WebCallback() {
            @Override
            public void onWorkDone(String webID, Exception e) {
                if (e == null) {
                    register.setParseIdentifier(webID);
                    register.save();
                    showMessageToast(getString(R.string.register_save_sucess));
                } else {
                    showMessageToast(getString(R.string.fail_save_online));
                    register.delete();
                }
                dismissProgressDialog();
            }
        });
    }

    //public Register(int type, double currentConsume, Date date,String idUnity,String idCondominium, String user) {
    private void saveRegisters(String unityId) {
        double coldWater = getDoubleValueInEditText(editTextCurrentColdWater);
        double gas = getDoubleValueInEditText(editTextCurrentGasRegister);
        double hotWater = getDoubleValueInEditText(editTextCurrentHotWater);

        String idCondominium = CondominiumDAO.retrieveCondominiumIdentifier();
        String user = ParseUser.getCurrentUser().getObjectId();

        Register registerGas = new Register(Register.REGISTER_GAS, gas, new Date(), unityId, idCondominium, user);
        registerGas.save();

        Register registerColdWater = new Register(Register.REGISTER_COLD_WATER, coldWater, new Date(), unityId, idCondominium, user);
        registerColdWater.save();

        Register registerHotWater = new Register(Register.REGISTER_HOT_WATER, hotWater, new Date(), unityId, idCondominium, user);
        registerHotWater.save();
    }

    private double getDoubleValueInEditText(EditText editText) {
        String value = editText.getText().toString();
        double valueInDouble = Double.valueOf(value);
        return valueInDouble;
    }

    /*
    private boolean checkIfDoubleValueIsProperlyFilled(EditText... fields) {
        boolean result = false;
        for (EditText editText : fields) {
            String value = editText.getText().toString();
            try {
                Double.valueOf(value);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                editText.setError(getString(R.string.unexpected_value));
            }
        }

        return result;
    }

    private void loadOldConsume() {

    }
    */

    private String getStringMonth(int month, int year) {
        String monthStr = getMonth(month);
        String result = getString(R.string.read).concat(" ").concat(monthStr).concat(" ").concat(String.valueOf(year));
        return result;
    }


    private String getMonth(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return getString(R.string.jan);
            case Calendar.FEBRUARY:
                return getString(R.string.feb);
            case Calendar.MARCH:
                return getString(R.string.marc);
            case Calendar.MAY:
                return getString(R.string.may);
            case Calendar.JUNE:
                return getString(R.string.jun);
            case Calendar.JULY:
                return getString(R.string.july);
            case Calendar.AUGUST:
                return getString(R.string.august);
            case Calendar.SEPTEMBER:
                return getString(R.string.september);
            case Calendar.OCTOBER:
                return getString(R.string.october);
            case Calendar.NOVEMBER:
                return getString(R.string.november);
            case Calendar.APRIL:
                return getString(R.string.april);
            case Calendar.DECEMBER:
                return getString(R.string.december);
        }
        return "";
    }

}
