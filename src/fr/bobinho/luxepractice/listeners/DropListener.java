package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

import java.util.concurrent.TimeUnit;

public class DropListener implements Listener {

    /**
     * Listen when an item is dropped
     *
     * @param e the entity drop item event
     */
    public void onDrop(EntityDropItemEvent e) {

        //Sets pickup delay to 3 seconds
        e.getItemDrop().setPickupDelay(60);
    }

}
