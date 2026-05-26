
package com.github.mcshanshuo.qimimaid.listeners;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class VoidListener implements Listener {

    private final QimiMaid plugin;

    public VoidListener(QimiMaid plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Item item) {
            plugin.getRecycleManager().addItem(item, "void");
        }
    }
}
