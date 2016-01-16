package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.Date;

@Table(name = "NoticeBoard")
public class NoticeBoard extends Model implements ParseData, Serializable {

    public static final String MOBILE_ID_KEY = "MobileID";
    public static final String SIGNED_BY_KEY = "SignedBy";
    public static final String CREATED_AT_KEY = "CreatedAt";
    public static final String MESSAGE_KEY = "Message";
    public static final String TITLE_KEY = "Title";
    public static final String IS_READ_KEY = "IsRead";

    @Column
    private String condominiumParseIdentifier;

    @Column
    private String parseIdentifier;

    @Column
    private String signedBy;

    @Column
    private Date createdAt;

    @Column
    private String message;

    @Column
    private String title;

    @Column
    private boolean isRead;

    public NoticeBoard() {
    }

    public NoticeBoard(String signedBy, Date createdAt, String message, String title, boolean isRead) {
        this.signedBy = signedBy;
        this.createdAt = createdAt;
        this.message = message;
        this.title = title;
        this.isRead = isRead;
    }

    public void loadData(String signedBy, Date createdAt, String message, String title, boolean isRead) {
        this.signedBy = signedBy;
        this.createdAt = createdAt;
        this.message = message;
        this.title = title;
        this.isRead = isRead;
    }

    public String getCondominiumParseIdentifier() {
        return condominiumParseIdentifier;
    }

    public void setCondominiumParseIdentifier(String condominiumParseIdentifier) {
        this.condominiumParseIdentifier = condominiumParseIdentifier;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public String getParseUniqueID() {
        return parseIdentifier;
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {

        parseObject.put(Condominium.CONDOMINIUM_ID_KEY, getCondominiumParseIdentifier());
        parseObject.put(MOBILE_ID_KEY, getId());
        parseObject.put(SIGNED_BY_KEY, getSignedBy());
        parseObject.put(CREATED_AT_KEY, getCreatedAt());
        parseObject.put(MESSAGE_KEY, getMessage());
        parseObject.put(TITLE_KEY, getTitle());
        parseObject.put(IS_READ_KEY, isRead());
    }

    @Override
    public ParseObject toParseObject() {
        final ParseObject parseObject = new ParseObject(getClass().getSimpleName());
        updateParseObject(parseObject);
        return parseObject;
    }

    @Override
    public void toObject(ParseObject parseObject) {

        condominiumParseIdentifier = parseObject.getString(Condominium.CONDOMINIUM_ID_KEY);
        parseIdentifier = parseObject.getObjectId();
        signedBy = parseObject.getString(SIGNED_BY_KEY);
        createdAt = parseObject.getDate(CREATED_AT_KEY);
        message  = parseObject.getString(MESSAGE_KEY);
        title = parseObject.getString(TITLE_KEY);
        isRead = parseObject.getBoolean(IS_READ_KEY);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NoticeBoard that = (NoticeBoard) o;

        return parseIdentifier.equals(that.parseIdentifier);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parseIdentifier.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return title;
    }
}