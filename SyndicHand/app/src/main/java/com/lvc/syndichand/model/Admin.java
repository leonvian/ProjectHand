package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Admin")
public class Admin extends Model {

    @Column
    private String parseIdentifier;

    @Column
    private String name;

    @Column
    private String contact;

    @Column
    private String cnpj;

    @Column
    private String phone;

    @Column
    private String emailPaymentAccount;

    @Column
    private String emailFinancial;

    @Column
    private String emailContact;

    @Column
    private String emailTax;

    public Admin() {
    }

    public Admin(String name, String contact, String cnpj, String phone, String emailPaymentAccount, String emailFinancial, String emailContact, String emailTax) {
        this.name = name;
        this.contact = contact;
        this.cnpj = cnpj;
        this.phone = phone;
        this.emailPaymentAccount = emailPaymentAccount;
        this.emailFinancial = emailFinancial;
        this.emailContact = emailContact;
        this.emailTax = emailTax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailPaymentAccount() {
        return emailPaymentAccount;
    }

    public void setEmailPaymentAccount(String emailPaymentAccount) {
        this.emailPaymentAccount = emailPaymentAccount;
    }

    public String getEmailFinancial() {
        return emailFinancial;
    }

    public void setEmailFinancial(String emailFinancial) {
        this.emailFinancial = emailFinancial;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getEmailTax() {
        return emailTax;
    }

    public void setEmailTax(String emailTax) {
        this.emailTax = emailTax;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }
}
