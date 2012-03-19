package org.bullecarree.improv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImprovDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "improv.db";
    private static final int DB_VERSION = 1;
    
    public ImprovDbHelper(Context context) {
        // CursorFactory set to null ... ?
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ImprovDbTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ImprovDbTable.onUpgrade(db, oldVersion, newVersion);
    }
    
    
}
