package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "Register")
public class Register extends Model {

    public static final int REGISTER_COLD_WATER = 1;
    public static final int REGISTER_GAS = 2;
    public static final int REGISTER_HOT_WATER = 3;

    @Column
    private String parseIdentifier;

    @Column(name = "Type")
    private int type;

    @Column(name = "CurrentConsume")
    private double currentConsume;

    @Column(name = "RegisterDate")
    private Date date;

    @Column(name = "IdOwner")
    private Long idOwner;


    public Register() {
    }

    public Register(int type, double currentConsume, Date date,Long idOwner) {
        this.type = type;
        this.currentConsume = currentConsume;
        this.date = date;
        this.idOwner = idOwner;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
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

}
