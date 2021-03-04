package ru.test.pbr;

public class Timer {

    private long lastLoopTime;
    
    public Timer() {
        lastLoopTime = getTime();
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public int getElapsedTime() {
        long time = getTime();
        int elapsedTime = (int) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public long getLastLoopTime() {
        return lastLoopTime;
    }
}