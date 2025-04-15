package com.cota.cotacore.core.damage;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class PopTotemUtil {


    public static void pop(Player damagedPlayer) {
        if (damagedPlayer.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)) {



            damagedPlayer.playEffect(EntityEffect.TOTEM_RESURRECT);
            damagedPlayer.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(20 * 40, 0));
            damagedPlayer.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(20 * 5, 1));
            damagedPlayer.addPotionEffect(PotionEffectType.REGENERATION.createEffect(20 * 45, 1));
            damagedPlayer.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            return;
        } else if (damagedPlayer.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {


            damagedPlayer.playEffect(EntityEffect.TOTEM_RESURRECT);
            damagedPlayer.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(20 * 40, 0));
            damagedPlayer.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(20 * 5, 1));
            damagedPlayer.addPotionEffect(PotionEffectType.REGENERATION.createEffect(20 * 45, 1));
            damagedPlayer.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            return;
        } else {
           // damagedPlayer.kill
            if (!damagedPlayer.isDead()) {
                damagedPlayer.setHealth(0);
            }




        }
    }

}
