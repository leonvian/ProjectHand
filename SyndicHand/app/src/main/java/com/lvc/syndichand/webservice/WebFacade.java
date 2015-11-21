package com.lvc.syndichand.webservice;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.CommonArea;
import com.lvc.syndichand.model.Condominium;
import com.lvc.syndichand.model.CondominiumNotCreatedYetException;
import com.lvc.syndichand.model.ParseData;
import com.lvc.syndichand.model.Unity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 10/20/15.
 */
public class WebFacade {

    //fxsJlXI6nj
    public static void loadCondominiumByCondominiumCode(String condominiumCode, final UniqueQueryWebCallback<Condominium> uniqueQueryWebCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Condominium.class.getSimpleName());
        query.whereEqualTo("objectId", condominiumCode);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Condominium condominium = new Condominium();
                    condominium.toObject(object);
                    uniqueQueryWebCallback.onQueryResult(condominium, null);
                } else {
                    uniqueQueryWebCallback.onQueryResult(null, e);
                }
            }
        });
    }

    public static void retrieveListOfCommonAreas(final QueryWebCallback<CommonArea> queryWebCallback) throws CondominiumNotCreatedYetException {
        query(CommonArea.class, new FindCallback<ParseObject>() {

            public void done(List<ParseObject> objects, ParseException e) {
                List<CommonArea> dataList = new ArrayList<CommonArea>();
                if (e == null) {
                    for (ParseObject parseObject : objects) {
                        CommonArea data = new CommonArea();
                        data.toObject(parseObject);
                        dataList.add(data);
                    }
                    queryWebCallback.onQueryResult(dataList, null);
                } else {
                    queryWebCallback.onQueryResult(dataList, e);
                }

            }

        });
    }

    public static void retrieveListOfUnitys(final QueryWebCallback<Unity> queryWebCallback) throws CondominiumNotCreatedYetException {
        query(Unity.class,new FindCallback<ParseObject>() {

            public void done(List<ParseObject> objects, ParseException e) {
                List<Unity> unities = new ArrayList<Unity>();
                if (e == null) {
                    for (ParseObject parseObject : objects) {
                        Unity unity = new Unity();
                        unity.toObject(parseObject);
                        unities.add(unity);
                    }
                    queryWebCallback.onQueryResult(unities, null);
                } else {
                    queryWebCallback.onQueryResult(unities, e);
                }

            }

        });
    }

    private static void query(Class<? extends ParseData> classData, final FindCallback<ParseObject> findCallback) throws CondominiumNotCreatedYetException {
        String condominiumIdentifier = retrieveCondominiumOrThrowException();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(classData.getSimpleName());
        query.whereEqualTo(Condominium.CONDOMINIUM_ID_KEY, condominiumIdentifier);
        query.findInBackground(findCallback);
    }

    private static String retrieveCondominiumOrThrowException() throws CondominiumNotCreatedYetException {
        String condominiumIdentifier = CondominiumDAO.retrieveCondominiumIdentifier();
        if (condominiumIdentifier == null) {
            throw new CondominiumNotCreatedYetException();
        }

        return condominiumIdentifier;
    }

    public static void saveOrUpdateData(ParseData parseData, final WebCallback webCallback) {
        ParseObject parseObject = parseData.toParseObject();
        String parseIdentifier = parseData.getParseUniqueID();
        if (parseIdentifier == null) {
            saveParseObject(parseObject, webCallback);
        } else {
            updateParseObject(parseData, webCallback);
        }
    }

    private static void updateParseObject(final ParseData parseData, final WebCallback webCallback) {
        String className = parseData.getClass().getSimpleName();
        String parseIdentifier = parseData.getParseUniqueID();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.getInBackground(parseIdentifier,  new GetCallback<ParseObject>() {
            public void done(ParseObject target, ParseException e) {
                if (e == null) {
                    String parseId = target.getObjectId();
                    parseData.updateParseObject(target);
                    saveParseObject(target, webCallback);
                } else {
                    webCallback.onWorkDone(null, e);
                }
            }
        });
    }

    private static void saveParseObject(final ParseObject parseObject, final WebCallback webCallback) {
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                webCallback.onWorkDone(parseObject.getObjectId(), e);
            }
        });
    }

    public interface WebCallback {
        public void onWorkDone(String webID, Exception e);
    }

    public interface QueryWebCallback<T> {

        public void onQueryResult(List<T> data, Exception e);
    }

    public interface UniqueQueryWebCallback<T> {
        public void onQueryResult(T data, Exception e);
    }


}