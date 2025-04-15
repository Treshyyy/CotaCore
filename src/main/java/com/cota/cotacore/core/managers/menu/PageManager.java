package com.cota.cotacore.core.managers.menu;



import com.cota.cotacore.core.managers.menu.buttons.BlockButton;
import com.cota.cotacore.core.managers.menu.buttons.pages.NextPageButton;
import com.cota.cotacore.core.managers.menu.buttons.pages.PreviousPageButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;


public class PageManager {

    ///We create pages as an array, so we can hold multiple pages in one Inventory array
    private static final Inventory[] page = new Inventory[10000];

    private final AbstractMenu menu;
    private int pageIndex =1;
    private PageManager pageManager;
    private NextPageButton nextPageButton;
    private PreviousPageButton previousPageButton;



    ///Creating the pageManager
    public PageManager(AbstractMenu menu, int pageIndex) {
        this.pageIndex = pageIndex;
        this.menu = menu;
        page[this.pageIndex] = menu.getMenu();///Getting the menu
        if (menu.hasPageHeader()) {
            page[this.pageIndex] = Bukkit.createInventory(null, 9*menu.getSize(), menu.getTitle()+" §7("+ Integer.valueOf(pageIndex+1)+")");
            return;

        }
        page[this.pageIndex] = Bukkit.createInventory(null, 9*menu.getSize(), menu.getTitle());///Creating the menu

    }


    ///Adding the blockButton to the menu

    ///If the menu is full, the blockButton will be added to the next page
    public void addBlockButton(BlockButton blockButton, int stopIndex) {
    ///The stopIndex is the index where the blockButton will stop adding items to the menu
        ///We are checking if the contents of the menu are less than the size of the menu, if it is then we add the item to the menu
        if (getNotNullItems(stopIndex, blockButton.getItemStack()) <= stopIndex-1-getNonBlockItems(stopIndex, blockButton.getItemStack())) {

            if (!Objects.equals(page[this.pageIndex].getItem(stopIndex), blockButton.getItemStack())) {

                if (page[this.pageIndex].getItem(stopIndex) != null) {
                    return;
                }
                page[this.pageIndex].addItem(blockButton.getItemStack());///Adding the blockButton to the menu


            }
        }else{
            if (pageManager == null) {
                pageManager = new PageManager(menu, pageIndex+1);
                ///Checking if the pageManager has already been initialized, because we don't want to create a new constructor every time we add a new item

                menu.refreshNextPageClass(pageManager);
                ///We have to refresh the next page class, so it will always be the latest version of the class

            }
            pageManager.addBlockButton(blockButton, stopIndex);



        }
    }


    ///Adding the blockButton to the menu
    public void addBlockButton(BlockButton blockButton) {
        ///We are checking if the contents of the menu are less than the size of the menu, if it is then we add the item to the menu
       ///In that case there is no stopIndex, so we will use the whole size of the menu
        if (getNotNullItems(page[this.pageIndex].getSize(), blockButton.getItemStack()) <= page[this.pageIndex].getSize()-1-getNonBlockItems(page[this.pageIndex].getSize(), blockButton.getItemStack())) {
            page[this.pageIndex].addItem(blockButton.getItemStack());///Adding the blockButton to the menu
            for (int i=menu.getStopIndex()+1; i<menu.getSize(); i++) {
                page[this.pageIndex].setItem(i, menu.getMenu().getItem(i));
            }
        }else{
            if (pageManager == null) {
                pageManager = new PageManager(menu, pageIndex+1);
                ///Checking if the pageManager has already been initialized, because we don't want to create a new constructor every time we add a new item

                menu.refreshNextPageClass(pageManager);
                ///We have to refresh the next page class, so it will always be the latest version of the class
            }
            pageManager.addBlockButton(blockButton);

        }
    }

    ///Displaying the menu to the player
    public void displayTo(Player p, int pageIndex){
        p.openInventory(page[pageIndex]);
    }



    public PageManager getNextPageClass(){
        return pageManager;
    }

    ///-------------Getting the not-null items-------------------------------
    ///That means that every item that is not null will be counted
    private int getNotNullItems(int stopIndex, ItemStack blockButton) {
        int contents = 0;
        for (int i=0; i<stopIndex; i++) {
            if (page[this.pageIndex].getItem(i) != null) {
                if (page[this.pageIndex].getItem(i).equals(blockButton)) {
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
            if (page[this.pageIndex].getItem(i) != null) {
                if (!page[this.pageIndex].getItem(i).equals(blockButton)) {
                    nonBlockItem++;
                }

            }
        }
        return nonBlockItem;
    }
    ///------------------------------------------------------------------------


    ///Adding the next page button to the menu
    public void addNextPageButton(NextPageButton nextPageButton, int pageIndex) {

        page[pageIndex].setItem(nextPageButton.getIndex(), nextPageButton.getItemStack());
        this.nextPageButton = nextPageButton;
    }

    public NextPageButton getNextPageButton() {
        return nextPageButton;
    }

    public PreviousPageButton getPreviousPageButton() {
        return previousPageButton;
    }


    ///Adding the previous page button to the menu
    public void addPreviousPageButton(PreviousPageButton previousPageButton, int pageIndex) {
        ///Creating the previous page button which will initialize the previous page
        page[pageIndex].setItem(previousPageButton.getIndex(), previousPageButton.getItemStack());
        this.previousPageButton = previousPageButton;


    }



    ///Getting the menu
    public Inventory getMenu(int pageIndex) {
        return page[pageIndex];
    }




    public int getAllPages() {
        return Arrays.stream(page).filter(Objects::nonNull).toArray().length;
    }

}
