package com.cota.cotacore.core.managers.menu.buttons.pages;



import com.cota.cotacore.core.managers.menu.AbstractMenu;
import com.cota.cotacore.core.managers.menu.PageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class PreviousPageButton implements Listener {

    private int index;
    private boolean centered;
    private int menuSize;
    private ItemStack itemStack;
    private AbstractMenu menu;
    private PageManager pageManager;





    ///Constructor
    public PreviousPageButton(AbstractMenu menu) {
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
            Bukkit.getLogger().severe("The menu with a size of 9*"+menuSize+" doesn't have a true center. Centering will be disabled.");
            return;
        }
        this.centered = centered;
        setIndex((menuSize*9-1)/2);

    }

    ///Adjusting the index of the button if centered
    public void adjustCenteredButton(int index) {
        if (centered) {
            if (this.index +index > menuSize*9) {
                Bukkit.getLogger().severe("The button is out of the menu's bounds.");
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
        menu.addPreviousPageButton(this);
    }


    ///Setting the button to be centered
    public void onButtonClick(Player player, ClickType clickType, Inventory inventory, ItemStack itemStack) {

        ///NextPageButton and PreviousPageButton are working in synchronization.
        ///Every time a button is pressed, the other will get updated based on the page number.


        ///Getting the current page by accessing it from an updating AbstractMenu class
        pageManager = menu.getNextPageClass();


        ///The main 'currentPage' variable is in the NextPageButton class, so we have to get it from there.

        int currentPage =1;
        if (pageManager.getNextPageButton() != null) {
            currentPage = pageManager.getNextPageButton().getCurrentPage();
        }



        if (currentPage-2 ==0) {
            menu.displayTo(player);
            pageManager.getNextPageButton().setCurrentPage(currentPage-1);

            return;
        }else if(currentPage-2<0) {
            menu.getNextPageButton().setCurrentPage(1);
            menu.displayTo(player);
            return;
        }
        pageManager.displayTo(player, currentPage-2);

        if (currentPage>1)
            pageManager.addPreviousPageButton(this, currentPage-1);
        pageManager.getNextPageButton().setCurrentPage(currentPage-1);

    }


    ///Getting the current page



    ///Registering the event
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        pageManager = menu.getNextPageClass();
        if (e.getCurrentItem().equals(itemStack)) {


            if (pageManager.getNextPageButton() != null) {
                if (e.getInventory().equals(pageManager.getMenu(pageManager.getNextPageButton().getCurrentPage() - 1))) {

                    onButtonClick(p, e.getClick(), e.getInventory(), e.getCurrentItem());
                    e.setCancelled(true);
                }
            }else {
                onButtonClick(p, e.getClick(), e.getInventory(), e.getCurrentItem());
                e.setCancelled(true);
            }
        }
    }



    ///Registering the event
    private void register() {
        menu.getPlugin().getServer().getPluginManager().registerEvents(this, menu.getPlugin());

    }

}
