package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.Unity;
import com.lvc.syndichand.webservice.WebFacade;

/**
 * Created by administrator on 9/22/15.
 */
public class OwnerEntry extends SyndicHandActivity {

    private EditText editTextApartamentNumber;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextDDD;
    private EditText editTextPhone;

    private CheckBox checkBoxRent;
    private CheckBox checkBoxPet;
    private CheckBox checkBoxSyndic;
    private CheckBox checkBoxAudittCommittee;
    private Unity unity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_owner);

        prepareActionBarToBack(getString(R.string.owner));

        editTextApartamentNumber = (EditText) findViewById(R.id.edit_text_apartament_number);
        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextDDD = (EditText) findViewById(R.id.edit_text_ddd);
        editTextPhone = (EditText) findViewById(R.id.edit_text_phone);

        checkBoxAudittCommittee = (CheckBox) findViewById(R.id.checkbox_audit);
        checkBoxSyndic = (CheckBox) findViewById(R.id.checkbox_syndic);
        checkBoxRent = (CheckBox) findViewById(R.id.checkbox_rent);
        checkBoxPet = (CheckBox) findViewById(R.id.checkbox_pet);

        loadUnityOrCreateNew();
        filledUIElementsByModel();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isObrigatoryFieldsOk()) {
                    Unity unity = loadOwnerByFields();
                    unity.save();
                    saveOnline(unity);
                }
            }
        });
    }

    private void filledUIElementsByModel() {
        editTextApartamentNumber.setText(unity.getApartamentNumber());
        editTextName.setText(unity.getResponsableName());
        editTextEmail.setText(unity.getEmail());
        editTextDDD.setText(unity.getDdd());
        editTextPhone.setText(unity.getPhone());

        checkBoxAudittCommittee.setChecked(unity.isAuditCommittee());
        checkBoxSyndic.setChecked(unity.isSyndic());
        checkBoxRent.setChecked(unity.isRent());
        checkBoxPet.setChecked(unity.isPet());

    }

    private void saveOnline(final Unity unity) {
        WebFacade.saveOrUpdateData(unity, new WebFacade.WebCallback() {

            @Override
            public void onWorkDone(String webId, Exception e) {
                if (e == null) {
                    unity.setParseIdentifier(webId);
                    unity.save();
                    Toast.makeText(OwnerEntry.this, R.string.save_sucess, Toast.LENGTH_LONG).show();
                    finishWithResult(unity);
                } else {
                    e.printStackTrace();
                    unity.delete();
                    Toast.makeText(OwnerEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void loadUnityOrCreateNew() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            unity = (Unity)extras.getSerializable(Unity.class.getSimpleName());
        } else {
            unity = new Unity();
            String condominiumID = CondominiumDAO.retrieveCondominiumIdentifier();
            unity.setCondominiumIdentifier(condominiumID);
        }
    }

    private void finishWithResult(Unity owner) {
        Bundle extras = new Bundle();
        extras.putSerializable(Unity.class.getSimpleName(), owner);

        Intent data = new Intent();
        data.putExtras(extras);
        setResult(RESULT_OK,data);

        finish();
    }

    private Unity loadOwnerByFields() {
        String name = editTextName.getText().toString();
        String apartmentNumber = editTextApartamentNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String ddd = editTextDDD.getText().toString();
        boolean syndic = checkBoxSyndic.isChecked();
        boolean auditt = checkBoxAudittCommittee.isChecked();
        boolean rent = checkBoxRent.isChecked();
        boolean pet = checkBoxPet.isChecked();

        unity.setResponsableName(name);
        unity.setApartamentNumber(apartmentNumber);
        unity.setEmail(email);
        unity.setPhone(phone);
        unity.setDdd(ddd);
        unity.setSyndic(syndic);
        unity.setAuditCommittee(auditt);
        unity.setRent(rent);
        unity.setPet(pet);


        return unity;
    }

    private boolean isObrigatoryFieldsOk() {
        boolean result = isObrigatoryFieldsOk(editTextApartamentNumber, editTextDDD, editTextEmail, editTextName, editTextPhone);

        return result;
    }
}
