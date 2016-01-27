package com.lvc.syndichand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvc.syndichand.model.PendingRegisterData;
import com.lvc.syndichand.model.Register;
import com.lvc.syndichand.model.Unity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 1/12/16.
 */
public class PendingRegisterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MMM");

    private List<PendingRegisterData> registers;
    private Context context;
    private OnDataSelected onDataSelected;

    public PendingRegisterAdapter(Context context, OnDataSelected onDataSelected, List<PendingRegisterData> registers) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.registers = registers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.register_pending_item, parent, false);
            vh = new ViewHolderDefault(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header, parent, false);
            vh = new ViewHolderHeader(view);
        }


        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;
            viewHolderHeader.textViewHeader.setText(R.string.registers_to_be_processed);
        } else {
            ViewHolderDefault viewHolderDefault = (ViewHolderDefault) holder;

            PendingRegisterData register = getItem(position);

            Unity unity = register.getUnity();
            viewHolderDefault.textViewUnity.setText(unity.getResponsableName());
            viewHolderDefault.textViewUnityNumber.setText(unity.getApartamentNumber());

            String body = register.generateRegistersOrdenateByDate(context);

            viewHolderDefault.textViewRegisters.setText(Html.fromHtml(body));
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private PendingRegisterData getItem(int position) {
        return registers.get(position - 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return registers.size() + 1;
    }


    public class ViewHolderDefault extends RecyclerView.ViewHolder {


        public TextView textViewUnity;
        public TextView textViewUnityNumber;

        public TextView textViewRegisters;

        public ViewHolderDefault(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(v, getAdapterPosition());
                }
            });


            textViewUnityNumber = (TextView) view.findViewById(R.id.text_view_unity_number);
            textViewUnity = (TextView) view.findViewById(R.id.text_view_unity);

            textViewRegisters = (TextView) view.findViewById(R.id.text_view_registers);

        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView textViewHeader;

        public ViewHolderHeader(View view) {
            super(view);
            textViewHeader = (TextView) view.findViewById(R.id.text_view_header);
        }
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if (onDataSelected != null) {
            onDataSelected.onDataSelected(view, position);
        }
    }



    private String typeToString(int type) {
        switch (type) {
            case Register.REGISTER_COLD_WATER:
                return context.getString(R.string.cold_water);

            case Register.REGISTER_HOT_WATER:
                return context.getString(R.string.hot_water);

            case Register.REGISTER_GAS:
                return context.getString(R.string.gas);

        }

        return null;
    }

}