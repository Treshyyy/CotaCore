package com.cota.cotacore.main.files.data.files;

public class PlayerUtils {

    public static long getCooldown(PlayerData pd, int index) {
        return pd.getConfig().getLong("cooldowns.cooldoown_" + index + ".time");
    }


    public static void setToActive(PlayerData pd, int index, boolean value) {
        pd.getConfig().set("cooldowns.cooldoown_" + index + ".active", value);
        pd.save();
    }

    public static void setCooldown(PlayerData pd, int index, long value) {
        pd.getConfig().set("cooldowns.cooldoown_" + index + ".time", value);
        pd.save();
    }

    public static boolean isActive(PlayerData pd, int index) {
        return pd.getConfig().getBoolean("cooldowns.cooldoown_" + index + ".active");
    }

}
