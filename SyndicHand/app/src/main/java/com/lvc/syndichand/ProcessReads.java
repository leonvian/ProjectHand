package com.lvc.syndichand;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.database.RegisterDAO;
import com.lvc.syndichand.model.Condominium;
import com.lvc.syndichand.model.PendingRegisterData;
import com.lvc.syndichand.model.Register;
import com.lvc.syndichand.model.Unity;
import com.lvc.syndichand.utils.DialogBuilder;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by administrator on 1/4/16.
 * com.lvc.syndichand.ProcessReads
 */
public class ProcessReads extends SyndicHandActivity implements OnDataSelected {


    private static final String SPACE_USER = " ************************* ";
    private static final String SPACE = " ";
    private static final String BROKE_LINE = "\n";
    private static final String FUNCTION_PROCESS_CONDOMINIUM = "processConsumeRegisters";

    private HashMap<Unity, List<Register>> hashMapUnityRegister = new HashMap<Unity, List<Register>>();

    private static final int RELOAD_LIST_CODE = 10;
    private List<PendingRegisterData> registers = new ArrayList<PendingRegisterData>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MMM");

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

        WebFacade.retrieveListOfUnitys(new WebFacade.QueryWebCallback<Unity>() {
            @Override
            public void onQueryResult(List<Unity> data, Exception e) {
                if (e == null) {
                    loadUserHash(data);
                    loadRegister();
                } else {
                    showMessageToast(getString(R.string.fail_to_retrieve_data));
                }
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //processAllRegisters();
                sendingEmailByIntent();
            }
        });
    }


    private void loadUserHash(List<Unity> data) {
        for (Unity unity : data) {
            hashMapUnityRegister.put(unity, new ArrayList<Register>());
        }
    }

    private void loadRegister() {
        WebFacade.retrieveListOfRegisterNotProcessed(new WebFacade.QueryWebCallback<Register>() {
            @Override
            public void onQueryResult(List<Register> data, Exception e) {
                if (e == null) {
                    if (data.isEmpty()) {
                        findViewById(R.id.view_no_register_found).setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                    } else {
                        putRegisterOnHash(data);
                        registers.addAll(generatePendingRegistersData());

                    }

                } else {
                    putRegisterOnHash(RegisterDAO.retrieveAllNotProcessed());
                    registers.addAll(generatePendingRegistersData());
                }

                dismissProgressDialog();
                adapter.notifyDataSetChanged();
            }
        });
    }


    private List<PendingRegisterData> generatePendingRegistersData() {
        List<PendingRegisterData> list = new ArrayList<PendingRegisterData>();
        Set<Unity> setUnity = hashMapUnityRegister.keySet();
        for (Unity unity : setUnity) {
            List<Register> registersUser = hashMapUnityRegister.get(unity);

            if (!registersUser.isEmpty()) {

                HashMap<String, List<Register>> hashMapDataRegister = new HashMap<String, List<Register>>();

                for(Register register : registersUser) {
                    String dateStr = simpleDateFormat.format(register.getDate());
                    List<Register> registersByDate = hashMapDataRegister.get(dateStr);
                    if(registersByDate == null) {
                        registersByDate = new ArrayList<Register>();
                    }

                    registersByDate.add(register);
                    hashMapDataRegister.put(dateStr,registersByDate);
                }



                PendingRegisterData pendingRegisterData = new PendingRegisterData(unity, hashMapDataRegister);
                list.add(pendingRegisterData);
            }


        }

        return list;
    }

    private void putRegisterOnHash(List<Register> registers) {
        for (Register register : registers) {
            String idUnity = register.getIdUnity();
            Unity unity = getUnityById(idUnity);

            List<Register> registersUser = hashMapUnityRegister.get(unity);
            registersUser.add(register);

            hashMapUnityRegister.put(unity, registersUser);
        }

    }

    private Unity getUnityById(String idUnity) {
        Set<Unity> setUnity = hashMapUnityRegister.keySet();
        for (Unity unity : setUnity) {
            if (unity.getParseIdentifier().equals(idUnity)) {
                return unity;
            }
        }

        return null;
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


    private void sendingEmailByIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "casasanta.dev@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Registros de Consumo");
        String body = createDataToSendByEmail();
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private String createDataToSendByEmail() {
        Set<Unity> setUnity = hashMapUnityRegister.keySet();
        StringBuilder stringBuilder = new StringBuilder();

        for (Unity unity : setUnity) {
            List<Register> registers = hashMapUnityRegister.get(unity);

            if (!registers.isEmpty()) {

                stringBuilder.append(unity.getResponsableName());
                stringBuilder.append(BROKE_LINE);
                stringBuilder.append(unity.getApartamentNumber());
                stringBuilder.append(BROKE_LINE);

                HashMap<String, List<Register>> hashMap =   generateHashmapConsumeByDate(registers);
                Set<String> dates = hashMap.keySet();
                for(String date : dates) {
                    stringBuilder.append(BROKE_LINE);
                    stringBuilder.append(date);
                    stringBuilder.append(BROKE_LINE);
                    stringBuilder.append(BROKE_LINE);
                    List<Register> registersByDate = hashMap.get(date);

                    for (Register register : registersByDate) {
                        stringBuilder.append(typeToString(register.getType()));
                        stringBuilder.append(SPACE);
                        stringBuilder.append(register.getCurrentConsume());
                        stringBuilder.append(BROKE_LINE);
                    }

                }

                stringBuilder.append(BROKE_LINE);
                stringBuilder.append(BROKE_LINE);
                stringBuilder.append(SPACE_USER);
                stringBuilder.append(BROKE_LINE);
                stringBuilder.append(BROKE_LINE);
            }
        }

        return stringBuilder.toString();
    }


    private HashMap<String, List<Register>> generateHashmapConsumeByDate(List<Register> registers) {
        HashMap<String, List<Register>> hashMap = new HashMap<String, List<Register>>();
        for (Register register : registers) {
            String dateStr = simpleDateFormat.format(register.getDate());

            List<Register> registersDate = hashMap.get(dateStr);
            if (registersDate == null) {
                registersDate = new ArrayList<Register>();
            }

            registersDate.add(register);
            hashMap.put(dateStr, registersDate);
        }

        return hashMap;
    }


    private String typeToString(int type) {
        switch (type) {
            case Register.REGISTER_COLD_WATER:
                return getString(R.string.cold_water);

            case Register.REGISTER_HOT_WATER:
                return getString(R.string.hot_water);

            case Register.REGISTER_GAS:
                return getString(R.string.gas);

        }

        return null;
    }

}
