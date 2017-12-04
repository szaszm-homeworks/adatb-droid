package tk.szaszm.adatb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marci on 2017.03.31..
 */
public class EventDispatcher {
    private Map<Class, ArrayList> listeners;

    public EventDispatcher() {
        listeners = new HashMap<>();
    }
    private static EventDispatcher instance = null;
    public static EventDispatcher getInstance() {
        if(instance == null) instance = new EventDispatcher();
        return instance;
    }


    public <E> void listen(Class<E> eventType, EventListener<E> listener) {
        if(!listeners.containsKey(eventType)) {
            ArrayList<EventListener<E>> al = new ArrayList<>();
            al.add(listener);
            listeners.put(eventType, al);
        } else {
            listeners.get(eventType).add(listener);
        }
    }

    public <E> void dispatch(E event) {
        Class<E> eClass = (Class<E>) event.getClass();
        ArrayList<EventListener<E>> listeners = (ArrayList<EventListener<E>>)this.listeners.get(eClass);
        if(listeners != null) {
            for (EventListener<E> listener : listeners) {
                listener.handleEvent(event);
            }
        }
    }

    public <E> void removeListener(Class<E> eventType, EventListener<E> listener) {
        if(!listeners.containsKey(eventType)) {
            return;
        }

        ArrayList<EventListener<E>> listeners = (ArrayList<EventListener<E>>)this.listeners.get(eventType);
        listeners.remove(listener);
    }
}
