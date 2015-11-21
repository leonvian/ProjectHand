package com.lvc.syndichand.database;

import com.activeandroid.query.Select;
import com.lvc.syndichand.model.Condominium;
import com.lvc.syndichand.model.User;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.ParseUser;

/**
 * Created by administrator on 10/24/15.
 */
public class CondominiumDAO {

    private static String condominiumIdentifier = null;

    public static Condominium retrieveCondominium() {
        Condominium condominium = new Select().from(Condominium.class).executeSingle();

        return condominium;
    }

    public static String retrieveCondominiumIdentifier() {

        loadCondominiumIdentifierByUser();

        if (condominiumIdentifier == null) {
            Condominium condominium = retrieveCondominium();
            if (condominium != null)
                condominiumIdentifier = condominium.getParseIdentifier();
        }

        return condominiumIdentifier;
    }

    private static void loadCondominiumIdentifierByUser() {
        if(condominiumIdentifier == null)
            return;

        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser != null) {
            String condominiumID = parseUser.getString(User.CONDOMINIUM_ID_KEY);
            if(condominiumID != null) {
                condominiumIdentifier = condominiumID;
                if(!hasCondominiumOnDatabase()) {
                    WebFacade.loadCondominiumByCondominiumCode(condominiumID, new WebFacade.UniqueQueryWebCallback<Condominium>() {
                        @Override
                        public void onQueryResult(Condominium data, Exception e) {
                            if(data != null) {
                                data.save();
                            }
                        }
                    });
                }
            }
        }
    }

    private static boolean hasCondominiumOnDatabase() {
        int count = new Select().from(Condominium.class).count();
        if(count == 0) {
            return false;
        } else {
            return true;
        }

    }
}
