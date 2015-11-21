package com.lvc.syndichand.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.parse.ParseObject;

import java.io.Serializable;

@Table(name = "CommonArea")
public class CommonArea extends Model implements ParseData, Serializable {


    public static final String MOBILE_ID_KEY = "MobileID";
    public static final String CONDOMINIUM_ID_KEY = Condominium.CONDOMINIUM_ID_KEY;
    public static final String NAME_KEY = "Name";
    public static final String MAX_PEOPLE_KEY = "MaxPeople";
    public static final String WORK_TIME_BEGIN_KEY = "WorkTimeBegin";
    public static final String WORK_TIME_END_KEY = "WorkTimeEND";
    public static final String NEED_SYNDIC_VALIDATION_KEY = "NeedSyndicValidation";

    public static final String SCHEDULE_COST_KEY = "ScheduleCost";
    public static final String OBSERVATION_KEY = "Observation";

    @Column
    private String condominiumParseIdentifier;
    @Column
    private String parseIdentifier;
    @Column
    private String name;
    @Column
    private int maxPeople;
    @Column
    private String workTimeBegin;
    @Column
    private String workTimeEnd;
    @Column
    private boolean needSyndicValidation;
    @Column
    private String scheduleCost; //Custo Reserva
    @Column
    private String observation;

    public CommonArea() {
    }

    public CommonArea(String name, int maxPeople, String workTimeBegin,String workTimeEnd, String scheduleCost, String observation, boolean needSyndicValidation) {
        this.name = name;
        this.maxPeople = maxPeople;
        this.workTimeBegin = workTimeBegin;
        this.workTimeEnd = workTimeEnd;
        this.scheduleCost = scheduleCost;
        this.observation = observation;
        this.needSyndicValidation = needSyndicValidation;
    }

    public void loadData(String name, int maxPeople, String workTimeBegin,String workTimeEnd, String scheduleCost, String observation, boolean needSyndicValidation) {
        this.name = name;
        this.maxPeople = maxPeople;
        this.workTimeBegin = workTimeBegin;
        this.workTimeEnd = workTimeEnd;
        this.scheduleCost = scheduleCost;
        this.observation = observation;
        this.needSyndicValidation = needSyndicValidation;
    }

    public String getCondominiumParseIdentifier() {
        return condominiumParseIdentifier;
    }

    public void setCondominiumParseIdentifier(String condominiumParseIdentifier) {
        this.condominiumParseIdentifier = condominiumParseIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getScheduleCost() {
        return scheduleCost;
    }

    public void setScheduleCost(String scheduleCost) {
        this.scheduleCost = scheduleCost;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getWorkTimeBegin() {
        return workTimeBegin;
    }

    public void setWorkTimeBegin(String workTimeBegin) {
        this.workTimeBegin = workTimeBegin;
    }

    public String getWorkTimeEnd() {
        return workTimeEnd;
    }

    public void setWorkTimeEnd(String workTimeEnd) {
        this.workTimeEnd = workTimeEnd;
    }

    public boolean isNeedSyndicValidation() {
        return needSyndicValidation;
    }

    public void setNeedSyndicValidation(boolean needSyndicValidation) {
        this.needSyndicValidation = needSyndicValidation;
    }

    public String getParseIdentifier() {
        return parseIdentifier;
    }

    public void setParseIdentifier(String parseIdentifier) {
        this.parseIdentifier = parseIdentifier;
    }

    @Override
    public String toString() {
        return name;
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
        condominiumParseIdentifier = parseObject.getString(CONDOMINIUM_ID_KEY);
        name = parseObject.getString(NAME_KEY);
        maxPeople = parseObject.getInt(MAX_PEOPLE_KEY);
        workTimeBegin  = parseObject.getString(WORK_TIME_BEGIN_KEY);
        workTimeEnd = parseObject.getString(WORK_TIME_END_KEY);

        scheduleCost = parseObject.getString(SCHEDULE_COST_KEY);
        observation = parseObject.getString(OBSERVATION_KEY);

        needSyndicValidation = parseObject.getBoolean(NEED_SYNDIC_VALIDATION_KEY);
    }

    @Override
    public void updateParseObject(ParseObject parseObject) {
        parseObject.put(MOBILE_ID_KEY, getId());
        parseObject.put(CONDOMINIUM_ID_KEY, getCondominiumParseIdentifier());

        parseObject.put(NAME_KEY, getName());
        parseObject.put(MAX_PEOPLE_KEY, getMaxPeople());
        parseObject.put(WORK_TIME_BEGIN_KEY, getWorkTimeBegin());
        parseObject.put(WORK_TIME_END_KEY, getWorkTimeEnd());

        parseObject.put(SCHEDULE_COST_KEY, getScheduleCost());
        parseObject.put(OBSERVATION_KEY, getObservation());
        parseObject.put(NEED_SYNDIC_VALIDATION_KEY, isNeedSyndicValidation());
    }

    @Override
    public String getParseUniqueID() {
        return getParseIdentifier();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonArea that = (CommonArea) o;

        return parseIdentifier.equals(that.parseIdentifier);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parseIdentifier.hashCode();
        return result;
    }
}
