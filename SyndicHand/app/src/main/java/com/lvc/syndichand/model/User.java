package com.lvc.syndichand.model;

import com.parse.ParseUser;

/**
 * Created by administrator on 11/3/15.
 */
public class User   {

    public static final int STATUS_DENIED = 1;
    public static final int STATUS_OK = 2;
    public static final int STATUS_WAITING_APPROVAL = 3;

    public static final int TYPE_SYNDIC = 1;
    public static final int TYPE_REGULAR_USER = 2;

    public static final String CONDOMINIUM_ID_KEY = "CondominiumID";
    public static final String NAME_KEY = "Name";
    public static final String TYPE_KEY = "Type";
    public static final String STATUS_KEY = "Status";
    public static final String UNITIY_ID_KEY = "UnityID";
    //O primeiro usuário de uma unidade é o administrador daquela unidade, logo, novos usuários que quiserem se cadastrar naquela unidade, terão que ser aprovados por ele. Por sua vez ele é aprovado pelo Sindico
    public static final String UNITIY_ADMIN_KEY = "UnityAdmin";



    public User() {
    }

    public static ParseUser  createUserSyndic(String name, String userName, String password, String email, String condominium, String unityId, boolean unityAdmin) {
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);

        user.put(User.NAME_KEY, name);
        user.put(User.UNITIY_ID_KEY, unityId);
        user.put(User.UNITIY_ADMIN_KEY, unityAdmin);
        user.put(User.CONDOMINIUM_ID_KEY, condominium);
        user.put(User.TYPE_KEY, TYPE_SYNDIC);
        user.put(User.STATUS_KEY, STATUS_OK);


        return user;
    }


}
