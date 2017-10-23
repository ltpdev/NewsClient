package com.gdcp.newsclient.event;

/**
 * Created by asus- on 2017/8/24.
 */

public class DayNightEvent {
    private boolean isNight;

    public DayNightEvent(boolean isNight) {
        this.isNight = isNight;
    }

    public boolean isNight() {
        return isNight;
    }
}
