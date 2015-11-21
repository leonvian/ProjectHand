package com.lvc.syndichand.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Vehicle")
public class Vehicle extends Model {

    @Column
    private String parseIdentifier;

    @Column
    private String model;

    @Column
    private String color;

    @Column
    private String carBoard; // placa

    @Column
    private String unityParseIdentifier; // Unidade?

    public Vehicle() {
    }

    public Vehicle(String model, String color, String carBoard, String unityParseIdentifier) {
        this.model = model;
        this.color = color;
        this.carBoard = carBoard;
        this.unityParseIdentifier = unityParseIdentifier;
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

    public String getUnityParseIdentifier() {
        return unityParseIdentifier;
    }

    public void setUnityParseIdentifier(String unityParseIdentifier) {
        this.unityParseIdentifier = unityParseIdentifier;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }
}
