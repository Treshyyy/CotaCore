package com.cota.cotacore.core.managers.menu.buttons;



import com.cota.cotacore.core.managers.menu.AbstractMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class Button implements Listener {

    private int index;
    private boolean centered;
    private int menuSize;
    private ItemStack itemStack;
    private AbstractMenu menu;
    private Plugin plugin;

    public Button(AbstractMenu menu) {
        this.menu = menu;
        menuSize = menu.getSize();
    }

    ///Setting the index of the button
    public void setIndex(int index) {
        this.index = index;
    }

    ///Getting the index of the button
    public int getIndex() {
        return index;
    }

    ///Getting the ItemStack of the button
    public ItemStack getItemStack() {
        return itemStack;
    }


    ///Setting the button to be centered
    public void isCentered(boolean centered) {
        if ((menuSize*9-1) % 2 != 0) {
            this.centered = false;
            Bukkit.getLogger().severe("The menu with a size of 9*"+menuSize+" doesn't have a true center. Centering will be disabled."); ///Logging an error if the menu doesn't have a true center
            return;
        }
        this.centered = centered;
        setIndex((menuSize*9-1)/2);

    }

    ///Adjusting the index of the button if centered
    public void adjustCenteredButton(int index) {
        if (centered) {
            if (this.index +index > menuSize*9) {
                Bukkit.getLogger().severe("The button is out of the menu's bounds."); ///Logging an error if the button is out of the menu's bounds
                return;
            }
            this.index = this.index + index;
        }
    }


    ///Setting the ItemStack of the button
    public void setItemStack(ItemStack itemStack) {
        if (itemStack == null){
            Bukkit.getLogger().severe("The itemStack is null.");
            return;
        }
        this.itemStack = itemStack;

    }

    ///Initializing the button (Adding the button to the currently implemented menu)
    public void initializeButton() {
        register();
        menu.addButton(this);
    }

    ///This is a method that will be overridden in the class that extends this class
    public void onButtonClick(InventoryClickEvent e) {
    }
 


    ///This is a basic event handler for the button click event (Spigot/Paper API)
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        if (menu.getNextPageClass() == null) {
            if (e.getInventory().equals(menu.getMenu())) {
                if (e.getCurrentItem().equals(itemStack)) {
                    if (e.getSlot() == getIndex()) {
                        onButtonClick(e); ///Calling the onButtonClick method which will be overridden in the class that extends this class
                        e.setCancelled(true); ///Cancelling the event
                    }

                }
            }
        }else {


            //Making sure the buttons are not removable even in the new pages!
            if (e.getCurrentItem().equals(itemStack)) {
                if (e.getSlot() == getIndex()) {
                    onButtonClick(e); ///Calling the onButtonClick method which will be overridden in the class that extends this class
                    e.setCancelled(true); ///Cancelling the event
                }
            }

        }
    }




    private void register() {
        menu.getPlugin().getServer().getPluginManager().registerEvents(this, menu.getPlugin()); ///Registering the event

    }

}
