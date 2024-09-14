package com.cota.cotacore.core.commands.main;

import com.cota.cotacore.core.commands.parameters.ArgManager;
import com.cota.cotacore.core.commands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabHandler implements TabCompleter {

    private HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> tabCompletes = new HashMap<>();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        ArrayList<String> all_tabs = new ArrayList<>();


        for (Integer keys: tabCompletes.keySet()) {
            HashMap<ArrayList<String>, ArrayList<String>> stored_tab_completes = tabCompletes.get(keys);
            for (ArrayList<String> tab_completes : stored_tab_completes.keySet()) {
                ArgManager am = CommandUtils.recogniseByName(tab_completes.get(0));
                if (am != null) {
                    if (am.permission() == null || sender.hasPermission(am.permission())) {
                        all_tabs.addAll(tab_completes);
                    }
                }



            }
            for (ArrayList<String> tab_completes: stored_tab_completes.keySet()) {
                ArrayList<String> tab_complete_index = stored_tab_completes.get(tab_completes);
                if (args.length == keys) {
                    if (args.length == 1) return all_tabs;
                    ArgManager am = CommandUtils.recogniseByName(args[keys - 2]);
                    if (am != null) {
                        if (am.permission() == null || sender.hasPermission(am.permission())) {

                            if (tab_complete_index.contains(args[keys-2])) return tab_completes;
                          //  if (args[keys - 2].equalsIgnoreCase(tab_complete_index)) return tab_completes;
                        }
                    }
                }
            }
        }
        return List.of();
    }

    public void addTabComplete(int index, ArrayList<String> tabs, String tab_index) {
        HashMap<ArrayList<String>, ArrayList<String>> stored_tab_completes = tabCompletes.getOrDefault(index, new HashMap<>());
        ArrayList<String> same_tabs = stored_tab_completes.getOrDefault(tabs, new ArrayList<>());
        if (!same_tabs.contains(tab_index)) {
            same_tabs.add(tab_index);
            stored_tab_completes.put(tabs, same_tabs);
        }



        tabCompletes.put(index, stored_tab_completes);
    }

    public void addTabComplete(int index, ArrayList<String> tabs, ArrayList<String> tab_index) {
        for (String s : tab_index) {
            addTabComplete(index, tabs, s);
        }
    }
}
