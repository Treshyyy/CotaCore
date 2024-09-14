package com.cota.cotacore.core.runnables;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractTask extends BukkitRunnable implements TaskInterface {

    private JavaPlugin plugin;

    public AbstractTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Method to start a repeating task
    public void startRepeatingTask(long delay, long period) {
        this.runTaskTimer(plugin, delay, period);
    }

    // Method to start a delayed task
    public void startDelayedTask(long delay) {
        this.runTaskLater(plugin, delay);
    }

    // Method to start an asynchronous repeating task
    public void startAsyncRepeatingTask(long delay, long period) {
        this.runTaskTimerAsynchronously(plugin, delay, period);
    }

    // Method to start an asynchronous delayed task
    public void startAsyncDelayedTask(long delay) {
        this.runTaskLaterAsynchronously(plugin, delay);
    }

    @Override
    public void run() {
        runTask();
    }
}
