package com.lvc.syndichand;

import android.test.AndroidTestCase;
import android.util.Log;

import com.lvc.syndichand.database.RegisterDAO;
import com.lvc.syndichand.database.UnityDAO;
import com.lvc.syndichand.model.Register;
import com.lvc.syndichand.model.Unity;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 10/14/15.
 */
public class RegisterDAOTest extends AndroidTestCase {

    public void testIsAllRegistersOk() {
        Log.i("testOwnerBasic", "testOwnerBasic");
        //String name,String apartamentNumber, int blockId, String email, String ddd, String phone, boolean rent, boolean pet, boolean syndic, boolean auditCommittee
        List<Unity> owners = UnityDAO.retrieveAll();

        Long ownerId = null;
        if(owners.isEmpty()) {
            Unity owner = new Unity("Leonardo Casasanta","123","0","leonardo2050@gmail.com","31","88168360",false,false,false,false);
            ownerId = owner.save();
        } else {
            ownerId = owners.get(0).getId();
        }

        //(int type, double currentConsume, Date date,Long idOwner)
        Register register = new Register(Register.REGISTER_COLD_WATER,20,new Date(), ownerId);
        register.save();

        boolean isAllRegisterOk = RegisterDAO.isAllRegistersOk(ownerId);
        assertTrue(isAllRegisterOk);
    }

}
