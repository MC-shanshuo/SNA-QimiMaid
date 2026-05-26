
package com.github.mcshanshuo.qimimaid.listeners;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

public class ItemDespawnListener implements Listener {

    private final QimiMaid plugin;

    public ItemDespawnListener(QimiMaid plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        Item item = event.getEntity();
        plugin.getRecycleManager().addItem(item, "despawn");
    }
}
