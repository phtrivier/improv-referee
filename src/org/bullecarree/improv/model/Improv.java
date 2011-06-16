package org.bullecarree.improv.model;

public class Improv {
    private ImprovType type;
    private String title;
    private int playerCount;
    private int duration;
    private String category;
    public ImprovType getType() {
        return type;
    }
    public void setType(ImprovType type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getPlayerCount() {
        return playerCount;
    }
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
    /**
     * Duration of the improv, in seconds.
     * @return
     */
    public int getDuration() {
        return duration;
    }
    /**
     * Duration of the improv, in seconds.
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    
    
    
}
