
package com.github.mcshanshuo.qimimaid.listeners;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class EntityDeathListener implements Listener {

    private final QimiMaid plugin;
    private final List<EntityType> MOB_TYPES = Arrays.asList(
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER,
            EntityType.ENDERMAN, EntityType.WITCH, EntityType.BLAZE, EntityType.GHAST,
            EntityType.PIGLIN, EntityType.HOGLIN, EntityType.STRIDER, EntityType.ZOMBIFIED_PIGLIN,
            EntityType.DROWNED, EntityType.HUSK, EntityType.PHANTOM, EntityType.SILVERFISH,
            EntityType.CAVE_SPIDER, EntityType.ENDERMITE, EntityType.VEX, EntityType.EVOKER,
            EntityType.VINDICATOR, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER,
            EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.WITHER_SKELETON,
            EntityType.STRAY, EntityType.ZOGLIN
    );

    public EntityDeathListener(QimiMaid plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        
        if (MOB_TYPES.contains(entity.getType())) {
            for (ItemStack drop : event.getDrops()) {
                plugin.getRecycleManager().addItemStack(drop, "mob-drop");
            }
            event.getDrops().clear();
        }
    }
}
