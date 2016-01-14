package com.lvc.syndichand.database;

import com.activeandroid.query.Select;
import com.lvc.syndichand.model.Vehicle;

import java.util.List;

public class VehicleDAO {

    public static List<Vehicle> retrieveAll() {
        List<Vehicle> vehicles = new Select().from(Vehicle.class).execute();
        return vehicles;
    }
}