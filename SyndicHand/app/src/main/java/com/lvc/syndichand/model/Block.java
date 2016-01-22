package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by administrator on 10/26/15.
 */
@Table(name = "Block")
public class Block extends Model implements ParseData, Serializable {


    public static final String KEY_NAME = "Name";
    public static final String KEY_MOBILE_ID = "MobileID";

    private String parseID;
    private String name;
    private String condominiumID;


    public String getParseID() {
        return parseID;
    }

    public void setParseID(String parseID) {
        this.parseID = parseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondominiumID() {
        return condominiumID;
    }

    public void setCondominiumID(String condominiumID) {
        this.condominiumID = condominiumID;
    }

    @Override
    public String getParseUniqueID() {
        return parseID;
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {
        parseObject.put(KEY_MOBILE_ID, getId());
        parseObject.put(KEY_NAME, getName());
        parseObject.put(Condominium.CONDOMINIUM_ID_KEY, getCondominiumID());
    }

    @Override
    public ParseObject toParseObject() {
        final ParseObject parseObject = new ParseObject(getClass().getSimpleName());
        updateParseObject(parseObject);

        return parseObject;
    }

    @Override
    public void toObject(ParseObject parseObject) {
        parseID = parseObject.getObjectId();
        name = parseObject.getString(KEY_NAME);
        condominiumID = parseObject.getString(Condominium.CONDOMINIUM_ID_KEY);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if(parseID != null) {
            return parseID.equals(block.getParseID());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parseID != null ? parseID.hashCode() : 0);
        return result;
    }
}
