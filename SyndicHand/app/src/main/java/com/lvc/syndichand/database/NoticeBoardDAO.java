package com.lvc.syndichand.database;

import com.activeandroid.query.Select;
import com.lvc.syndichand.model.NoticeBoard;

import java.util.List;

public class NoticeBoardDAO {

    public static List<NoticeBoard> retrieveAll() {
        List<NoticeBoard> noticeBoards = new Select().from(NoticeBoard.class).execute();
        return noticeBoards;
    }
}