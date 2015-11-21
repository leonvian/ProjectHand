package com.lvc.syndichand;

import com.parse.Parse;

/**
 * com.lvc.syndichand.AppSyndicHand
 */
public class AppSyndicHand extends com.activeandroid.app.Application {

    private static final String APPLICATION_ID = "ujuwXSWAwIINTuKbHQF0VyCZasda2L3nbmHDpHrv";
    private static final String CLIENT_KEY = "CTVYvwrJ7tWI1hrpN1UpFZ4BsAqihmQauGSTJgAM";

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }
}
