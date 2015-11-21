package com.lvc.syndichand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvc.syndichand.model.Unity;

import java.util.List;

/**
 * Created by administrator on 9/17/15.
 */
public class OwnerListAdapter extends RecyclerView.Adapter<OwnerListAdapter.ViewHolder> {

    private List<Unity> owners;
    private Context context;
    private OnDataSelected  onDataSelected;

    public OwnerListAdapter(Context context, OnDataSelected onDataSelected, List<Unity> owners) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.owners = owners;
    }

    @Override
    public OwnerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_adapter_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Unity owner = owners.get(position);
        holder.textViewName.setText(owner.getResponsableName());
        holder.textViewApartmentNumber.setText(owner.getApartamentNumber());

    }

    @Override
    public int getItemCount() {
        return owners.size();
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

}