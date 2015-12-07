package com.lvc.syndichand;

import android.os.Bundle;
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

    String idGas = null;
    String idHotWater = null;
    String idColdWater = null;

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
                if (editTextCurrentColdWaterOld.isEnabled()) {
                    double oldRegister = getDoubleValueInEditText(editTextCurrentColdWaterOld);
                    saveOldRegister(oldRegister, Register.REGISTER_COLD_WATER);
                }

                double coldWater = getDoubleValueInEditText(editTextCurrentColdWater);
                saveRegister(idColdWater,coldWater, Register.REGISTER_COLD_WATER);
            }
        });

        findViewById(R.id.button_save_gas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCurrentGasRegisterOld.isEnabled()) {
                    double oldRegister = getDoubleValueInEditText(editTextCurrentGasRegisterOld);
                    saveOldRegister(oldRegister, Register.REGISTER_GAS);
                }


                double gas = getDoubleValueInEditText(editTextCurrentGasRegister);
                saveRegister(idGas,gas, Register.REGISTER_GAS);
            }
        });

        findViewById(R.id.button_save_hot_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCurrentHotWaterOld.isEnabled()) {
                    double oldRegister = getDoubleValueInEditText(editTextCurrentHotWaterOld);
                    saveOldRegister(oldRegister, Register.REGISTER_HOT_WATER);
                }

                double hotWater = getDoubleValueInEditText(editTextCurrentHotWater);
                saveRegister(idHotWater, hotWater, Register.REGISTER_HOT_WATER);
            }
        });

        loadOldRegisters();
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int oldMonth = calendar.get(Calendar.MONTH);

        for (Register register : data) {

            boolean isOldMonth = false;
            if (register.getMonth() == oldMonth) {
                isOldMonth = true;
            }

            int type = register.getType();
            switch (type) {
                case Register.REGISTER_COLD_WATER:
                    if (isOldMonth) {
                        loadEditTextOld(editTextCurrentColdWaterOld, register);
                    } else {
                        loadEditTextNew(editTextCurrentColdWater, register);
                        idColdWater = register.getParseUniqueID();
                    }
                    break;
                case Register.REGISTER_HOT_WATER:
                    if (isOldMonth) {
                        loadEditTextOld(editTextCurrentHotWaterOld, register);
                    } else {
                        loadEditTextNew(editTextCurrentHotWater, register);
                        idHotWater = register.getParseUniqueID();
                    }
                    break;
                case Register.REGISTER_GAS:
                    if (isOldMonth) {
                        loadEditTextOld(editTextCurrentGasRegisterOld, register);
                    } else {
                        loadEditTextNew(editTextCurrentGasRegister, register);
                        idGas = register.getParseUniqueID();
                    }

                    break;
            }
        }
    }

    private void loadEditTextOld(EditText editText, Register register) {
        loadEditText(editText, register, false);
    }

    private void loadEditTextNew(EditText editText, Register register) {
        loadEditText(editText, register, true);
    }

    private void loadEditText(EditText editText, Register register, boolean enable) {
        String consume = String.valueOf(register.getCurrentConsume());
        editText.setText(consume);
        editText.setEnabled(enable);
    }

    private void saveRegister(String parserId, double consume, int type) {
        saveRegister(parserId,consume, type, new Date());
    }

    private void saveOldRegister(double consume, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        saveRegister(null,consume, type, calendar.getTime());
    }

    private void saveRegister(String parserId, double consume, int type, Date date) {
        showProgressDialog();
        String idCondominium = CondominiumDAO.retrieveCondominiumIdentifier();
        String user = ParseUser.getCurrentUser().getObjectId();
        String unityId = unity.getParseUniqueID();

        final Register register = new Register(type, consume, date, unityId, idCondominium, user);
        register.setParseIdentifier(parserId);
        register.save();

        WebFacade.saveOrUpdateData(register, new WebFacade.WebCallback() {
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

}
