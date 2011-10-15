package org.bullecarree.improv.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bullecarree.improv.model.Improv;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ImprovFileReader {

    private static final String CSV_FILE = "/Android/improv/improvs.csv";

    BufferedReader bufferedReader;

    int currentImprovIndex = 0;
    
    ImprovReader reader = new ImprovReader();

    private static Improv FALLBACK_IMPROV = new Improv();
    static {
        FALLBACK_IMPROV.setTitle("C'est deux pizza qui sont dans un four");
        FALLBACK_IMPROV.setDuration(60 * 3);
    }

    List<Improv> improvs = new ArrayList<Improv>();

    /**
     * The improv index to store in order to reopen 
     * the same value later.
     * 
     * @return
     */
    public int getImprovIndexToStore() {
        return currentImprovIndex;
    }
    
    public void readImprovs() throws IOException {

        // Open a well-known file in a well-known dir (should be in an init
        // method, of course)
        // Parse it as a CSV file. (Keep whatever is needed in memory)
        // Keep index of the read line

        String state = Environment.getExternalStorageState();
        Log.d("improv", "External storage state " + state);
        
        Log.d("improv", "External storage dir : " + Environment.getExternalStorageDirectory());
        
        File f = new File(Environment.getExternalStorageDirectory(), CSV_FILE);
        if (f == null || !f.exists()) {
            Log.e("improv", "file does not exists, looked for " + CSV_FILE + " in " + Environment.getExternalStorageDirectory());
            throw new RuntimeException("File " + CSV_FILE + " does not exists");
        }

        FileReader fr = new FileReader(f);
        bufferedReader = new BufferedReader(fr);
        // Skip first line
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw e;
        }

        // This is much uglier than I would like, but
        // I guess it will do.
        // TODO(pht) improve error handling. 
        // For the moment I'll just ditch improvs whenever I can't read something...
        while (line != null) {
            Improv res = null;
            try {
                line = bufferedReader.readLine();
                if (line != null) {
                    res = reader.readLine(line);
                    if (res == null) {
                        improvs.add(FALLBACK_IMPROV);
                    } else {
                        improvs.add(res);
                    }
                }
            } catch (IOException e) {
                res = FALLBACK_IMPROV;
                improvs.add(res);
            }
        }

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

        Log.d("improv", "Getting next improv, line index after " + currentImprovIndex);
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
        
        Log.d("improv", "Getting previous improv, line index after " + currentImprovIndex);
        return res;
    }

    /**
     * Force the reader to give a specific improv.
     * @param currentImprovIndex
     */
    public void setCurrentImprovIndex(int currentImprovIndex) {
        this.currentImprovIndex = currentImprovIndex;
    }
    
    public Improv getImprov(int improvIndex) {
        return improvs.get(improvIndex);
    }

}
