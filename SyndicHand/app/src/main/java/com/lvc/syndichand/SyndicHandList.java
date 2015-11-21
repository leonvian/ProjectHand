package com.lvc.syndichand;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by administrator on 10/23/15.
 */
public class SyndicHandList extends SyndicHandActivity {

    public static final int DEFAULT_NUMBER_COLUMNS = 2;

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    protected GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list_layout);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        gridLayoutManager = new GridLayoutManager(this, DEFAULT_NUMBER_COLUMNS);
    }

}
