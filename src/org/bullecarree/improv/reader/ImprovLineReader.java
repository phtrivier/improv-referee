package org.bullecarree.improv.reader;

import java.io.IOException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVUtils;
import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;

import android.util.Log;

public class ImprovLineReader {
    
    public boolean isBlank(String str) {
        return str == null || "".equals(str);
    }
    
    public Improv readLine(String line) {
        
        Improv res = new Improv();
        
        String[] tokens;
        try {
            // TODO(pht) some error handling ...
            tokens = CSVUtils.parseLine(line);
        
            if (!isBlank(tokens[0])) {
                if ("M".equals(tokens[0])) {
                    res.setType(ImprovType.MIXT);
                } else {
                    res.setType(ImprovType.COMPARED);
                }
            }
            
            res.setTitle(tokens[1]);
            
            res.setCategory(tokens[2]);
            
            if (!isBlank(tokens[3])) {
                res.setPlayerCount(Integer.parseInt(tokens[3]));
            }
            
            res.setDuration(Integer.parseInt(tokens[4]));
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res = null;
        }

        return res;
    }
}
