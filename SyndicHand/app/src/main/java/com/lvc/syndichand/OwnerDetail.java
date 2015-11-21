package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvc.syndichand.model.Unity;

public class OwnerDetail extends SyndicHandActivity {

    /*
    Adicionar aqui opção de cadastrar Moradores e Veiculos (Dweller and Vehicles)
     */

    private Unity owner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_detail);
        prepareActionBarToBack("");

        owner = (Unity)getIntent().getExtras().getSerializable(Unity.class.getSimpleName());

        TextView textViewName = (TextView) findViewById(R.id.text_view_name);
        textViewName.setText(owner.getResponsableName());

        TextView textViewApartmentNumber = (TextView) findViewById(R.id.text_view_apartament_number);
        textViewApartmentNumber.setText(owner.getApartamentNumber());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerDetail.this, OwnerEntry.class);
                Bundle bundle = createBundleWithUnity();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.read_status_indicator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerDetail.this, OwnerRead.class);
                Bundle bundle = createBundleWithUnity();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        findViewById(R.id.dwellers_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para cadastro de moradores.
            }
        });

        findViewById(R.id.item_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        findViewById(R.id.item_historic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private Bundle createBundleWithUnity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Unity.class.getSimpleName(), owner);

        return bundle;
    }
}
