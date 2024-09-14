package com.cota.cotacore.core.listener;// EventInterfaces.java
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public interface EventInterfaces {
    void onPlayerMove(PlayerMoveEvent e);
    void onBlockBreak(BlockBreakEvent e);
    void onPlayerInteract(PlayerInteractEvent e);
    void onEntityDamageByEntity(EntityDamageByEntityEvent e);
    void onPlayerDropItem(PlayerDropItemEvent e);
    void onPlayerChat(AsyncPlayerChatEvent e);
    void onPlayerLogin(PlayerLoginEvent e);
    void onPlayerJoin(PlayerJoinEvent e);
    void onPlayerQuit(PlayerQuitEvent e);
    void onPlayerKick(PlayerKickEvent e);
    void onEntityDamage(EntityDamageEvent e);
    void onEntityDeath(EntityDeathEvent e);
    void onInventoryClick(InventoryClickEvent e);
    void onInventoryClose(InventoryCloseEvent e);
    void onInventoryOpen(InventoryOpenEvent e);
    void onBlockPlace(BlockPlaceEvent e);
    void onPlayerRespawn(PlayerRespawnEvent e);
    void onPlayerConsume(PlayerItemConsumeEvent e);
    void onPlayerDeath(PlayerDeathEvent e);
}
