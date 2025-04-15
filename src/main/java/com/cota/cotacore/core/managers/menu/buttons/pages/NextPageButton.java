package com.cota.cotacore.core.managers.menu.buttons.pages;



import com.cota.cotacore.core.managers.menu.AbstractMenu;
import com.cota.cotacore.core.managers.menu.PageManager;
import com.cota.cotacore.core.managers.menu.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class NextPageButton implements Listener {

    private int index;
    private boolean centered;
    private int menuSize;
    private ItemStack itemStack;
    private AbstractMenu menu;
    private PageManager pageManager;


    private int currentPage = 1;



    public NextPageButton(AbstractMenu menu) {
        this.menu = menu;
        menuSize = menu.getSize();

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void isCentered(boolean centered) {
        if ((menuSize*9-1) % 2 != 0) {
            this.centered = false;
            Bukkit.getLogger().severe("The menu with a size of 9*"+menuSize+" doesn't have a true center. Centering will be disabled.");
            return;
        }
        this.centered = centered;
        setIndex((menuSize*9-1)/2);

    }

    public void adjustCenteredButton(int index) {
        if (centered) {
            if (this.index +index > menuSize*9) {
                Bukkit.getLogger().severe("The button is out of the menu's bounds.");
                return;
            }
            this.index = this.index + index;
        }
    }

    public void setItemStack(ItemStack itemStack) {
        if (itemStack == null){
            Bukkit.getLogger().severe("The itemStack is null.");
            return;
        }
        this.itemStack = itemStack;

    }

    public void initializeButton() {
        register();
        menu.addNextPageButton(this);
    }

    public void onButtonClick(Player player, ClickType clickType, Inventory inventory, ItemStack itemStack) {
        ///NextPageButton and PreviousPageButton are working in synchronization.
        ///Every time a button is pressed, the other will get updated based on the page number.



        pageManager = menu.getNextPageClass();
        pageManager.displayTo(player, currentPage);
        if (currentPage!= pageManager.getAllPages())
            pageManager.addNextPageButton(this, currentPage);
        PreviousPageButton previousPageButton = menu.getPreviousPageButton();
        pageManager.addPreviousPageButton(previousPageButton, currentPage);


        //Adding normal button to the new pages too(Like return button, decoration buttons, etc.)
        for (Button button : menu.getButtons()) {
            int index = button.getIndex();
            pageManager.getMenu(currentPage).setItem(index, button.getItemStack());
        }

        currentPage ++;
        menu.refreshNextPageClass(pageManager);

    }


    ///Getting the current page



    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;





        if (e.getCurrentItem().equals(itemStack)) {

            if (pageManager != null) {
                if (e.getInventory().equals(pageManager.getMenu(currentPage - 1)) || e.getInventory().equals(menu.getMenu())) {

                    onButtonClick(p, e.getClick(), e.getInventory(), e.getCurrentItem());
                    e.setCancelled(true);
                }
            }else {
                if (e.getInventory().equals(menu.getMenu())) {

                    onButtonClick(p, e.getClick(), e.getInventory(), e.getCurrentItem());
                    e.setCancelled(true);
                }
            }
        }
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    private void register() {
        menu.getPlugin().getServer().getPluginManager().registerEvents(this, menu.getPlugin());

    }

}
