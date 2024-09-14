package com.cota.cotacore.core.commands.utils;

import com.cota.cotacore.core.commands.main.CotaCommand;
import com.cota.cotacore.core.commands.parameters.ArgManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandUtils {


    public static ArgManager recogniseByName(String name) {
        for (ArgManager args : CotaCommand.stored_arguments) {
            if (args.name().equals(name)) {
                return args;
            }
        }
        return null;
    }

    public static Integer findLargestKey(HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> map) {
        // Check if the map is empty


        if (map.isEmpty()) {
            return 0;
        }

        // Initialize the largest key with the smallest possible integer value
        Integer largestKey = Integer.MIN_VALUE;

        // Iterate through the keys of the map
        for (Integer key : map.keySet()) {
            if (key > largestKey) {
                largestKey = key;
            }
        }

        return largestKey;
    }

    public static HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> getFixedTabCompletes(HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> tabCompletes) {
        HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> fixedTabCompletes = new HashMap<>();

        int j=0;
        int last_index = CommandUtils.findLargestKey(tabCompletes);
        for (Integer i: tabCompletes.keySet()) {


            fixedTabCompletes.put(i, tabCompletes.get(last_index-j));
            j++;
        }

        return fixedTabCompletes;
    }

}
