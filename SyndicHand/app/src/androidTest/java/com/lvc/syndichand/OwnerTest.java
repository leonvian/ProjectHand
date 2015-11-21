package com.lvc.syndichand;

import android.test.AndroidTestCase;

import com.lvc.syndichand.database.UnityDAO;
import com.lvc.syndichand.model.Unity;

import java.util.List;

/**
 * Created by administrator on 9/14/15.
 */
public class OwnerTest extends AndroidTestCase {


    public void testSave() {
        Unity owner = new Unity("Leonardo Casasanta","123","0","leonardo2050@gmail.com","31","88168360",false,false,false,false);
        owner.setParseIdentifier("Leonardo1");

        owner.save();
        Long mobileId =  owner.getId();
        assertNull(mobileId);

        List<Unity> unities = UnityDAO.retrieveAll();
        boolean hasIdentifier = false;
        for(Unity unity : unities) {
            if(unity.getParseIdentifier().equals("Leonardo1")) {
                hasIdentifier = true;
            }
        }

        assertTrue(hasIdentifier);
    }
}
