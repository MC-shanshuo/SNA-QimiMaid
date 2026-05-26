
package com.github.mcshanshuo.qimimaid.commands;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QimiMaidCommand implements CommandExecutor {

    private final QimiMaid plugin;

    public QimiMaidCommand(QimiMaid plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                sendHelp(sender);
                break;
            case "reload":
                if (!sender.hasPermission("qimimaid.admin")) {
                    plugin.getMessageManager().sendMessage(sender, "no-permission");
                    return true;
                }
                plugin.getConfigManager().reload();
                plugin.getMessageManager().reload();
                plugin.getMessageManager().sendMessage(sender, "reload-success");
                break;
            case "clean":
                if (!sender.hasPermission("qimimaid.admin")) {
                    plugin.getMessageManager().sendMessage(sender, "no-permission");
                    return true;
                }
                if (plugin.getConfigManager().isCleanEnabled()) {
                    plugin.getCleanManager().triggerClean();
                } else {
                    plugin.getMessageManager().sendMessage(sender, "clean.disabled");
                }
                break;
            case "recycle":
                if (!(sender instanceof Player)) {
                    plugin.getMessageManager().sendMessage(sender, "no-player");
                    return true;
                }
                if (!sender.hasPermission("qimimaid.recycle")) {
                    plugin.getMessageManager().sendMessage(sender, "no-permission");
                    return true;
                }
                new com.github.mcshanshuo.qimimaid.gui.RecycleGUI(plugin, (Player) sender);
                plugin.getMessageManager().sendMessage(sender, "recycle.opened");
                break;
            case "trashcan":
                if (!(sender instanceof Player)) {
                    plugin.getMessageManager().sendMessage(sender, "no-player");
                    return true;
                }
                if (!sender.hasPermission("qimimaid.trashcan")) {
                    plugin.getMessageManager().sendMessage(sender, "no-permission");
                    return true;
                }
                new com.github.mcshanshuo.qimimaid.gui.TrashCanGUI(plugin, (Player) sender);
                plugin.getMessageManager().sendMessage(sender, "trashcan.opened");
                break;
            case "version":
                sender.sendMessage("QimiMaid v" + plugin.getDescription().getVersion());
                break;
            default:
                plugin.getMessageManager().sendMessage(sender, "invalid-command");
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§b===== QimiMaid 扫地娘插件 =====");
        sender.sendMessage("§e/qm help §7- 显示此帮助信息");
        sender.sendMessage("§e/qm reload §7- 重新加载配置文件");
        sender.sendMessage("§e/qm clean §7- 立即执行清理");
        sender.sendMessage("§e/qm recycle §7- 打开回收站");
        sender.sendMessage("§e/qm trashcan §7- 打开垃圾桶");
        sender.sendMessage("§e/qm version §7- 显示版本信息");
        sender.sendMessage("§e/recycle §7- 快捷打开回收站");
        sender.sendMessage("§e/trashcan §7- 快捷打开垃圾桶");
        sender.sendMessage("");
    }
}
