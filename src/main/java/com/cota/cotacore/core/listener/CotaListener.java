package com.cota.cotacore.core.listener;

import com.cota.cotacore.CotaCore;
import com.cota.cotacore.core.interfaces.Register;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public class CotaListener implements EventInterfaces, Listener, Register {


    @Override
    public void register() {
        System.out.println("Registering CotaListener...");
        CotaCore.INSTANCE.getUserPlugin().getServer().getPluginManager().registerEvents(this, CotaCore.INSTANCE.getUserPlugin());
    }

    @EventHandler
    private void asdfasz(PlayerMoveEvent e) {
        e.getPlayer().sendMessage("KURVA");
        onPlayerMove(e);
    }

    @EventHandler
    private void onBlockBreakEvent(BlockBreakEvent e) {
        onBlockBreak(e);
    }

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e) {
        onPlayerInteract(e);
    }

    @EventHandler
    private void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        onEntityDamageByEntity(e);
    }

    @EventHandler
    private void onPlayerDropItemEvent(PlayerDropItemEvent e) {

        onPlayerDropItem(e);
    }

    @EventHandler
    private void onPlayerDeathEvent(PlayerDeathEvent e) {
        onPlayerDeath(e);
    }
    @EventHandler
    private void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        onPlayerRespawn(e);
    }


    @EventHandler
    private void onPlayerChatEvent(AsyncPlayerChatEvent e) {
        onPlayerChat(e);
    }

    @EventHandler
    private void onPlayerLoginEvent(PlayerLoginEvent e) {
        onPlayerLogin(e);
    }

    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent e) {
        e.getPlayer().sendMessage("SZIAA");
        onPlayerJoin(e);
    }

    @EventHandler
    private void onPlayerQuitEvent(PlayerQuitEvent e) {
        onPlayerQuit(e);
    }


    @EventHandler
    private void onBlockPlaceEvent(BlockPlaceEvent e) {
        onBlockPlace(e);
    }

    @EventHandler
    private void onPlayerConsumeEvent(PlayerItemConsumeEvent e) {
        onPlayerConsume(e);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent e) {
        // Default implementation
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent e) {
        // Default implementation
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        // Default implementation
    }



    @Override
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerKick(PlayerKickEvent e) {
        // Default implementation
    }

    @Override
    public void onEntityDamage(EntityDamageEvent e) {
        // Default implementation
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent e) {
        // Default implementation
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        // Default implementation
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent e) {
        // Default implementation
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent e) {
        // Default implementation
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        // Default implementation
    }

    @Override
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        // Default implementation
    }
}
