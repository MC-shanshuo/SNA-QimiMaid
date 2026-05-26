
package com.github.mcshanshuo.qimimaid.listeners;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.inventory.ItemStack;

public class EntityRemoveListener implements Listener {

    private final QimiMaid plugin;

    public EntityRemoveListener(QimiMaid plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event) {
        if (!(event.getEntity() instanceof Item item)) {
            return;
        }

        Location location = item.getLocation();
        
        if (isInFire(location)) {
            plugin.getRecycleManager().addItem(item, "fire");
            return;
        }
        
        if (isInCactus(location)) {
            plugin.getRecycleManager().addItem(item, "cactus");
            return;
        }
    }

    private boolean isInFire(Location location) {
        return location.getBlock().getType() == Material.FIRE ||
               location.getBlock().getType() == Material.LAVA;
    }

    private boolean isInCactus(Location location) {
        return location.getBlock().getType() == Material.CACTUS;
    }
}
