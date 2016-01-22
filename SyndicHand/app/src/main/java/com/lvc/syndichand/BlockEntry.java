package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.Block;
import com.lvc.syndichand.webservice.WebFacade;

/**
 * Created by administrator on 11/22/15.
 */
public class BlockEntry extends SyndicHandActivity {

    private EditText editTextBLockName = null;
    private Block block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_entry);
        prepareActionBarToBack("");

        editTextBLockName = (EditText) findViewById(R.id.edit_text_block_name);
        loadBlockOrCreateNewOne();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                String blockName = editTextBLockName.getText().toString();
                block.setName(blockName);
                saveBlock();
            }
        });

    }

    private void saveBlock() {
        block.save();
        WebFacade.saveOrUpdateData(block, new WebFacade.WebCallback() {

            @Override
            public void onWorkDone(String webId, Exception e) {
                if (e == null) {
                    block.setParseID(webId);
                    block.save();

                    dismissProgressDialog();
                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putSerializable(Block.class.getSimpleName(), block);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    block.delete();

                    dismissProgressDialog();
                    Toast.makeText(BlockEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void loadBlockOrCreateNewOne() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            block = (Block) bundle.getSerializable(Block.class.getSimpleName());
            editTextBLockName.setText(block.getName());
        } else {
            block = new Block();
        }

        String condominium = CondominiumDAO.retrieveCondominiumIdentifier();
        block.setCondominiumID(condominium);
    }

}
