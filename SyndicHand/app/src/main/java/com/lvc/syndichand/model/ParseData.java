package com.lvc.syndichand.model;

import com.parse.ParseObject;

/**
 * Created by administrator on 10/26/15.
 */
public interface ParseData {

    public String getParseUniqueID();

    public void updateParseObject(ParseObject parseObject);

    public ParseObject toParseObject();

    public void toObject(ParseObject parseObject);
}
