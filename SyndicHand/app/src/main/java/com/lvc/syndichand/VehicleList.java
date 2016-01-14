package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lvc.syndichand.database.VehicleDAO;
import com.lvc.syndichand.model.Vehicle;
import com.lvc.syndichand.webservice.WebFacade;

import java.util.ArrayList;
import java.util.List;

public class VehicleList extends SyndicHandList implements OnDataSelected {

    private static final int RELOAD_LIST_CODE = 20;
    private List<Vehicle> vehicles = new ArrayList<Vehicle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        prepareActionBarToBack(getString(R.string.vehicle));

        adapter = new GenericAdapter<Vehicle>(VehicleList.this, VehicleList.this, vehicles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        WebFacade.retrieveListOfVehicles(new WebFacade.QueryWebCallback<Vehicle>() {

            @Override
            public void onQueryResult(List<Vehicle> data, Exception e) {

                if (e == null) {
                    vehicles.addAll(data);
                } else {
                    vehicles.addAll(VehicleDAO.retrieveAll());
                }

                adapter.notifyDataSetChanged();
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleList.this, VehicleEntry.class);
                startActivityForResult(intent, RELOAD_LIST_CODE);
            }
        });
    }


    @Override
    public void onDataSelected(View view, int position) {

        Vehicle vehicle = vehicles.get(position);
        Intent intent = new Intent(VehicleList.this, VehicleEntry.class);
        Bundle extras = new Bundle();
        extras.putSerializable(Vehicle.class.getSimpleName(), vehicle);
        intent.putExtras(extras);
        startActivityForResult(intent, RELOAD_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RELOAD_LIST_CODE && resultCode == RESULT_OK) {

            Vehicle vehicle = (Vehicle)data.getSerializableExtra(Vehicle.class.getSimpleName());

            if (vehicles.contains(vehicle)) {
                int index = vehicles.indexOf(vehicle);
                vehicles.set(index, vehicle);
            } else {
                vehicles.add(vehicle);
                adapter.notifyItemInserted(0);
            }

            adapter.notifyDataSetChanged();
        }
    }
}