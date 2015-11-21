package com.lvc.syndichand.database;

import com.activeandroid.query.Select;
import com.lvc.syndichand.model.CommonArea;

import java.util.List;

/**
 * Created by administrator on 10/17/15.
 */
public class CommonAreaDAO {

    public static List<CommonArea> retrieveAll() {
        List<CommonArea> areas = new Select().from(CommonArea.class).execute();

        return areas;
    }


    public static void removeCommonArea(CommonArea commonArea) {
        commonArea.delete();
    }

}
