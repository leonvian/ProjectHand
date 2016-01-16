package com.lvc.syndichand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvc.syndichand.model.NoticeBoard;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder> {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private List<NoticeBoard> noticeBoards;
    private Context context;
    private OnDataSelected  onDataSelected;

    public NoticeBoardAdapter(Context context, OnDataSelected onDataSelected, List<NoticeBoard> noticeBoards) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.noticeBoards = noticeBoards;
    }

    @Override
    public NoticeBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_board_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NoticeBoard noticeBoard = noticeBoards.get(position);

        String createdAtFormatted = simpleDateFormat.format(noticeBoard.getCreatedAt());

        holder.textViewSignedBy.setText(noticeBoard.getSignedBy());
        holder.textViewCreatedAt.setText(createdAtFormatted);
        holder.textViewTitle.setText(noticeBoard.getTitle());
        holder.textViewMessage.setText(noticeBoard.getMessage());
    }

    @Override
    public int getItemCount() {
        return noticeBoards.size();
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(onDataSelected != null) {
            onDataSelected.onDataSelected(view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewSignedBy;
        public TextView textViewCreatedAt;
        public TextView textViewMessage;
        public TextView textViewTitle;

        public ViewHolder(View view) {

            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(v, getAdapterPosition());
                }
            });

            textViewSignedBy = (TextView) view.findViewById(R.id.text_view_signed_by);
            textViewCreatedAt = (TextView) view.findViewById(R.id.text_view_created_at);
            textViewMessage = (TextView) view.findViewById(R.id.text_view_message);
            textViewTitle = (TextView) view.findViewById(R.id.text_view_title);
        }
    }
}