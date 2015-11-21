package com.lvc.syndichand;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lvc.syndichand.model.Register;
import com.lvc.syndichand.model.Unity;

import java.util.Date;

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

        unity = (Unity)getIntent().getExtras().getSerializable(Unity.class.getSimpleName());

        editTextCurrentColdWater = (EditText) findViewById(R.id.edit_text_cold_water);
        editTextCurrentHotWater = (EditText) findViewById(R.id.edit_text_hotwater);
        editTextCurrentGasRegister = (EditText) findViewById(R.id.edit_text_quantity_gas);

        editTextCurrentGasRegisterOld =  (EditText) findViewById(R.id.edit_text_quantity_gas_old);
        editTextCurrentColdWaterOld  = (EditText) findViewById(R.id.edit_text_cold_water_old);
        editTextCurrentHotWaterOld  = (EditText) findViewById(R.id.edit_text_hotwater_old);

        TextView textViewName = (TextView) findViewById(R.id.text_view_name);
        textViewName.setText(unity.getResponsableName());

        TextView textViewApartmentNumber = (TextView) findViewById(R.id.text_view_apartament_number);
        textViewApartmentNumber.setText(unity.getApartamentNumber());

        prepareActionBarToBack(getString(R.string.read_title));

        findViewById(R.id.button_save_cold_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.button_save_gas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.button_save_hot_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void saveRegisters(Long ownerId) {
        double coldWater = getDoubleValueInEditText(editTextCurrentColdWater);
        double gas = getDoubleValueInEditText(editTextCurrentGasRegister);
        double hotWater = getDoubleValueInEditText(editTextCurrentHotWater);

        Register registerGas = new Register(Register.REGISTER_GAS, gas, new Date(), ownerId);
        registerGas.save();

        Register registerColdWater = new Register(Register.REGISTER_COLD_WATER, coldWater, new Date(), ownerId);
        registerColdWater.save();

        Register registerHotWater = new Register(Register.REGISTER_HOT_WATER, hotWater, new Date(), ownerId);
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

}
