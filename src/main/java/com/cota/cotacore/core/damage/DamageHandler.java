package com.cota.cotacore.core.damage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class DamageHandler {

    private Entity e;
    private OfflinePlayer damager;
    private double damage;

    public DamageHandler(Entity e, OfflinePlayer damager, double damage) {
        this.e = e;
        this.damager = damager;
        this.damage = damage;
    }

    public DamageHandler(Entity e, Player damager, double damage) {
        this.e = e;
        this.damager = damager;
        this.damage = damage;
    }

    public void handle() {
        if (!(e instanceof LivingEntity livingEntity)) return;
        if (e instanceof Player target) {

        }

        double hp = livingEntity.getHealth() -damage;
        if (hp <= 0) {
            if (livingEntity instanceof Player target) {
                PopTotemUtil.pop(target);

            }
        }else {
            if (!livingEntity.isDead()) {
                livingEntity.setHealth(hp);
            }
            if (damager.isOnline()) {
                livingEntity.setKiller(Bukkit.getPlayer(damager.getUniqueId()));

            }

        }


        //
        if (!livingEntity.isDead()) {
            livingEntity.damage(.001);
        }


    }
}
