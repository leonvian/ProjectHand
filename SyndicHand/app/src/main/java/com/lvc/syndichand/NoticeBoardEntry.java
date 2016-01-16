package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.model.NoticeBoard;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.ParseUser;

import java.util.Date;

public class NoticeBoardEntry extends SyndicHandActivity {

    private EditText editTextMessage;
    private EditText editTextTitle;

    private NoticeBoard noticeBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board_entry);
        prepareActionBarToBack(getString(R.string.notice_board));

        editTextMessage = (EditText) findViewById(R.id.edit_text_message);
        editTextTitle = (EditText) findViewById(R.id.edit_text_title);

        loadNoticeOrCreateNew();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isObrigatoryFieldsOk(editTextMessage, editTextTitle)) {
                    return;
                }

                showProgressDialog();

                String message = editTextMessage.getText().toString();
                String title = editTextTitle.getText().toString();

                String signedBy = ParseUser.getCurrentUser().getUsername();
                Date createdAt = new Date();
                boolean isRead = false;

                noticeBoard.loadData(signedBy, createdAt, message, title, isRead);
                noticeBoard.save();
                saveOnline(noticeBoard);
            }
        });
    }

    private void loadNoticeOrCreateNew() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            noticeBoard = (NoticeBoard) extras.getSerializable(NoticeBoard.class.getSimpleName());
            loadFieldsByData(noticeBoard);
        } else {
            noticeBoard = new NoticeBoard();
            String condominiumId = CondominiumDAO.retrieveCondominiumIdentifier();
            noticeBoard.setCondominiumParseIdentifier(condominiumId);
        }
    }

    private void loadFieldsByData(NoticeBoard noticeBoard) {
        editTextMessage.setText(noticeBoard.getMessage());
        editTextTitle.setText(noticeBoard.getTitle());
    }

    private void saveOnline(final NoticeBoard noticeBoard) {

        WebFacade.saveOrUpdateData(noticeBoard, new WebFacade.WebCallback() {

            @Override
            public void onWorkDone(String webId, Exception e) {

                if (e == null) {

                    noticeBoard.setParseIdentifier(webId);
                    noticeBoard.save();

                    dismissProgressDialog();
                    Toast.makeText(NoticeBoardEntry.this, R.string.notice_board_save_sucesss, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putSerializable(NoticeBoard.class.getSimpleName(), noticeBoard);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    noticeBoard.delete();
                    Toast.makeText(NoticeBoardEntry.this, R.string.fail_save_online, Toast.LENGTH_LONG).show();
                    dismissProgressDialog();
                }
            }
        });
    }
}