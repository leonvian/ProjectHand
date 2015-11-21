package com.lvc.syndichand;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.Condominium;
import com.lvc.syndichand.model.User;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by administrator on 10/24/15.
 */
public class CondominiumEntry extends SyndicHandActivity {

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextCEP;
    private EditText editTextObservation;
    private CheckBox checkBoxColdwater;
    private CheckBox checkBoxHotwater;
    private CheckBox checkBoxGas;
    private Condominium condominium = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condominium_entry);

        prepareActionBarToBack(getString(R.string.condominium));

        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextAddress = (EditText) findViewById(R.id.edit_text_address);
        editTextCEP = (EditText) findViewById(R.id.edit_text_cep);
        editTextObservation = (EditText) findViewById(R.id.edit_text_observation);

        checkBoxColdwater = (CheckBox) findViewById(R.id.checkbox_coldwater);
        checkBoxHotwater = (CheckBox) findViewById(R.id.checkbox_hotwater);
        checkBoxGas = (CheckBox) findViewById(R.id.checkbox_gas);

        loadOrCreateNewInstanceOfCondominium();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isObrigatoryFieldsOk(editTextName)) {
                    return;
                }

                loadDataFromUIFields();
                condominium.save();
                saveOnline(condominium);
            }
        });
    }

    private void loadDataFromUIFields() {
        String name = editTextName.getText().toString();
        String address = editTextAddress.getText().toString();
        String cep = editTextCEP.getText().toString();
        String observation = editTextObservation.getText().toString();

        boolean hotWater = checkBoxHotwater.isChecked();
        boolean coldWater = checkBoxColdwater.isChecked();
        boolean gas = checkBoxGas.isChecked();

        condominium.setName(name);
        condominium.setAddress(address);
        condominium.setCep(cep);
        condominium.setObservation(observation);
        condominium.setHotWater(hotWater);
        condominium.setColdWater(coldWater);
        condominium.setGas(gas);
    }

    private void loadOrCreateNewInstanceOfCondominium() {
        condominium = retrieveCondominium();

        if (condominium == null) {
            condominium = new Condominium();
        } else {
            loadCondominiumData();
        }
    }

    private Condominium retrieveCondominium() {
        Condominium condominium = CondominiumDAO.retrieveCondominium();

        return condominium;
    }

    private void loadCondominiumData() {
        editTextName.setText(condominium.getName());
        editTextAddress.setText(condominium.getAddress());
        editTextObservation.setText(condominium.getObservation());
        editTextCEP.setText(condominium.getCep());
        checkBoxColdwater.setChecked(condominium.isColdWater());
        checkBoxHotwater.setChecked(condominium.isHotWater());
        checkBoxGas.setChecked(condominium.isGas());
    }

    private void saveOnline(final Condominium condominium) {
        showProgressDialog();
        WebFacade.saveOrUpdateData(condominium, new WebFacade.WebCallback() {

            @Override
            public void onWorkDone(String webId, Exception e) {
                dismissProgressDialog();
                if (e == null) {
                    condominium.setParseIdentifier(webId);
                    condominium.save();
                    saveUserWithcCondominiumID(webId);
                } else {
                    condominium.delete();
                    Toast.makeText(CondominiumEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void saveUserWithcCondominiumID(String condominiumID) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put(User.CONDOMINIUM_ID_KEY, condominiumID);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(CondominiumEntry.this, R.string.save_sucess, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    condominium.delete();
                    Toast.makeText(CondominiumEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}