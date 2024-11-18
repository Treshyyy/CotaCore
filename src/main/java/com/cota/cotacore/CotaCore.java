package com.cota.cotacore;

import com.cota.cotacore.core.commands.main.CotaCommand;
import com.cota.cotacore.core.listener.CotaListener;
import com.cota.cotacore.core.managers.menu.AbstractMenu;
import com.cota.cotacore.main.Design;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

public class CotaCore extends JavaPlugin implements LoadMethods {


    public static CotaCore INSTANCE;



    public void onCoreLoad() {

    }

    public void onCoreUnLoad() {

    }
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
        onCoreLoad();


    }


    @Override
    public void onDisable() {

        INSTANCE = this;
        Design.showShutdownMessage();
        //((CraftServer) CotaCore.INSTANCE.getUserPlugin().getServer()).getCommandMap().clearCommands();
        onCoreUnLoad();
    }

    public JavaPlugin getUserPlugin() {
        return this;
    }

    @Override
    public String name() {
        return "CotaCore";
    }

    @Override
    public void CoreLoad() {

    }

    @Override
    public void CoreUnload() {

    }

    @Override
    public String playerDataPath() {
        return getUserPlugin().getDataFolder() + "/playerdata";
    }

    @Override
    public String version() {
        return "1.0.0";
    }
}
