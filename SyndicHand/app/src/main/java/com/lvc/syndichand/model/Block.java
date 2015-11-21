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


    private String parseID;
    private String name;
    private String condominiumID;



    @Override
    public String getParseUniqueID() {
        return parseID;
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {

    }

    @Override
    public ParseObject toParseObject() {
        return null;
    }

    @Override
    public void toObject(ParseObject parseObject) {

    }
}
