package fr.pht.improv.referee;

public interface ProgressListener {
    public void onTick(int progress, long durationInMillis);
}
