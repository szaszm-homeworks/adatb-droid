package tk.szaszm.adatb;

import java.util.ArrayList;

import tk.szaszm.adatb.event.EventListener;

/**
 * Created by marci on 12/4/17.
 */

public class DrawerItem {
    String label;
    ArrayList<EventListener<DrawerItem>> onClickListeners = new ArrayList<>();

    public DrawerItem(String label, EventListener<DrawerItem>... listeners) {
        this.label = label;
        for (EventListener<DrawerItem> listener : listeners) {
            onClickListeners.add(listener);
        }
    }

    @Override
    public String toString() {
        return label;
    }

    public void click() {
        for (EventListener<DrawerItem> listener : onClickListeners) {
            listener.handleEvent(this);
        }
    }
}
