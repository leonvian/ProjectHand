package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by administrator on 10/22/15.
 */
@Table(name = "Unity")
public class Unity extends Model implements Serializable, ParseData {


    public static final String PARSE_ID_KEY = "ParseID";
    public static final String MOBILE_ID_KEY = "MobileID";
    public static final String RESPONSABLE_NAME_KEY =   "ResponsableName";
    public static final String RESPONSABLE_EMAIL_KEY = "ResponsableEmail";
    public static final String APARTMENT_NUMBER_KEY = "ApartmentNumber";
    public static final String BLOCK_ID_KEY = "BlockId";
    public static final String PHONE_DDD_KEY = "DDD";
    public static final String PHONE_KEY = "Phone";
    public static final String RENT_KEY = "Rent";
    public static final String PET_KEY = "Pet";
    public static final String SYNDIC_KEY = "Syndic";
    public static final String AUDIT_COMMITEE_KEY = "AuditCommittee";


    public static final String UNITY_ID_KEY = "UNITY_ID_KEY";

    @Column
    private String condominiumIdentifier = "";

    @Column
    private String parseIdentifier;

    @Column(name = "ApartamentNumber")
    private String apartamentNumber;

    @Column(name = "ResponsableName")
    private String responsableName;

    @Column(name = "BlockId")
    private String parseIdentifierBlock = "";

    @Column(name = "Email")
    private String email;

    @Column(name = "DDD")
    private String ddd;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Rent")
    private boolean rent; // é alugado

    @Column(name = "Pet")
    private boolean pet; // Tem animais de estimação?

    @Column(name = "Syndic")
    private boolean syndic; // É sindico?

    @Column(name = "AuditCommittee")
    private boolean auditCommittee;// Conselho Fiscal


    public Unity() {
    }

    public Unity(String responsableName,String apartamentNumber, String parseIdentifier, String email, String ddd, String phone, boolean rent, boolean pet, boolean syndic, boolean auditCommittee) {
        this.responsableName = responsableName;
        this.apartamentNumber = apartamentNumber;
        this.parseIdentifier = parseIdentifier;
        this.email = email;
        this.ddd = ddd;
        this.phone = phone;
        this.rent = rent;
        this.pet = pet;
        this.syndic = syndic;
        this.auditCommittee = auditCommittee;

    }

    public String getResponsableName() {
        return responsableName;
    }

    public void setResponsableName(String responsableName) {
        this.responsableName = responsableName;
    }

    public String getApartamentNumber() {
        return apartamentNumber;
    }

    public void setApartamentNumber(String apartamentNumber) {
        this.apartamentNumber = apartamentNumber;
    }

    public String getParseIdentifierBlock() {
        return parseIdentifierBlock;
    }

    public void setParseIdentifierBlock(String parseIdentifierBlock) {
        this.parseIdentifierBlock = parseIdentifierBlock;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    public boolean isPet() {
        return pet;
    }

    public void setPet(boolean pet) {
        this.pet = pet;
    }

    public boolean isSyndic() {
        return syndic;
    }

    public void setSyndic(boolean syndic) {
        this.syndic = syndic;
    }

    public boolean isAuditCommittee() {
        return auditCommittee;
    }

    public void setAuditCommittee(boolean auditCommittee) {
        this.auditCommittee = auditCommittee;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }

    public String getCondominiumIdentifier() {
        return condominiumIdentifier;
    }

    public void setCondominiumIdentifier(String condominiumIdentifier) {
        this.condominiumIdentifier = condominiumIdentifier;
    }

    @Override
    public String getParseUniqueID() {
        return getParseIdentifier();
    }


    @Override
    public void updateParseObject(ParseObject parseObject) {
        parseObject.put(MOBILE_ID_KEY, getId());
        parseObject.put(RESPONSABLE_NAME_KEY, getResponsableName());
        parseObject.put(RESPONSABLE_EMAIL_KEY, getEmail());
        parseObject.put(APARTMENT_NUMBER_KEY, getApartamentNumber());
        parseObject.put(PHONE_DDD_KEY, getDdd());
        parseObject.put(PHONE_KEY, getPhone());
        parseObject.put(RENT_KEY, isRent());
        parseObject.put(PET_KEY, isPet());
        parseObject.put(SYNDIC_KEY, isSyndic());
        parseObject.put(AUDIT_COMMITEE_KEY, isAuditCommittee());
        parseObject.put(Condominium.CONDOMINIUM_ID_KEY, getCondominiumIdentifier());
        parseObject.put(BLOCK_ID_KEY, getParseIdentifierBlock());
    }

    @Override
    public ParseObject toParseObject() {
        final ParseObject parseObject = new ParseObject(getClass().getSimpleName());
        updateParseObject(parseObject);
        return parseObject;
    }

    @Override
    public void toObject(ParseObject parseObject) {
        parseIdentifier = parseObject.getObjectId();
        responsableName = parseObject.getString(RESPONSABLE_NAME_KEY);
        email = parseObject.getString(RESPONSABLE_EMAIL_KEY);
        apartamentNumber = parseObject.getString(APARTMENT_NUMBER_KEY);
        phone = parseObject.getString(PHONE_KEY);
        ddd = parseObject.getString(PHONE_DDD_KEY);


        pet = parseObject.getBoolean(PET_KEY);
        syndic = parseObject.getBoolean(SYNDIC_KEY);
        rent = parseObject.getBoolean(RENT_KEY);
        auditCommittee = parseObject.getBoolean(AUDIT_COMMITEE_KEY);

        parseIdentifierBlock = parseObject.getString(BLOCK_ID_KEY);
        condominiumIdentifier = parseObject.getString(Condominium.CONDOMINIUM_ID_KEY);

        parseObject.getLong(MOBILE_ID_KEY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Unity unity = (Unity) o;

        return !(parseIdentifier != null ? !parseIdentifier.equals(unity.parseIdentifier) : unity.parseIdentifier != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parseIdentifier != null ? parseIdentifier.hashCode() : 0);
        return result;
    }
}

