package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Table(name = "Register")
public class Register extends Model implements Serializable, ParseData {

    public static final int REGISTER_COLD_WATER = 1;
    public static final int REGISTER_GAS = 2;
    public static final int REGISTER_HOT_WATER = 3;


    public static final String KEY_USER = "UserId"; // O síndico que cadastrou esse registro
    public static final String KEY_MOBILE_ID = "MobileID";
    public static final String KEY_ID_UNITY = "IdUnity";
    public static final String KEY_ID_BLOCK = "IdBlock";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_CURRENT_CONSUME = "CurrentConsume";
    public static final String KEY_DATE = "Date";
    public static final String KEY_DAY = "Day";
    public static final String KEY_MONTH = "Month";
    public static final String KEY_YEAR = "Year";

    @Column
    private String parseIdentifier;

    @Column(name = "IdUser")
    private String idUser;

    @Column(name = "IdUnity")
    private String idUnity;

    @Column(name = "IdBlock")
    private String idBlock;

    @Column(name = "IdCondominium")
    private String idCondominium;

    @Column(name = "Type")
    private int type;

    @Column(name = "CurrentConsume")
    private double currentConsume;

    @Column(name = "RegisterDate")
    private Date date;

    @Column(name = "Day")
    private int day;

    @Column(name = "Month")
    private int month;

    @Column(name = "Year")
    private int year;


    public Register() {
    }

    public Register(int type, double currentConsume, Date date,String idUnity,String idCondominium, String user) {
        this.type = type;
        this.currentConsume = currentConsume;
        this.date = date;
        this.idUnity = idUnity;
        this.idCondominium = idCondominium;
        this.idUser = user;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
    }

    public String getIdBlock() {
        return idBlock;
    }

    public void setIdBlock(String idBlock) {
        this.idBlock = idBlock;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdCondominium() {
        return idCondominium;
    }

    public void setIdCondominium(String idCondominium) {
        this.idCondominium = idCondominium;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIdUnity() {
        return idUnity;
    }

    public void setIdUnity(String idUnity) {
        this.idUnity = idUnity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getCurrentConsume() {
        return currentConsume;
    }

    public void setCurrentConsume(double currentConsume) {
        this.currentConsume = currentConsume;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }


    @Override
    public String getParseUniqueID() {
        return getParseIdentifier();
    }
    @Override
    public void toObject(ParseObject parseObject) {
        parseIdentifier = parseObject.getObjectId();
        idUnity = parseObject.getString(KEY_ID_UNITY);
        idCondominium = parseObject.getString(Condominium.CONDOMINIUM_ID_KEY);
        idBlock = parseObject.getString(KEY_ID_BLOCK);
        day = parseObject.getInt(KEY_DAY);
        month = parseObject.getInt(KEY_MONTH);
        year = parseObject.getInt(KEY_YEAR);
        date = parseObject.getDate(KEY_DATE);
        currentConsume = parseObject.getDouble(KEY_CURRENT_CONSUME);
        type = parseObject.getInt(KEY_TYPE);
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {
        parseObject.put(KEY_MOBILE_ID, getId());
        parseObject.put(KEY_ID_UNITY, getIdUnity());
        String myIdBlock = "";
        if(getIdBlock() != null) {
            myIdBlock = getIdBlock();
        }
        parseObject.put(KEY_ID_BLOCK, myIdBlock);
        parseObject.put(KEY_USER, getIdUser());
        parseObject.put(KEY_TYPE, getType());
        parseObject.put(KEY_CURRENT_CONSUME, getCurrentConsume());
        parseObject.put(KEY_DATE, getDate());
        parseObject.put(KEY_DAY, getDay());
        parseObject.put(KEY_MONTH, getMonth());
        parseObject.put(KEY_YEAR, getYear());
        parseObject.put(Condominium.CONDOMINIUM_ID_KEY, getIdCondominium());
    }

    @Override
    public ParseObject toParseObject() {
        final ParseObject parseObject = new ParseObject(getClass().getSimpleName());
        updateParseObject(parseObject);
        return parseObject;
    }



}
