package org.bullecarree.improv.reader;

import org.bullecarree.improv.model.Improv;

public class ImprovDbReader {

    public Improv getImprov(int improvIndex) {
        Improv res = new Improv();
        res.setTitle("He ho ça va hein");
        res.setDuration(60);
        return res;
    }

    public void readImprovs() {
        // TODO Auto-generated method stub
        
    }

    public void setCurrentImprovIndex(int improvIndex) {
        // TODO Auto-generated method stub
        
    }
    
}
