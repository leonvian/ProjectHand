package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lvc.syndichand.model.Block;

/**
 * Created by administrator on 12/7/15.
 */

//com.lvc.syndichand.SelectedBlockList
public class SelectedBlockList extends BlockList {

    @Override
    public void onDataSelected(View view, int position) {
        Block block = blocks.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Block.class.getSimpleName(), block);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(RESULT_OK,intent);
        finish();
    }
}
