package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lvc.syndichand.database.BlockDAO;
import com.lvc.syndichand.model.Block;
import com.lvc.syndichand.webservice.WebFacade;

import java.util.ArrayList;
import java.util.List;

public class BlockList extends SyndicHandList implements  OnDataSelected {

    private static final int RELOAD_LIST_CODE = 10;
    protected List<Block> blocks = new ArrayList<Block>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActionBarToBack(getString(R.string.blocks));

        adapter = new GenericAdapter<Block>(this, this, blocks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        loadList();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlockList.this, BlockEntry.class);
                startActivityForResult(intent, RELOAD_LIST_CODE);
            }
        });
    }

    private void loadList() {
        showProgressDialog();
        WebFacade.retrieveListOfBlocks(new WebFacade.QueryWebCallback<Block>() {
            @Override
            public void onQueryResult(List<Block> data, Exception e) {
                if (e == null) {
                    blocks.addAll(data);
                } else {
                    blocks.addAll(BlockDAO.retrieveAll());
                }

                adapter.notifyDataSetChanged();
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onDataSelected(View view, int position) {
        Block block = blocks.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Block.class.getSimpleName(), block);
        goToNextScreen(BlockEntry.class, bundle, RELOAD_LIST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RELOAD_LIST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Block block = (Block)extras.get(Block.class.getSimpleName());
            if(blocks.contains(block)) {
                int indexOf = blocks.indexOf(block);
                blocks.set(indexOf, block);
            } else {
                blocks.add(block);
            }

            adapter.notifyDataSetChanged();
        }
    }
}
