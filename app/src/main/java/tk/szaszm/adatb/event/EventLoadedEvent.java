package tk.szaszm.adatb.event;

import tk.szaszm.adatb.model.Events;

/**
 * Created by marci on 12/4/17.
 */

public class EventLoadedEvent {
    private Events event;

    public EventLoadedEvent(Events event) { this.event = event; }

    public Events getEvent() {
        return event;
    }
}
