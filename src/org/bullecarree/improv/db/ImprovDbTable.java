package org.bullecarree.improv.db;

import java.util.Arrays;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ImprovDbTable {
    public static final String TABLE_IMPROV = "improv";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_TYPE = "type"; // Warning is it acceptable ?
    public static final String COL_DURATION = "duration";
    public static final String COL_PLAYER = "player";
    public static final String COL_CATEGORY = "category";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_IMPROV + "(" + COL_ID
            + " integer primary key autoincrement, " + COL_TITLE
            + " text not null, " + COL_TYPE + " text not null, " + COL_DURATION
            + " integer not null, " + 
            COL_CATEGORY + " text, " + COL_PLAYER + " integer)";
    
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        Log.w(ImprovDbTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_IMPROV);
        onCreate(database);
    }
    
}
