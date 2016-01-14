package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.Vehicle;
import com.lvc.syndichand.webservice.WebFacade;

public class VehicleEntry extends SyndicHandActivity {

    private EditText editTextModel;
    private EditText editTextColor;
    private EditText editTextCarBoard;
    private EditText editTextUnity;

    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_entry);
        prepareActionBarToBack(getString(R.string.vehicle));

        editTextModel = (EditText) findViewById(R.id.edit_text_model);
        editTextColor = (EditText) findViewById(R.id.edit_text_color);
        editTextCarBoard = (EditText) findViewById(R.id.edit_text_car_board);
        editTextUnity = (EditText) findViewById(R.id.edit_text_unity);

        loadVehicleOrCreateNew();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isObrigatoryFieldsOk(editTextModel, editTextColor, editTextCarBoard, editTextUnity)) {
                    return;
                }

                showProgressDialog();

                String model = editTextModel.getText().toString();
                String color = editTextColor.getText().toString();
                String carBoard = editTextCarBoard.getText().toString();
                String unity = editTextUnity.getText().toString();

                vehicle.loadData(model, color, carBoard, unity);
                vehicle.save();
                saveOnline(vehicle);
            }
        });
    }

    private void loadVehicleOrCreateNew() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            vehicle = (Vehicle) extras.getSerializable(Vehicle.class.getSimpleName());
            loadFieldsByData(vehicle);
        } else {
            vehicle = new Vehicle();
            String condominiumId = CondominiumDAO.retrieveCondominiumIdentifier();
            vehicle.setCondominiumParseIdentifier(condominiumId);
        }
    }

    private void loadFieldsByData(Vehicle vehicle) {

        editTextModel.setText(vehicle.getModel());
        editTextColor.setText(vehicle.getColor());
        editTextCarBoard.setText(vehicle.getCarBoard());
        editTextUnity.setText(vehicle.getUnity());
    }

    private void saveOnline(final Vehicle vehicle) {

        WebFacade.saveOrUpdateData(vehicle, new WebFacade.WebCallback() {

            @Override
            public void onWorkDone(String webId, Exception e) {

                if (e == null) {

                    vehicle.setParseIdentifier(webId);
                    vehicle.save();

                    dismissProgressDialog();
                    Toast.makeText(VehicleEntry.this, R.string.vehicle_save_sucesss, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putSerializable(Vehicle.class.getSimpleName(), vehicle);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    vehicle.delete();
                    Toast.makeText(VehicleEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                    dismissProgressDialog();
                }
            }
        });
    }
}