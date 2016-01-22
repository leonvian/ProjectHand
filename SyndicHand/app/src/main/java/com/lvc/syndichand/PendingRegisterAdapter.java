package com.lvc.syndichand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvc.syndichand.model.Register;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 1/12/16.
 */
public class PendingRegisterAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MMM");

    private List<Register> registers;
    private Context context;
    private OnDataSelected  onDataSelected;

    public PendingRegisterAdapter(Context context, OnDataSelected onDataSelected, List<Register> registers) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.registers = registers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
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
        if(holder instanceof  ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;
            viewHolderHeader.textViewHeader.setText(R.string.registers_to_be_processed);
        } else {
            ViewHolderDefault viewHolderDefault = (ViewHolderDefault) holder;
            Register register = getItem(position);
            viewHolderDefault.textViewRegisterType.setText(typeToStr(register.getType()));
            viewHolderDefault.textViewRegisterValue.setText(String.valueOf(register.getCurrentConsume()));
            Date dateD = register.getDate();
            String dateFormat = simpleDateFormat.format(dateD);
            viewHolderDefault.textViewRegisterDate.setText(dateFormat);
        }

    }

    private int typeToStr(int type) {
        switch (type) {
            case Register.REGISTER_COLD_WATER:
                return R.string.cold_water;
            case Register.REGISTER_GAS:
                return R.string.gas;
            case Register.REGISTER_HOT_WATER:
                return R.string.hot_water;
        }

        throw new IllegalArgumentException("Tipo de consumo passado não encontrado: " + type);
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private Register getItem(int position) {
        return registers.get(position - 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return registers.size() +1;
    }


    public class ViewHolderDefault extends RecyclerView.ViewHolder {

        public TextView textViewRegisterDate;
        public TextView textViewRegisterType;
        public TextView textViewRegisterValue;

        public ViewHolderDefault(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(v, getAdapterPosition());
                }
            });

            //View layoutReads = view.findViewById(R.id.layout_alert_reads);
            //layoutReads.setVisibility(View.GONE);
            textViewRegisterType = (TextView)view.findViewById(R.id.text_view_register_type);
            textViewRegisterDate = (TextView)view.findViewById(R.id.text_view_register_date);
            textViewRegisterValue = (TextView)view.findViewById(R.id.text_view_register_value);
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView textViewHeader;

        public ViewHolderHeader(View view) {
            super(view);
            textViewHeader = (TextView)view.findViewById(R.id.text_view_header);
        }
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(onDataSelected != null) {
            onDataSelected.onDataSelected(view, position);
        }
    }

}