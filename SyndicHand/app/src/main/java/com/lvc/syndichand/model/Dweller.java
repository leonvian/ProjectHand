package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

//Morador
@Table(name = "Dweller")
public class Dweller extends Model {

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String identifierUnity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifierUnity() {
        return identifierUnity;
    }

    public void setIdentifierUnity(String identifierUnity) {
        this.identifierUnity = identifierUnity;
    }
}
