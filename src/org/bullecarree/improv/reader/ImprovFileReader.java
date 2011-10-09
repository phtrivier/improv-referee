package org.bullecarree.improv.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ImprovFileReader {
    
    BufferedReader bufferedReader;
    
    ImprovReader reader = new ImprovReader();
    
    public ImprovFileReader(Context context) {
        // Open a well-known file in a well-known dir (should be in an init method, of course)
        // Parse it as a CSV file. (Keep whatever is needed in memory)
        // Keep index of the read line
        File f = new File(Environment.getExternalStorageDirectory(), "/improv/improvs.csv");
        if (f != null) {
            Log.d("File exsists ?", String.valueOf(f.exists()));
        }
        try {
            FileReader fr = new FileReader(f);
            bufferedReader = new BufferedReader(fr);
            // Skip first line
            bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Read an improv. 
     * @return an improv, or null if no improv is available.
     */
    public Improv readImprov() {
        Improv res = null;
        try {
            String line = bufferedReader.readLine();
            res = reader.readLine(line);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }
}
