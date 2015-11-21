package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lvc.syndichand.database.CommonAreaDAO;
import com.lvc.syndichand.model.CommonArea;
import com.lvc.syndichand.webservice.WebFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 10/17/15.
 */
public class CommonAreaList extends SyndicHandList implements OnDataSelected {

    private static final int RELOAD_LIST_CODE = 10;
    private List<CommonArea> commonAreas = new ArrayList<CommonArea>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActionBarToBack(getString(R.string.common_area));

        adapter = new GenericAdapter<CommonArea>(CommonAreaList.this, CommonAreaList.this, commonAreas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        WebFacade.retrieveListOfCommonAreas(new WebFacade.QueryWebCallback<CommonArea>() {
            @Override
            public void onQueryResult(List<CommonArea> data, Exception e) {
                if (e == null) {
                    commonAreas.addAll(data);
                } else {
                    commonAreas.addAll(CommonAreaDAO.retrieveAll());
                }

                adapter.notifyDataSetChanged();
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommonAreaList.this, CommonAreaEntry.class);
                startActivityForResult(intent, RELOAD_LIST_CODE);
            }
        });
    }


    @Override
    public void onDataSelected(View view, int position) {
        CommonArea commonArea = commonAreas.get(position);
        Intent intent = new Intent(CommonAreaList.this, CommonAreaEntry.class);
        Bundle extras = new Bundle();
        extras.putSerializable(CommonArea.class.getSimpleName(),commonArea);
        intent.putExtras(extras);
        startActivityForResult(intent, RELOAD_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RELOAD_LIST_CODE && resultCode == RESULT_OK) {
            CommonArea commonArea = (CommonArea)data.getSerializableExtra(CommonArea.class.getSimpleName());
            if(commonAreas.contains(commonArea)) {
                int index = commonAreas.indexOf(commonArea);
                commonAreas.set(index, commonArea);
            } else {
                commonAreas.add(commonArea);
                adapter.notifyItemInserted(0);
            }


            adapter.notifyDataSetChanged();
        }
    }


}
