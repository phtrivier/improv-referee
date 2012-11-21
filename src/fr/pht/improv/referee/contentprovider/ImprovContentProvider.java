package fr.pht.improv.referee.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import fr.pht.improv.db.ImprovDbHelper;
import fr.pht.improv.db.ImprovDbTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ImprovContentProvider extends ContentProvider {

    // Used for the UriMacher
    private static final int IMPROVS = 10;
    private static final int IMPROV_ID = 20;

    private static final String AUTHORITY = "fr.pht.improvs.contentprovider";

    private static final String BASE_PATH = "improvs";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/improvs";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/improv";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, IMPROVS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", IMPROV_ID);
    }

    
    private ImprovDbHelper improvDb;

    @Override
    public boolean onCreate() {
        improvDb = new ImprovDbHelper(getContext());
        return false; // Why should it return false ? 
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(ImprovDbTable.TABLE_IMPROV);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case IMPROVS:
            break;
        case IMPROV_ID:
            // Adding the ID to the original query
            queryBuilder.appendWhere(ImprovDbTable.COL_ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = improvDb.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
        
        
    }
    
    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }
    

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = improvDb.getWritableDatabase();
        long id = 0;
        switch (uriType) {
        case IMPROVS:
            id = sqlDB.insert(ImprovDbTable.TABLE_IMPROV, null, values);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = improvDb.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
        case IMPROVS:
            rowsDeleted = sqlDB.delete(ImprovDbTable.TABLE_IMPROV, selection,
                    selectionArgs);
            break;
        case IMPROV_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsDeleted = sqlDB.delete(
                        ImprovDbTable.TABLE_IMPROV,
                        ImprovDbTable.COL_ID + "=" + id, 
                        null);
            } else {
                rowsDeleted = sqlDB.delete(
                        ImprovDbTable.TABLE_IMPROV,
                        ImprovDbTable.COL_ID + "=" + id 
                        + " and " + selection,
                        selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    
    
    private void checkColumns(String[] projection) {
        String[] available = { 
                ImprovDbTable.COL_ID, // Really ? In the list of available ? 
                ImprovDbTable.COL_CATEGORY,
                ImprovDbTable.COL_DURATION,
                ImprovDbTable.COL_PLAYER,
                ImprovDbTable.COL_TITLE,
                ImprovDbTable.COL_TYPE
                
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = improvDb.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
        case IMPROVS:
            rowsUpdated = sqlDB.update(ImprovDbTable.TABLE_IMPROV, 
                    values, 
                    selection,
                    selectionArgs);
            break;
        case IMPROV_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsUpdated = sqlDB.update(ImprovDbTable.TABLE_IMPROV, 
                        values,
                        ImprovDbTable.COL_ID + "=" + id, 
                        null);
            } else {
                rowsUpdated = sqlDB.update(ImprovDbTable.TABLE_IMPROV, 
                        values,
                        ImprovDbTable.COL_ID + "=" + id 
                        + " and " 
                        + selection,
                        selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
