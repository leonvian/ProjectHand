package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;

@Table(name = "Condominium")
public class Condominium extends Model implements Serializable, ParseData {

    public static final String CONDOMINIUM_ID_KEY = "CondominiumIdentifier";

    @Column
    private String parseIdentifier;

    @Column
    private String name;

    @Column
    private String observation;

    @Column
    private String address;

    @Column
    private String cep;

    @Column
    private boolean hotWater;

    @Column
    private boolean coldWater;

    @Column
    private boolean gas;

    public Condominium() {
    }

    public Condominium(String parseIdentifier, String name, String address, String cep, boolean hotWater, boolean coldWater, boolean gas, String observation) {
        this.parseIdentifier = parseIdentifier;
        this.name = name;
        this.address = address;
        this.cep = cep;
        this.hotWater = hotWater;
        this.coldWater = coldWater;
        this.gas = gas;
        this.observation = observation;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isHotWater() {
        return hotWater;
    }

    public void setHotWater(boolean hotWater) {
        this.hotWater = hotWater;
    }

    public boolean isColdWater() {
        return coldWater;
    }

    public void setColdWater(boolean coldWater) {
        this.coldWater = coldWater;
    }

    public boolean isGas() {
        return gas;
    }

    public void setGas(boolean gas) {
        this.gas = gas;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getParseUniqueID() {
        return parseIdentifier;
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {
        parseObject.put("MobileID", getId());
        parseObject.put("Name", getName());
        parseObject.put("Observation", getObservation());
        parseObject.put("Address", getAddress());
        parseObject.put("CEP", getCep());
        parseObject.put("hotwater", isHotWater());
        parseObject.put("coldwater", isColdWater());
        parseObject.put("gas", isGas());
    }

    @Override
    public ParseObject toParseObject() {
        final ParseObject parseObject = new ParseObject(getClass().getSimpleName());
        updateParseObject(parseObject);

        return parseObject;
    }

    @Override
    public void toObject(ParseObject parseObject) {
        setName(parseObject.getString("Name"));
        setObservation(parseObject.getString("Observation"));
        setAddress(parseObject.getString("Address"));
        setCep(parseObject.getString("CEP"));
        setHotWater(parseObject.getBoolean("hotwater"));
        setColdWater(parseObject.getBoolean("coldwater"));
        setGas(parseObject.getBoolean("gas"));
        String objectID = parseObject.getObjectId();
        setParseIdentifier(objectID);
    }
}
