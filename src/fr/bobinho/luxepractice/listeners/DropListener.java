package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.concurrent.TimeUnit;

public class DropListener implements Listener {

    /**
     * Listen when an item is dropped
     *
     * @param e the item spawn event
     */
    @EventHandler
    public void onDrop(ItemSpawnEvent e) {

        //Clears item after 3 seconds
        PracticeScheduler.syncScheduler().after(3, TimeUnit.SECONDS).run(() -> {
            e.getEntity().remove();
        });
    }

}
