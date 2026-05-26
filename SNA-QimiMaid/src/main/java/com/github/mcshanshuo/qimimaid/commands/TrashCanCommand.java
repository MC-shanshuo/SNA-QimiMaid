
package com.github.mcshanshuo.qimimaid.commands;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import com.github.mcshanshuo.qimimaid.gui.TrashCanGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCanCommand implements CommandExecutor {

    private final QimiMaid plugin;

    public TrashCanCommand(QimiMaid plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getMessageManager().sendMessage(sender, "no-player");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("qimimaid.trashcan")) {
            plugin.getMessageManager().sendMessage(player, "no-permission");
            return true;
        }

        new TrashCanGUI(plugin, player);
        plugin.getMessageManager().sendMessage(player, "trashcan.opened");

        return true;
    }
}
