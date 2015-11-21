package com.lvc.syndichand.database;

import com.activeandroid.query.Select;
import com.lvc.syndichand.model.Unity;

import java.util.List;

/**
 * Created by administrator on 9/17/15.
 */
public class UnityDAO {


    public static List<Unity> retrieveAll() {
        List<Unity> owners = new Select().from(Unity.class).execute();

        return owners;
    }
    // from.where("IdOwner = ?", idOwner);
    public static Unity retrieveUnityByid(Long mobileId) {
        Unity unity = new Select().from(Unity.class).where("mId = ?", mobileId).executeSingle();
        return unity;
    }
}
