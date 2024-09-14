package com.cota.cotacore.core.commands.parameters;

import com.cota.cotacore.core.commands.main.CotaCommand;
import com.cota.cotacore.core.commands.main.TabHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

// Class to manage command arguments
public class ArgManager {

    // HashMap to store arguments and their corresponding managers
    private HashMap<String, ArgManager> arguments = new HashMap<>();


    // Variable to store dynamic arguments manager
    private ArgManager dynamicArguments = null;

    // Default depth is 1
    private int depth = 2;

    // Reference to the main command
    public CotaCommand main;

    // Constructor to initialize with main command
    public ArgManager(CotaCommand main) {
        this.main = main;
    }

    // Methods to execute command (can be overridden)
    public void execute(CommandSender sender, String[] args) {
        // Default implementation, can be overridden
    }

    public void execute(CommandSender sender, String arg) {
        // Default implementation, can be overridden
    }

    // Method to handle command execution with arguments
    public void handle(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            boolean valid_argument_found = false;
            int last_index = args.length - 1;

            if (permission() != null && !sender.hasPermission(permission())) {
                sender.sendMessage(main.noPerm());
                return;
            }
            // Check if the last argument matches a static argument
            if (this.containsArgument(args[last_index])) {
                this.getArgumentClass(args[last_index]).execute(sender, args);
                valid_argument_found = true;
            } else {
                // Iterate over arguments to find a match
                for (int i = 1; i < args.length; i++) {
                    String indexed_argument = args[i];
                    if (this.containsArgument(indexed_argument)) {
                        this.getArgumentClass(indexed_argument).handle(sender, args);
                        valid_argument_found = true;
                        break;
                    }
                }

                // If no static argument matched, process dynamic arguments
                if (!valid_argument_found) {
                    if (this.getDynamicArgumentClass() != null) {
                        if (getDynamicArgumentClass().getArgumentsList().isEmpty()) {
                            execute(sender, args);
                        } else {
                            getDynamicArgumentClass().handle(sender, args);
                        }
                        valid_argument_found = true;
                    } else if (getMainCommandClass().getDynamicArgumentClass() != null) {
                        getMainCommandClass().execute(sender);
                        valid_argument_found = true;
                    }
                }
            }

            // If no argument was processed, send usage message
            if (!valid_argument_found) {
                sender.sendMessage(usage());
            }
        } else {
            execute(sender, args);
        }
    }

    // Method to add static or dynamic arguments
    public void addArguments(ArgManager args) {
        addArguments(args, true);
    }

    // Overloaded method to add arguments with optional tab completion
    public void addArguments(ArgManager args, boolean useTabComplete) {
      //  args.setDepth(this.depth - 1);
        this.setDepth(args.getDepth()+1);

        if (args.name() == null) {
            this.addDynamicArgument(args);
        } else {
            String name = args.name();
            this.addArguments(name, args);
            main.stored_arguments.add(args);
        }

        // Log depth

        if (useTabComplete) {
            // Retrieve or create the current_tabs map for the given depth
            HashMap<ArrayList<String>, ArrayList<String>> current_tabs = main.getTabCompletes().getOrDefault(args.getDepth(), new HashMap<>());

            // Create the new ArrayList<String> key
            ArrayList<String> same_tabs = current_tabs.get(new ArrayList<>(List.of(args.name())));
            same_tabs.add(this.name());
            current_tabs.put(new ArrayList<>(List.of(args.name())), same_tabs);

            // Put the updated current_tabs map back into the main tabCompletes map
            main.getTabCompletes().put(args.getDepth(), current_tabs);
        }
    }

    // Overloaded method to add arguments with custom tab completions
    public void addArguments(ArgManager args, ArrayList<String> tab) {
        addArguments(args, tab, true);
    }

    // Overloaded method to add arguments with custom tab completions and optional tab completion flag
    public void addArguments(ArgManager args, ArrayList<String> tab, boolean useTabComplete) {
       // args.setDepth(this.depth - 1);
        this.setDepth(args.getDepth()+1);
        if (args.name() == null) {
            this.addDynamicArgument(args);
        } else {
            String name = args.name();
            this.addArguments(name, args);
            main.stored_arguments.add(args);
        }

        // Log depth

        if (useTabComplete) {
            // Retrieve or create the current_tabs map for the given depth
            HashMap<ArrayList<String>, ArrayList<String>> current_tabs = main.getTabCompletes().getOrDefault(args.getDepth(), new HashMap<>());

            // Create the new ArrayList<String> key
            ArrayList<String> same_tabs = current_tabs.get(tab);
            if (same_tabs == null) same_tabs = new ArrayList<>();
            same_tabs.add((this.name()));
            current_tabs.put(tab, same_tabs);

            // Put the updated current_tabs map back into the main tabCompletes map
            main.getTabCompletes().put(args.getDepth(), current_tabs);
        }
    }

    // Methods to manage argument classes
    public ArgManager getArgumentClass(String arg) {
        return arguments.get(arg);
    }

    public boolean containsArgument(String arg) {
        return arguments.containsKey(arg);
    }

    public void addArguments(String name, ArgManager arg) {
        this.arguments.put(name, arg);
    }

    public ArgManager getDynamicArgumentClass() {
        return dynamicArguments;
    }

    public void addDynamicArgument(ArgManager dynamicArguments) {
        this.dynamicArguments = dynamicArguments;
    }

    // Method to set depth
    public void setDepth(int depth) {
        this.depth = depth;
    }

    // Method to get depth
    public int getDepth() {
        return depth;
    }

    // Method to get the main command class
    public CotaCommand getMainCommandClass() {
        return main;
    }

    // Methods to provide default command attributes (can be overridden)
    public boolean dynamic() {
        return false;
    }

    public String usage() {
        return main.usage();
    }

    public String name() {
        return null;
    }

    public String permission() {
        return main.permission();
    }

    // Method to get a list of arguments for tab completion
    public HashMap<String, ArgManager> getArgumentsList() {
        return arguments;
    }
}
