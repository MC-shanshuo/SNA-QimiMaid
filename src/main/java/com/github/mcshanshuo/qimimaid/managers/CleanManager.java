
package com.github.mcshanshuo.qimimaid.managers;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CleanManager {

    private final QimiMaid plugin;
    private BukkitRunnable cleanTask;
    private BukkitRunnable warningTask;
    private boolean warningSent = false;

    public CleanManager(QimiMaid plugin) {
        this.plugin = plugin;
        startTasks();
    }

    private void startTasks() {
        if (!plugin.getConfigManager().isCleanEnabled()) {
            return;
        }

        int interval = plugin.getConfigManager().getCleanInterval() * 20;
        int warningTime = plugin.getConfigManager().getWarningTime() * 20;

        cleanTask = new BukkitRunnable() {
            @Override
            public void run() {
                performClean();
                warningSent = false;
            }
        };

        warningTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!warningSent && plugin.getConfigManager().isCleanEnabled()) {
                    sendWarning();
                    warningSent = true;
                }
            }
        };

        cleanTask.runTaskTimer(plugin, interval, interval);
        warningTask.runTaskTimer(plugin, interval - warningTime, interval);
    }

    private void sendWarning() {
        int warningTime = plugin.getConfigManager().getWarningTime();
        String message = plugin.getMessageManager().get("clean.warning").replace("%time%", String.valueOf(warningTime));
        Bukkit.broadcastMessage(message);
    }

    private void performClean() {
        int itemsCleaned = 0;
        int mobsCleaned = 0;

        List<String> mobTypes = plugin.getConfigManager().getMobTypes();
        Set<EntityType> allowedMobTypes = new HashSet<>();
        
        for (String type : mobTypes) {
            try {
                EntityType entityType = EntityType.valueOf(type.toUpperCase());
                allowedMobTypes.add(entityType);
            } catch (IllegalArgumentException ignored) {
            }
        }

        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item item) {
                    if (!item.isDead()) {
                        plugin.getRecycleManager().addItem(item, "normal");
                        item.remove();
                        itemsCleaned++;
                    }
                } else if (plugin.getConfigManager().isMobCleanEnabled() && entity instanceof Monster) {
                    if (allowedMobTypes.contains(entity.getType())) {
                        entity.remove();
                        mobsCleaned++;
                    }
                }
            }
        }

        String message = plugin.getMessageManager().get("clean.cleaned")
                .replace("%items%", String.valueOf(itemsCleaned))
                .replace("%mobs%", String.valueOf(mobsCleaned));
        Bukkit.broadcastMessage(message);
    }

    public void stopTasks() {
        if (cleanTask != null) {
            cleanTask.cancel();
        }
        if (warningTask != null) {
            warningTask.cancel();
        }
    }

    public void triggerClean() {
        performClean();
    }
}
