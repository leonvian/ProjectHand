package com.lvc.syndichand.model;

import android.content.Context;

import com.lvc.syndichand.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by leonardo2050 on 1/26/16.
 */
public class PendingRegisterData {

    private static final String SPACE = " ";
    private static final String BROKE_LINE = "<br>";

    private Unity unity;
    private HashMap<String, List<Register>> hashMapRegistersData;

    public PendingRegisterData(Unity unity, HashMap<String, List<Register>> hashMapRegistersData) {
        this.unity = unity;
        this.hashMapRegistersData = hashMapRegistersData;
    }

    public Unity getUnity() {
        return unity;
    }

    public void setUnity(Unity unity) {
        this.unity = unity;
    }


    public HashMap<String, List<Register>> getHashMapRegistersData() {
        return hashMapRegistersData;
    }

    public void setHashMapRegistersData(HashMap<String, List<Register>> hashMapRegistersData) {
        this.hashMapRegistersData = hashMapRegistersData;
    }

    public String generateRegistersOrdenateByDate(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> dates = hashMapRegistersData.keySet();
        for(String date : dates) {
            stringBuilder.append("<h4>" + date + "</h4>" );
            List<Register> registersByDate = hashMapRegistersData.get(date);

            for (Register register : registersByDate) {
                stringBuilder.append("<b>" +typeToString(context,register.getType()) + "</b>");
                stringBuilder.append(SPACE);
                stringBuilder.append(register.getCurrentConsume());
                stringBuilder.append(BROKE_LINE);
            }

        }

        return stringBuilder.toString();
    }


    private String typeToString(Context context, int type) {
        switch (type) {
            case Register.REGISTER_COLD_WATER:
                return context.getString(R.string.cold_water);

            case Register.REGISTER_HOT_WATER:
                return context.getString(R.string.hot_water);

            case Register.REGISTER_GAS:
                return context.getString(R.string.gas);

        }

        return null;
    }

}
