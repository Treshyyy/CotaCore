package com.cota.cotacore.main.files;


import com.cota.cotacore.CotaCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileHandlers {


    private File file;
    protected FileConfiguration config;





    public FileHandlers(String path, String fileName) {
        this.file = new File(path, fileName);
        if (!file.exists()) {
            file.mkdir();

        }
        this.config = YamlConfiguration.loadConfiguration(file);

    }
    public  void save() {
        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        config = null;
        file.delete();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean exists() {

        return file.exists();
    }

    public String getName() {
        return file.getName();
    }

}
