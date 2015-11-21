package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.CommonArea;
import com.lvc.syndichand.webservice.WebFacade;

/**
 * Created by administrator on 10/17/15.
 */
public class CommonAreaEntry extends SyndicHandActivity {

    private EditText editTextName;
    private EditText editTextObservation;
    private EditText editTextMaxPeople;
    private EditText editTextWorkTimeBegin;
    private EditText editTextWorkTimeEnd;
    private EditText editTextReservationCost;
    private CheckBox checkBoxNecessarySindicValidation;
    private CommonArea commonArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_area_entry);
        prepareActionBarToBack(getString(R.string.common_area));

        editTextObservation = (EditText) findViewById(R.id.edit_text_observation);
        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextMaxPeople = (EditText) findViewById(R.id.edit_text_max_people);
        editTextWorkTimeBegin = (EditText) findViewById(R.id.edit_text_work_time_begin);
        editTextWorkTimeEnd = (EditText) findViewById(R.id.edit_text_work_time_end);
        editTextReservationCost = (EditText) findViewById(R.id.edit_text_reservation_cost);
        checkBoxNecessarySindicValidation = (CheckBox) findViewById(R.id.checkbox_allow_reservations);

        loadDataFromBundleOrCreateNewOne();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isObrigatoryFieldsOk(editTextName, editTextMaxPeople, editTextWorkTimeBegin, editTextWorkTimeEnd)) {
                    return;
                }

                showProgressDialog();
                String name = editTextName.getText().toString();
                int maxPeople = getIntValueOnEditText(editTextMaxPeople);
                String workTimeBegin = editTextWorkTimeBegin.getText().toString();
                String workTimeEnd = editTextWorkTimeEnd.getText().toString();
                String reservationCost = editTextReservationCost.getText().toString();
                String observation = editTextObservation.getText().toString();

                boolean allowReservations = checkBoxNecessarySindicValidation.isChecked();

                commonArea.loadData(name, maxPeople, workTimeBegin, workTimeEnd, reservationCost, observation, allowReservations);
                commonArea.save();
                saveOnline(commonArea);
            }
        });
    }

    private void loadDataFromBundleOrCreateNewOne() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            commonArea = new CommonArea();
            String condominiumId = CondominiumDAO.retrieveCondominiumIdentifier();
            commonArea.setCondominiumParseIdentifier(condominiumId);
        } else {
            commonArea = (CommonArea)extras.getSerializable(CommonArea.class.getSimpleName());
            loadFieldsByData(commonArea);
        }
    }

    private void loadFieldsByData(CommonArea commonArea) {
        editTextName.setText(commonArea.getName());
        editTextMaxPeople.setText(String.valueOf(commonArea.getMaxPeople()));
        editTextWorkTimeBegin.setText(commonArea.getWorkTimeBegin());
        editTextWorkTimeEnd.setText(commonArea.getWorkTimeEnd());
        editTextReservationCost.setText(commonArea.getScheduleCost());
        editTextObservation.setText(commonArea.getObservation());

        checkBoxNecessarySindicValidation.setChecked(commonArea.isNeedSyndicValidation());
    }

    private void saveOnline(final CommonArea commonArea) {
        WebFacade.saveOrUpdateData(commonArea, new WebFacade.WebCallback() {

            @Override
            public void onWorkDone(String webId, Exception e) {
                if (e == null) {
                    commonArea.setParseIdentifier(webId);
                    commonArea.save();

                    dismissProgressDialog();
                    Toast.makeText(CommonAreaEntry.this, R.string.common_area_save_sucesss, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putSerializable(CommonArea.class.getSimpleName(), commonArea);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    commonArea.delete();
                    Toast.makeText(CommonAreaEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                    dismissProgressDialog();
                }
            }

        });
    }

    private int getIntValueOnEditText(EditText editText) {
        String valueStr = editText.getText().toString();
        int value = Integer.valueOf(valueStr);

        return value;
    }

}
