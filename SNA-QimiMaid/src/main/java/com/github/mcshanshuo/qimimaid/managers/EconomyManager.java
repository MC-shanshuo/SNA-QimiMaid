
package com.github.mcshanshuo.qimimaid.managers;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private final QimiMaid plugin;
    private Economy economy;

    public EconomyManager(QimiMaid plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }

    public boolean hasEnough(OfflinePlayer player, double amount) {
        if (economy == null) {
            return false;
        }
        return economy.has(player, amount);
    }

    public boolean withdraw(OfflinePlayer player, double amount) {
        if (economy == null) {
            return false;
        }
        if (!economy.has(player, amount)) {
            return false;
        }
        economy.withdrawPlayer(player, amount);
        return true;
    }

    public double getBalance(OfflinePlayer player) {
        if (economy == null) {
            return 0;
        }
        return economy.getBalance(player);
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isEnabled() {
        return economy != null;
    }
}
