package com.lvc.syndichand;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.database.RegisterDAO;
import com.lvc.syndichand.model.Condominium;
import com.lvc.syndichand.model.Register;
import com.lvc.syndichand.utils.DialogBuilder;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by administrator on 1/4/16.
 * com.lvc.syndichand.ProcessReads
 */
public class ProcessReads extends SyndicHandActivity implements OnDataSelected {


    private static final String FUNCTION_PROCESS_CONDOMINIUM = "processConsumeRegisters";

    private static final int RELOAD_LIST_CODE = 10;
    private List<Register> registers = new ArrayList<Register>();

    private PendingRegisterAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_reads);
        prepareActionBarToBack(getString(R.string.process_consume_register));

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        adapter = new PendingRegisterAdapter(ProcessReads.this, ProcessReads.this, registers);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showProgressDialog();
        WebFacade.retrieveListOfRegisterNotProcessed(new WebFacade.QueryWebCallback<Register>() {
            @Override
            public void onQueryResult(List<Register> data, Exception e) {
                if (e == null) {
                    if(data.isEmpty()) {
                        findViewById(R.id.view_no_register_found).setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                    } else {
                        registers.addAll(data);
                    }

                } else {
                    registers.addAll(RegisterDAO.retrieveAllNotProcessed());
                }

                dismissProgressDialog();
                adapter.notifyDataSetChanged();
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                processAllRegisters();
            }
        });
    }

    private void processAllRegisters() {

        DialogBuilder.showDialogPositiveNegative(ProcessReads.this, R.string.app_name, R.string.confirm_send_reads, new DialogBuilder.ButtonCallback() {
            @Override
            protected void onPositive(AlertDialog.Builder builder, DialogInterface dialogInterface) {
                super.onPositive(builder, dialogInterface);
                processAllRegisters();

                HashMap<String, String> hashMap = new HashMap<String, String>();
                Condominium condominium = CondominiumDAO.retrieveCondominium();
                String condominiumParseId = condominium.getParseUniqueID();
                hashMap.put("condominium", condominiumParseId);
                ParseCloud.callFunctionInBackground(FUNCTION_PROCESS_CONDOMINIUM, hashMap, new FunctionCallback<Integer>() {
                    @Override
                    public void done(Integer response, ParseException e) {
                        if (response == -1) {
                            Toast.makeText(ProcessReads.this, R.string.fail_to_process_intern, Toast.LENGTH_LONG).show();
                        } else if (e != null) {
                            Toast.makeText(ProcessReads.this, R.string.fail_to_process_register_on_web, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProcessReads.this, R.string.registers_processed_sucess, Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }

            @Override
            protected void onNegative(AlertDialog.Builder builder, DialogInterface dialogInterface) {
                super.onNegative(builder, dialogInterface);
                dialogInterface.dismiss();
            }
        });

    }

    @Override
    public void onDataSelected(View view, int position) {


    }



}
