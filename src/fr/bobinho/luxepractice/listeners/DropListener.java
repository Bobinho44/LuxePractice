package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

import java.util.concurrent.TimeUnit;

public class DropListener implements Listener {

    public void onDrop(EntityDropItemEvent e) {
        e.getItemDrop().setPickupDelay(32767);
        PracticeScheduler.syncScheduler().after(3, TimeUnit.SECONDS).run(() ->
                e.getItemDrop().remove()
        );
    }
}
