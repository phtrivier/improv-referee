package org.bullecarree.improv.model;

public class ImprovRenderer {
    private Improv improv;
    
    public ImprovRenderer(Improv improv) {
        this.improv = improv;
    }
    
    public String getTitle() {
        return improv.getTitle();
    }
    
    public String getType() {
        // FIXME(pht) use Resources
        if (improv.getType() == ImprovType.COMPARED) {
            return "Comparée";
        } else {
            return "Mixte";
        }
    }
    
    public String getCategory() {
        return improv.getCategory();
    }
    
    public String getPlayerCount() {
        return String.valueOf(improv.getPlayerCount());
    }
    
    public String getDuration() {
        StringBuffer res = new StringBuffer();
        int minutes = improv.getDuration() / 60;
        int seconds = improv.getDuration() % 60;
        
        if (minutes > 0) {
            res.append(minutes).append("m ");
        }
        res.append(seconds).append("s");
        return res.toString();
    }
}
