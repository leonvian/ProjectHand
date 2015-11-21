package com.lvc.syndichand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvc.syndichand.model.CommonArea;

import java.util.List;

/**
 * Created by administrator on 10/17/15.
 */
public class CommonAreaAdapter extends RecyclerView.Adapter<CommonAreaAdapter.ViewHolder> {

    private List<CommonArea> commonAreas;
    private Context context;
    private OnDataSelected  onDataSelected;

    public CommonAreaAdapter(Context context, OnDataSelected onDataSelected, List<CommonArea> commonAreas) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.commonAreas = commonAreas;
    }

    @Override
    public CommonAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_adapter_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommonArea owner = commonAreas.get(position);
        holder.textViewName.setText(owner.getName());
        //holder.textViewApartmentNumber.setText(owner.getApartamentNumber());

    }

    @Override
    public int getItemCount() {
        return commonAreas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewApartmentNumber;
        public TextView textViewName;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(v, getAdapterPosition());
                }
            });

            View layoutReads = view.findViewById(R.id.layout_alert_reads);
            layoutReads.setVisibility(View.GONE);
            textViewName = (TextView)view.findViewById(R.id.text_view_name);
            textViewApartmentNumber = (TextView)view.findViewById(R.id.text_view_apartament_number);
        }
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(onDataSelected != null) {
            onDataSelected.onDataSelected(view, position);
        }
    }

    public static interface OnDataSelected {

        public void onDataSelected(View view, int position);

    }
}