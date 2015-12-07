package com.lvc.syndichand.database;

import com.activeandroid.query.Select;
import com.lvc.syndichand.model.Block;

import java.util.List;

/**
 * Created by administrator on 11/22/15.
 */
public class BlockDAO {

    public static List<Block> retrieveAll() {
        List<Block> blocks = new Select().from(Block.class).execute();

        return blocks;
    }

}
