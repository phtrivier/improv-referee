package org.bullecarree.improv.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bullecarree.improv.db.ImprovDbTable;
import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.referee.contentprovider.ImprovContentProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ImprovDatabaseReader {

    BufferedReader bufferedReader;

    int currentImprovIndex = 0;

    ImprovLineReader reader = new ImprovLineReader();

    private static Improv FALLBACK_IMPROV = new Improv();
    static {
        FALLBACK_IMPROV.setTitle("C'est deux pizza qui sont dans un four");
        FALLBACK_IMPROV.setDuration(60 * 3);
    }

    List<Improv> improvs = new ArrayList<Improv>();

    private ContentResolver contentResolver;

    public ImprovDatabaseReader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public ContentResolver getContentResolver() {
        return contentResolver;
    }

    /**
     * The improv index to store in order to reopen the same value later.
     * 
     * @return
     */
    public int getImprovIndexToStore() {
        return currentImprovIndex;
    }

    public void readImprovs() throws IOException {

        String[] projection = { ImprovDbTable.COL_TITLE,
                ImprovDbTable.COL_CATEGORY, ImprovDbTable.COL_TYPE,
                ImprovDbTable.COL_DURATION, ImprovDbTable.COL_PLAYER };
        Cursor cursor = getContentResolver().query(ImprovContentProvider.CONTENT_URI, projection,
                null, null, null);

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow(ImprovDbTable.COL_TYPE));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(ImprovDbTable.COL_TITLE));
                String duration = cursor.getString(cursor
                        .getColumnIndexOrThrow(ImprovDbTable.COL_DURATION));
                String player = cursor.getString(cursor
                        .getColumnIndexOrThrow(ImprovDbTable.COL_PLAYER));
                String category = cursor.getString(cursor
                        .getColumnIndexOrThrow(ImprovDbTable.COL_CATEGORY));

                Improv improv = new Improv();
                if (type.equalsIgnoreCase(ImprovType.MIXT.toString())) {
                    improv.setType(ImprovType.MIXT);
                } else {
                    improv.setType(ImprovType.COMPARED);
                }

                improv.setTitle(title);
                improv.setCategory(category);

                try {
                    int durationS = Integer.parseInt(duration);
                    improv.setDuration(durationS);
                } catch (NumberFormatException e) {
                    improv.setDuration(60 * 3);
                }

                try {
                    int playerCount = Integer.parseInt(player);
                    improv.setPlayerCount(playerCount);
                } catch (NumberFormatException e) {
                    // playerCount will be null, so infinite...
                }
                improvs.add(improv);

            }
        } else {
            improvs.add(FALLBACK_IMPROV);
        }
        //
        //
        // File f = new File(Environment.getExternalStorageDirectory(),
        // CSV_FILE);
        // if (f == null || !f.exists()) {
        // throw new RuntimeException("File " + CSV_FILE + " does not exists");
        // }
        //
        // FileReader fr = new FileReader(f);
        // bufferedReader = new BufferedReader(fr);
        // // Skip first line
        // String line = null;
        // try {
        // line = bufferedReader.readLine();
        // } catch (IOException e) {
        // throw e;
        // }
        //
        // // This is much uglier than I would like, but
        // // I guess it will do.
        // // TODO(pht) improve error handling.
        // // For the moment I'll just ditch improvs whenever I can't read
        // something...
        // while (line != null) {
        // Improv res = null;
        // try {
        // line = bufferedReader.readLine();
        // if (line != null) {
        // res = reader.readLine(line);
        // if (res == null) {
        // improvs.add(FALLBACK_IMPROV);
        // } else {
        // improvs.add(res);
        // }
        // }
        // } catch (IOException e) {
        // res = FALLBACK_IMPROV;
        // improvs.add(res);
        // }
        // }

    }

    /**
     * Read an improv.
     * 
     * @return an improv, or null if no improv is available.
     */
    public Improv nextImprov() {
        if (improvs.isEmpty()) {
            return FALLBACK_IMPROV;
        }

        currentImprovIndex = (currentImprovIndex + 1) % improvs.size();

        Improv res = improvs.get(currentImprovIndex);

        return res;
    }

    public Improv previousImprov() {
        if (improvs.isEmpty()) {
            return FALLBACK_IMPROV;
        }

        // Damn "x = (x - 1) % N" that does not work as I hope ;)
        if (currentImprovIndex == 0) {
            currentImprovIndex = improvs.size() - 1;
        } else {
            currentImprovIndex = (currentImprovIndex - 1);
        }

        Improv res = improvs.get(currentImprovIndex);

        return res;
    }

    /**
     * Force the reader to give a specific improv.
     * 
     * @param currentImprovIndex
     */
    public void setCurrentImprovIndex(int currentImprovIndex) {
        this.currentImprovIndex = currentImprovIndex;
    }

    public Improv getImprov(int improvIndex) {
        return improvs.get(improvIndex);
    }

}
