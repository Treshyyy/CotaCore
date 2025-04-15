package com.cota.cotacore.main.files.data.config;


import com.cota.cotacore.CotaCore;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigUtils {



    public static FileConfiguration getConfig() {
        return CotaCore.INSTANCE.getConfig();
    }

    public static String getStringSection(String section) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(section));
    }

    public static String getStringSectionPrefix(String section) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix") + getConfig().getString(section));
    }

    public static String getMessagesSection(String section) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + section));
    }

    public static String getMessagesSectionPrefix(String section) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix") + getConfig().getString("messages." + section));
    }

    public static int getIntegerSection(String section) {
        return getConfig().getInt(section);
    }

    public static double getDoubleSection(String section) {
        return getConfig().getDouble(section);
    }

    public static boolean getBooleanSection(String section) {
        return getConfig().getBoolean(section);
    }

    public static List<String> getStringArraySection(String section) {
        List<String> list = new ArrayList<>();
        for (String s : getConfig().getStringList(section)) {
            list.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return list;
    }


    public static List<Double> getDoubleArraySection(String section) {
        List<Double> list = new ArrayList<>();
        list.addAll(getConfig().getDoubleList(section));
        return list;
    }




}
