package com.lvc.syndichand.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;

@Table(name = "Vehicle")
public class Vehicle extends Model implements ParseData, Serializable {

    public static final String MOBILE_ID_KEY = "MobileID";
    public static final String MODEL_KEY = "Model";
    public static final String COLOR_KEY = "Color";
    public static final String CAR_BOARD_KEY = "CarBoard";
    public static final String UNITY_KEY = "Unity";

    @Column
    private String condominiumParseIdentifier;

    @Column
    private String parseIdentifier;

    @Column
    private String model;

    @Column
    private String color;

    @Column
    private String carBoard;

    @Column
    private String unity;

    public Vehicle() {
    }

    public Vehicle(String model, String color, String carBoard, String unity) {
        this.model = model;
        this.color = color;
        this.carBoard = carBoard;
        this.unity = unity;
    }

    public void loadData(String model, String color, String carBoard, String unity) {
        this.model = model;
        this.color = color;
        this.carBoard = carBoard;
        this.unity = unity;
    }

    public String getCondominiumParseIdentifier() {
        return condominiumParseIdentifier;
    }

    public void setCondominiumParseIdentifier(String condominiumParseIdentifier) {
        this.condominiumParseIdentifier = condominiumParseIdentifier;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarBoard() {
        return carBoard;
    }

    public void setCarBoard(String carBoard) {
        this.carBoard = carBoard;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }

    @Override
    public String getParseUniqueID() {
        return parseIdentifier;
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {

        parseObject.put(Condominium.CONDOMINIUM_ID_KEY, getCondominiumParseIdentifier());
        parseObject.put(MOBILE_ID_KEY, getId());
        parseObject.put(MODEL_KEY, getModel());
        parseObject.put(COLOR_KEY, getColor());
        parseObject.put(CAR_BOARD_KEY, getCarBoard());
        parseObject.put(UNITY_KEY, getUnity());
    }

    @Override
    public ParseObject toParseObject() {
        final ParseObject parseObject = new ParseObject(getClass().getSimpleName());
        updateParseObject(parseObject);
        return parseObject;
    }

    @Override
    public void toObject(ParseObject parseObject) {

        condominiumParseIdentifier = parseObject.getString(Condominium.CONDOMINIUM_ID_KEY);
        parseIdentifier = parseObject.getObjectId();
        model = parseObject.getString(MODEL_KEY);
        color = parseObject.getString(COLOR_KEY);
        carBoard  = parseObject.getString(CAR_BOARD_KEY);
        unity = parseObject.getString(UNITY_KEY);
    }

    @Override
    public String toString() {
        return carBoard;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Vehicle that = (Vehicle) o;

        return parseIdentifier.equals(that.parseIdentifier);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parseIdentifier.hashCode();
        return result;
    }
}