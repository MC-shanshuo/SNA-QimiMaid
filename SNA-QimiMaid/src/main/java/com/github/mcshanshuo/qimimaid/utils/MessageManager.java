
package com.github.mcshanshuo.qimimaid.utils;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final QimiMaid plugin;
    private Map<String, String> messages;

    public MessageManager(QimiMaid plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        loadMessages();
    }

    private void loadMessages() {
        messages.clear();
        
        messages.put("prefix", getMessage("prefix"));
        
        messages.put("no-permission", getMessage("no-permission"));
        messages.put("no-player", getMessage("no-player"));
        messages.put("invalid-command", getMessage("invalid-command"));
        messages.put("reload-success", getMessage("reload-success"));
        
        messages.put("clean.warning", getMessage("clean.warning"));
        messages.put("clean.cleaned", getMessage("clean.cleaned"));
        messages.put("clean.disabled", getMessage("clean.disabled"));
        
        messages.put("recycle.opened", getMessage("recycle.opened"));
        messages.put("recycle.buy-success", getMessage("recycle.buy-success"));
        messages.put("recycle.buy-failed", getMessage("recycle.buy-failed"));
        messages.put("recycle.item-given", getMessage("recycle.item-given"));
        messages.put("recycle.inventory-full", getMessage("recycle.inventory-full"));
        messages.put("recycle.empty", getMessage("recycle.empty"));
        messages.put("recycle.auto-clean", getMessage("recycle.auto-clean"));
        
        messages.put("trashcan.opened", getMessage("trashcan.opened"));
        messages.put("trashcan.cleared", getMessage("trashcan.cleared"));
        
        messages.put("gui.back", getMessage("gui.back"));
        messages.put("gui.next", getMessage("gui.next"));
        messages.put("gui.previous", getMessage("gui.previous"));
        messages.put("gui.close", getMessage("gui.close"));
        messages.put("gui.buy", getMessage("gui.buy"));
        messages.put("gui.category", getMessage("gui.category"));
        messages.put("gui.price", getMessage("gui.price"));
        messages.put("gui.origin", getMessage("gui.origin"));
        messages.put("gui.amount", getMessage("gui.amount"));
        messages.put("gui.valuable", getMessage("gui.valuable"));
    }

    private String getMessage(String path) {
        String message = plugin.getConfig().getString(path);
        if (message == null) {
            return path;
        }
        message = message.replace("%prefix%", plugin.getConfig().getString("prefix", ""));
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void reload() {
        plugin.reloadConfig();
        loadMessages();
    }

    public String get(String key) {
        return messages.getOrDefault(key, key);
    }

    public String get(String key, Map<String, String> placeholders) {
        String message = get(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

    public void sendMessage(CommandSender sender, String key) {
        sender.sendMessage(get(key));
    }

    public void sendMessage(CommandSender sender, String key, Map<String, String> placeholders) {
        sender.sendMessage(get(key, placeholders));
    }

    public Component getComponent(String key) {
        return Component.text(get(key));
    }

    public TextComponent getTextComponent(String key) {
        return Component.text(get(key));
    }

    public Component getColoredComponent(String key) {
        String text = get(key);
        return Component.text(text);
    }
}
