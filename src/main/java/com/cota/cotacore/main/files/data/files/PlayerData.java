package com.cota.cotacore.main.files.data.files;


import com.cota.cotacore.CotaCore;
import com.cota.cotacore.main.files.FileHandlers;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData extends FileHandlers {

    public PlayerData(OfflinePlayer p) {
        super( CotaCore.INSTANCE.getPlayerDataPath(), p.getUniqueId().toString() + ".yml");
    }

    public FileConfiguration getConfig() {
        return config;
    }



}
