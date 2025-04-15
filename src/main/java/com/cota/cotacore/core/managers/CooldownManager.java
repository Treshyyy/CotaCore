package com.cota.cotacore.core.managers;


import com.cota.cotacore.CotaCore;
import com.cota.cotacore.main.files.data.files.PlayerData;
import com.cota.cotacore.main.files.data.files.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownManager {
    private  Player p;

    private int cooldown;
    private int duration = 0;
    private int ability = 0;
    private PlayerData pd;

    private void setupVariables(Player p, int duration, int index, int cooldown) {

        this.p=p;
        this.cooldown = cooldown;
        this.duration = duration;
        this.ability = index;
    }

    public CooldownManager(Player p, int duration, int cooldown) {
       setupVariables(p, duration, 0, cooldown);
    }

    public CooldownManager(Player p, int cooldown) {
        setupVariables(p, 0, 0, cooldown);

    }
    public CooldownManager(Player p, double index) {
        setupVariables(p, 0, (int) index, 0);

    }



    public CooldownManager(Player p, int duration, int index, int cooldown) {
        setupVariables(p, duration, index, cooldown);
    }

    public CooldownManager(Player p, double index, int cooldown) {
      setupVariables(p, 0, (int) index, cooldown);
    }


    public String bypassCooldown() {
        return "cooldowns.bypass";
    }

    public String cooldownMessage() {
        return "You can't use this ability for another %second% seconds!";
    }

    public String activeMessage() {
        return "This ability is already active!";
    }

    public String reminderMessage() {
        return "You can use this ability again!";
    }
    public String startMessage() {
        return "Ability activated!";
    }



    public boolean onCooldown() {

        if (p.hasPermission(bypassCooldown())) {
            if (!isActive()) {
                if (startMessage() != null) {
                    p.sendMessage(startMessage()
                            .replaceAll("%index%", String.valueOf(ability)));
                }
                setActive(true);
                startDurationTimer();
            }else {
                if (activeMessage() != null) {
                    p.sendMessage(activeMessage());
                }

            };
            return false;
        }

        this.pd = new PlayerData(p);
        long current_time = System.currentTimeMillis();
        long current_cooldown = PlayerUtils.getCooldown(pd, ability);

        if (!isActive()) {
            if ((current_time-current_cooldown)/1000 > cooldown || current_cooldown ==0) {
                setActive(true);
                startDurationTimer();
                if (startMessage() != null) {
                    p.sendMessage(startMessage()
                            .replaceAll("%index%", String.valueOf(ability)));
                }



                return false;

            }else {
                if (cooldownMessage() != null) {
                    p.sendMessage(cooldownMessage()
                            .replace("%second%", getCooldown() + ""));
                }
            }
        }else {
            if (cooldownMessage() != null) {
                p.sendMessage(activeMessage());
            }
        }


        return true;
    }

    public void handleEnd() {

    }
    public void onCooldownReset() {

    }


    public boolean isCooldownActive() {
        this.pd = new PlayerData(p);
        if (p.hasPermission(bypassCooldown())) return false;

        long current_time = System.currentTimeMillis();
        long current_cooldown = PlayerUtils.getCooldown(pd, ability);

        if ((current_time-current_cooldown)/1000 > cooldown || current_cooldown ==0) return false;


        return true;
    }

    public void resetCooldownManager() {
        setActive(false);
        resetCooldown();
    }


    private void setActive(boolean value) {
        this.pd = new PlayerData(p);
        PlayerUtils.setToActive(pd, ability, value);
    }

    public boolean isActive() {
        this.pd = new PlayerData(p);
        return PlayerUtils.isActive(pd, ability);
    }
    public int getCooldown() {
        long current_time = System.currentTimeMillis();
        long current_cooldown = PlayerUtils.getCooldown(pd, ability);
        return (int) (cooldown - ((current_time-current_cooldown)/1000));
    }



    private void resetCooldown() {
        this.pd = new PlayerData(p);
        PlayerUtils.setCooldown(pd, ability, System.currentTimeMillis());
        startCooldownReminder();
    }

    private void startDurationTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                setActive(false);
                resetCooldown();
                handleEnd();
            }
        }.runTaskLater(CotaCore.INSTANCE.getUserPlugin(), duration* 20L);
    }

    private void startCooldownReminder() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (reminderMessage() != null) {
                    p.sendMessage(reminderMessage());

                }
                onCooldownReset();
            }
        }.runTaskLater(CotaCore.INSTANCE.getUserPlugin(), cooldown* 20L);
    }


}
