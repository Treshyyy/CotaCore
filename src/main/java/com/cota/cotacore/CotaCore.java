package com.cota.cotacore;

import com.cota.cotacore.core.managers.menu.AbstractMenu;
import com.cota.cotacore.main.Design;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CotaCore extends JavaPlugin {


    public static CotaCore INSTANCE;




    @Override
    public void onEnable() {

        INSTANCE = this;
        AbstractMenu.SetupAbstractMenu(this.getUserPlugin());
        Design.showStartupMessage();
        this.getUserPlugin().saveDefaultConfig();
        File pdf = new File(playerDataPath());
        if(!pdf.exists()) {
            pdf.mkdirs();
        }
        //new CotaListener().register();
        CoreLoad();


    }


    @Override
    public void onDisable() {

        INSTANCE = this;
        Design.showShutdownMessage();
        //((CraftServer) CotaCore.INSTANCE.getUserPlugin().getServer()).getCommandMap().clearCommands();
        CoreUnload();
    }

    public JavaPlugin getUserPlugin() {
        return this;
    }

    public String name() {
        return "CotaCore";
    }


    public void CoreLoad() {

    }

    public void CoreUnload() {

    }

    public String playerDataPath() {
        return getUserPlugin().getDataFolder() + "/playerdata";
    }

    public String version() {
        return "1.0.0";
    }
}
