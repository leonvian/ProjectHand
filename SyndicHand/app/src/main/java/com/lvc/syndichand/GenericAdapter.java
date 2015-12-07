package com.lvc.syndichand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by administrator on 10/22/15.
 */
public class GenericAdapter<T>  extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

    private List<T> data;
    private Context context;
    private OnDataSelected  onDataSelected;

    public GenericAdapter(Context context, OnDataSelected onDataSelected, List<T> data) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.data = data;
    }

    @Override
    public GenericAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_adapter_view, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenericAdapter.ViewHolder holder, int position) {
        Object genericModelViewer = data.get(position);
        String value = genericModelViewer.toString();
        String firstLetter = getFirstLetter(value);

        holder.textViewFirstLetter.setText(firstLetter);
        holder.textViewValue.setText(value);
    }

    private String getFirstLetter(String target) {
        if(target == null || target.length() < 2) {
            return "";
        }
        String firstLetter = target.substring(0,1);
        return firstLetter;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewFirstLetter;
        public TextView textViewValue;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(v, getAdapterPosition());
                }
            });

            textViewFirstLetter = (TextView)view.findViewById(R.id.text_view_first_letter);
            textViewValue = (TextView)view.findViewById(R.id.text_view_value);
        }
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(onDataSelected != null) {
            onDataSelected.onDataSelected(view, position);
        }
    }

}