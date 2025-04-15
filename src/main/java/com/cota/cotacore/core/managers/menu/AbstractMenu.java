package com.cota.cotacore.core.managers.menu;



import com.cota.cotacore.core.managers.menu.buttons.BlockButton;
import com.cota.cotacore.core.managers.menu.buttons.Button;
import com.cota.cotacore.core.managers.menu.buttons.pages.NextPageButton;
import com.cota.cotacore.core.managers.menu.buttons.pages.PreviousPageButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class AbstractMenu implements Listener {

    private Inventory menu;
    private PageManager pageManager;
    private String title = "Menu";
    private int size = 1;
    private boolean isMultiplePage = false;
    private boolean addPageHeader = false;
    private NextPageButton nextPageButton;
    private PreviousPageButton previousPageButton;
    private static Plugin plugin;
    private int stopIndex = 0;
    private InventoryType customInventoryType = null;
    private List<Button> buttons = new ArrayList<>();

    ///Creating the menu
    public void createMenu() {
        if (customInventoryType == null) {
            if (addPageHeader) {
                menu = Bukkit.createInventory(null, 9 * this.size, this.title + " §7(1)");
                return;
            }
            menu = Bukkit.createInventory(null, 9 * this.size, this.title);
        }else {
            if (addPageHeader) {
                menu = Bukkit.createInventory(null, customInventoryType, this.title + " §7(1)");
                return;
            }
            menu = Bukkit.createInventory(null, customInventoryType, this.title);
        }
        register();
    }
    ///Setting the title of the menu
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCustomInventoryType(InventoryType inventoryType) {
        this.customInventoryType = inventoryType;
    }

    ///Setting the size of the menu
    public void setSize(int size) {
        this.size = size;
    }

    //Setting for page header (for example %title%(1/30))
    public void addPageHeader(boolean value){
        this.addPageHeader = value;
    }

    //Checking if the menu has a page header
    public boolean hasPageHeader(){
        return addPageHeader;
    }


    ///Getting the size of the menu
    public int getSize() {
        return size;
    }


    ///Getting the menu
    public Inventory getMenu() {
        return menu;
    }


    ///Displaying the menu to the player
    public void displayTo(Player p) {
        p.openInventory(menu);
    }

    ///Setting the menu to be multiple pages
    public void isMultiplePage(boolean value){
        this.isMultiplePage = value;
    }

    ///Checking if the menu has a next page
    public boolean hasNextPage(){
        return isMultiplePage;
    }






    ///Adding a button to the menu
    public void addButton(Button button) {
        buttons.add(button);
        menu.setItem(button.getIndex(), button.getItemStack());
    }

    ///-------------Getting the not-null items-------------------------------
    ///That means that every item that is not null will be counted
    private int getNotNullItems(int stopIndex, ItemStack blockButton) {
        int contents = 0;
        for (int i=0; i<stopIndex; i++) {
            if (menu.getItem(i) != null) {
                if (menu.getItem(i).equals(blockButton)) {
                    contents++;
                }

            }
        }
        return contents;
    }
    ///-----------------------------------------------------------------

    ///-------------Getting the non-block items-------------------------------
    ///That means that every item that is not the blockButton will be counted
    private int getNonBlockItems(int stopIndex, ItemStack blockButton) {
        int nonBlockItem = 0;
        for (int i=0; i<stopIndex; i++) {
            if (menu.getItem(i) != null) {
                if (!menu.getItem(i).equals(blockButton)) {
                    nonBlockItem++;
                }

            }
        }
        return nonBlockItem;
    }
    ///------------------------------------------------------------------------

    public void addBlockButton(BlockButton blockButton, int stopIndex) {

        this.stopIndex = stopIndex;
        ///We are checking if the contents of the menu are less than the size of the menu, if it is then we add the item to the menu
        if (getNotNullItems(stopIndex, blockButton.getItemStack()) <= stopIndex-1-getNonBlockItems(stopIndex, blockButton.getItemStack())) {

            if (!Objects.equals(menu.getItem(stopIndex), blockButton.getItemStack())) {
                if (menu.getItem(stopIndex) != null) {
                    return;
                }
                menu.addItem(blockButton.getItemStack());///Adding the current item to the menu
            }
        }else if (hasNextPage()){

            ///If the menu has a next page, and the items are out of bound then we create a new page and add the remaining items to the next page
            if (pageManager==null) {
                pageManager = new PageManager(this, 1);
            ///Checking if the pageManager has already been initialized, because we don't want to create a new constructor every time we add a new item
            }
            pageManager.addBlockButton(blockButton, stopIndex);
            ///Recursively adding the blockButton to the next page

        }
    }

    public void addBlockButton(BlockButton blockButton) {
        ///We are checking if the contents of the menu are less than the size of the menu, if it is then we add the item to the menu
        ///In that case there is no stopIndex, so we will use the whole size of the menu
        if (getNotNullItems(menu.getSize(), blockButton.getItemStack()) <= menu.getSize()-1-getNonBlockItems(menu.getSize(), blockButton.getItemStack())) {
            menu.addItem(blockButton.getItemStack());///Adding the current item to the menu
        }else if (hasNextPage()){

            ///If the menu has a next page, and the items are out of bound then we create a new page and add the remaining items to the next page
            if (pageManager==null) {
                pageManager = new PageManager(this, 1);
                ///Checking if the pageManager has already been initialized, because we don't want to create a new constructor every time we add a new item
            }
            pageManager.addBlockButton(blockButton);
            ///Recursively adding the blockButton to the next page
        }
    }

    public PageManager getNextPageClass(){
        return pageManager;
    }

    public void refreshNextPageClass(PageManager pageManager){
        this.pageManager = pageManager;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void addNextPageButton(NextPageButton nextPageButton) {
        ///Creating the next page button which will initialize the next page
        if (pageManager != null) {
            menu.setItem(nextPageButton.getIndex(), nextPageButton.getItemStack());
        }
        this.nextPageButton = nextPageButton;
    }

    public NextPageButton getNextPageButton() {
        return nextPageButton;
    }

    public PreviousPageButton getPreviousPageButton() {
        return previousPageButton;
    }

    public int getStopIndex() {
        return stopIndex;
    }

    public void addPreviousPageButton(PreviousPageButton previousPageButton) {
        ///Creating the previous page button which will initialize the previous page
        if (nextPageButton.getCurrentPage()> 1) {
            menu.setItem(previousPageButton.getIndex(), previousPageButton.getItemStack());

        }
        this.previousPageButton = previousPageButton;
    }

    public String getTitle() {
        ///Returning the title of the menu
        return title;
    }

    public void onInvClose(Player p, Inventory inv) {
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public static void SetupAbstractMenu(Plugin plugin) {

        AbstractMenu.plugin = plugin;

    }

    private void register() {
        plugin.getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void invClose(org.bukkit.event.inventory.InventoryCloseEvent e) {
        if (e.getInventory().equals(menu)) {
            onInvClose((Player) e.getPlayer(), e.getInventory());
        }
    }
}
