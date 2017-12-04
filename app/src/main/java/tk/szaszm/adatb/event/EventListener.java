package tk.szaszm.adatb.event;

/**
 * Created by marci on 2017.03.31..
 */
public interface EventListener<E> {
    void handleEvent(E event);
}
