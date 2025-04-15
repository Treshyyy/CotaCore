package com.cota.cotacore.core.managers;

import com.cota.cotacore.CotaCore;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;


public class ItemManager implements Listener {


    private ItemStack item;
    private ItemStack forceItem;
    private String perm;
    private boolean despawn;
    private Player p;
    private boolean explode;
    private boolean burn;
    private boolean consumable = false;
    private boolean force = false;
    private int forceSlot;
    private boolean swapuse = false;
    private boolean bothuse = false;
    private boolean restrict = false;


    public ItemManager(ItemStack item) {
        this.item = item;
    }

    public ItemManager(Player p, ItemStack item) {
        this.item = item;
        this.p = p;
    }



    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }
    public void restrictMove(boolean value) {
        this.restrict = value;
    }


    public void isConsumable(boolean value) {
        this.consumable = value;
    }

    public void isSwapUse(boolean value) {
        this.swapuse = value;
    }
    public void isBothUse(boolean value) {
        this.bothuse = value;
    }

    public void forceInSlot(int slot) {
        this.force = true;
        this.forceSlot = slot;
    }

    public void onUse(Player p, ItemStack item, Action action, EquipmentSlot slot, Block clickedBlock) {

    }

    public void onUse(Player p, ItemStack item) {

    }

    public boolean canDespawn() {
        return despawn;
    }

    public void setDespawnable(boolean despawn) {
        this.despawn = despawn;
    }

    public boolean canExplode() {
        return explode;
    }

    public void setExplodable(boolean explode) {
        this.explode = explode;
    }

    public void register() {
        CotaCore.INSTANCE.getUserPlugin().getServer().getPluginManager().registerEvents(this, CotaCore.INSTANCE.getUserPlugin());
    }

    public boolean canBurn() {
        return burn;
    }

    public void setBurnable(boolean burn) {
        this.burn = burn;
    }
    public void removeOne() {
        ItemStack item = p.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        p.getInventory().setItemInMainHand(item);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (perm !=null && !p.hasPermission(perm)) return;
        if (e.getItem() == null) return;
        ItemStack check = e.getItem().clone();

        check = new ItemStack(check);
        check.setAmount(1);
        if (!check.equals(getItem())) return;

        if (consumable) {
            ItemStack item = p.getInventory().getItemInMainHand();
            item.setAmount(item.getAmount() - 1);
            p.getInventory().setItemInMainHand(item);
        }

        if (!swapuse || bothuse) {
            onUse(p, e.getItem(), e.getAction(), e.getHand(), e.getClickedBlock());

        }



    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {

        if (!e.getEntity().getItemStack().equals(getItem())) return;
        if (canDespawn()) return;
        e.setCancelled(true);

    }


    @EventHandler
    public void onBurn(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Item i)) return;

        if (!i.getItemStack().equals(getItem())) return;

        if (!canBurn()) {

            if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                   || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                e.setCancelled(true);
            }
        }

        if (!canExplode()) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvrntoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();


        if (!force && !restrict) return;

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.equals(getItem())) {

            if (force) {
                e.setCancelled(true);
                p.closeInventory();
            }

        }else {
            ItemStack old_item = e.getCurrentItem();
            new BukkitRunnable() {
                @Override
                public void run() {

                    int new_slot = e.getSlot();
                    boolean isCrafter = false;
                    boolean container = false;
                    ItemStack item = e.getInventory().getItem(new_slot);
                    if (e.getInventory().getType().equals(InventoryType.CRAFTING)) {
                        item = p.getInventory().getItem(new_slot);
                        isCrafter = true;
                    }
                    if (item != null && item.equals(getItem())) {
                        if (force) {
                            p.getInventory().setItem(forceSlot, item);
                        } else {
                            if (isCrafter) {
                                p.getInventory().setItem(new_slot, item);

                            } else {
                                p.getInventory().addItem(item);
                                container = true;

                            }

                        }


                        // if (force) {
                        if (!isCrafter) {
                            if (force || container) {
                                e.getInventory().setItem(new_slot, old_item);
                            }
                        } else {
                            if (force) {
                                p.getInventory().setItem(new_slot, old_item);
                            }
                        }
                        //}


                        p.closeInventory();
                        e.setCancelled(true);


                    }
                }

            }.runTaskLater(CotaCore.INSTANCE.getUserPlugin(), 1L);
        }

    }

    @EventHandler
    public void onInvrntoryMove(InventoryDragEvent e) {


        if (!force && !restrict) return;

        /*ItemStack item = e.getItem();
        if (item == null) return;
        if (item.equals(getItem())) {
            e.setCancelled(true);
        }*/

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = (Player) e.getPlayer();

        if (!force && !restrict) return;

        ItemStack item = e.getItemDrop().getItemStack();
        if (item.equals(getItem())) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = (Player) e.getEntity();

        if (!force && !restrict) return;

        List<ItemStack> drops = e.getDrops();
        drops.remove(getItem());

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = (Player) e.getPlayer();

        if (!force && !restrict) return;


        p.getInventory().setItem(forceSlot, getItem());


    }

    @EventHandler
    public void swapItemEvent(PlayerSwapHandItemsEvent e) {
        Player p = (Player) e.getPlayer();

        if (!force && !bothuse && !restrict) return;
        ItemStack off_item = e.getOffHandItem();
        ItemStack item = e.getMainHandItem();


        if (off_item != null && off_item.equals(getItem()) || item != null && item.equals(getItem())) {
            e.setCancelled(true);
            if (swapuse || bothuse) {
                onUse(p, off_item != getItem() ? item : off_item);

            }
        }


    }


}
