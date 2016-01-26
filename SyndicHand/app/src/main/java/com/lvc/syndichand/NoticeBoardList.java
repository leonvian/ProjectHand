package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.lvc.syndichand.database.NoticeBoardDAO;
import com.lvc.syndichand.model.NoticeBoard;
import com.lvc.syndichand.webservice.WebFacade;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoardList extends SyndicHandList implements OnDataSelected {

    private static final int RELOAD_LIST_CODE = 30;
    private List<NoticeBoard> noticeBoards = new ArrayList<NoticeBoard>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        prepareActionBarToBack(getString(R.string.notice_board));

        adapter = new NoticeBoardAdapter(NoticeBoardList.this, NoticeBoardList.this, noticeBoards);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoticeBoardList.this));

        showProgressDialog();

        WebFacade.retrieveListOfNoticeBoards(new WebFacade.QueryWebCallback<NoticeBoard>() {

            @Override
            public void onQueryResult(List<NoticeBoard> data, Exception e) {

                if (e == null) {
                    noticeBoards.addAll(data);
                } else {
                    noticeBoards.addAll(NoticeBoardDAO.retrieveAll());
                }

                dismissProgressDialog();
                adapter.notifyDataSetChanged();
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeBoardList.this, NoticeBoardEntry.class);
                startActivityForResult(intent, RELOAD_LIST_CODE);
            }
        });
    }


    @Override
    public void onDataSelected(View view, int position) {

        NoticeBoard noticeBoard = noticeBoards.get(position);
        Intent intent = new Intent(NoticeBoardList.this, NoticeBoardEntry.class);
        Bundle extras = new Bundle();
        extras.putSerializable(NoticeBoard.class.getSimpleName(), noticeBoard);
        intent.putExtras(extras);
        startActivityForResult(intent, RELOAD_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RELOAD_LIST_CODE && resultCode == RESULT_OK) {

            NoticeBoard noticeBoard = (NoticeBoard)data.getSerializableExtra(NoticeBoard.class.getSimpleName());

            if (noticeBoards.contains(noticeBoard)) {
                int index = noticeBoards.indexOf(noticeBoard);
                noticeBoards.set(index, noticeBoard);
            } else {
                noticeBoards.add(noticeBoard);
                adapter.notifyItemInserted(0);
            }

            adapter.notifyDataSetChanged();
        }
    }
}