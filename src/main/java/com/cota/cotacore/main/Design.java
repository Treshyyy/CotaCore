package com.cota.cotacore.main;

import com.cota.cotacore.CotaCore;
import org.bukkit.Bukkit;

public class Design {




    private static final String[] colors = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};
    public static void showStartupMessage() {
        Bukkit.getLogger().info("§7---------------");
        Bukkit.getLogger().info("§7|  §b"+CotaCore.INSTANCE.getPluginName()+"§7     |");
        Bukkit.getLogger().info("§7|  §a+enabled+§7  |");
        Bukkit.getLogger().info("§7---------------");
    }

    public static void showShutdownMessage() {
        Bukkit.getLogger().info("§7---------------");
        Bukkit.getLogger().info("§7|  §b"+CotaCore.INSTANCE.getPluginName()+"§7     |");
        Bukkit.getLogger().info("§7|  §c-disabled-§7 |");
        Bukkit.getLogger().info("§7---------------");
    }

}
