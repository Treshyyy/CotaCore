package com.cota.cotacore.core.managers.menu.buttons;


import com.cota.cotacore.core.managers.menu.AbstractMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockButton implements Listener {


    private boolean centered;
    private int menuSize;
    private ItemStack itemStack;
    private AbstractMenu menu;
    private boolean isPageFull= false;
    private int stopIndex = -1;

    ///Constructor
    public BlockButton(AbstractMenu menu) {
        this.menu = menu;
        menuSize = menu.getSize();
    }

    ///Setting the index of the button
    public void setStopIndex(int index) {
        this.stopIndex = index;
    }


    ///Getting the ItemStack of the button
    public ItemStack getItemStack() {
        return itemStack;
    }



    ///Making the button as an ItemStack
    public void setItemStack(ItemStack itemStack) {
        if (itemStack == null){
            Bukkit.getLogger().severe("The itemStack is null."); ///Logging an error if the itemStack is null
            return;
        }
        this.itemStack = itemStack; ///Setting the itemStack

    }



    ///Initializing the button (Adding the button to the currently implemented menu)
    public void initializeButton() {
        register();
        if (stopIndex != -1) {
            menu.addBlockButton(this, stopIndex);

        } else {
            menu.addBlockButton(this);
        }
    }

    ///Does nothing, but it will be overridden in the class that extends this class
    public void onButtonClick(Player player, ClickType clickType, Inventory inventory, ItemStack itemStack) {
    }



    ///This is a basic event handler for the button click event (Spigot/Paper API)

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        if (menu.getNextPageClass() == null) {
            if (e.getInventory().equals(menu.getMenu())) {

                if (e.getCurrentItem().equals(itemStack)) {
                    onButtonClick(p, e.getClick(), e.getInventory(), e.getCurrentItem()); ///Calling the onButtonClick method which will be overridden in the class that extends this class
                    e.setCancelled(true); ///Cancelling the event
                }
            }
        }else {


            //Making sure the buttons are not removable even in the new pages!
            if (e.getCurrentItem().equals(itemStack)) {
                onButtonClick(p, e.getClick(), e.getInventory(), e.getCurrentItem()); ///Calling the onButtonClick method which will be overridden in the class that extends this class
                e.setCancelled(true); ///Cancelling the event
            }

        }
    }


    private void register() {
        menu.getPlugin().getServer().getPluginManager().registerEvents(this, menu.getPlugin());///Registering the event

    }

}
