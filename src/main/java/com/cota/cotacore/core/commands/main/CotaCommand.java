// Package declaration and necessary imports
package com.cota.cotacore.core.commands.main;

import com.cota.cotacore.CotaCore;
import com.cota.cotacore.core.commands.parameters.ArgManager;
import com.cota.cotacore.core.commands.utils.CommandUtils;
import com.cota.cotacore.core.interfaces.CommandMethods;
import com.cota.cotacore.core.interfaces.Register;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Main command class implementing CommandExecutor and Register
public class CotaCommand implements CommandExecutor, Register, CommandMethods {

    // HashMap to store arguments and their corresponding managers
    private HashMap<String, ArgManager> arguments = new HashMap<>();

    // Variable to store dynamic arguments manager
    private ArgManager dynamicArguments = null;

    // Singleton instance of TabHandler for tab completion
    private TabHandler tabHandler = new TabHandler();

    private static HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> tabCompletes = new HashMap<>();
    public static List<ArgManager> stored_arguments = new ArrayList<>();

    // Method to execute command without arguments (can be overridden)
    public void execute(@NotNull CommandSender sender) {
        // Default implementation, can be overridden
    }

    // Method to execute command with arguments
    private void executeArgs(@NotNull CommandSender sender, @NotNull String[] args) {
        String first_argument = args[0];
        if (this.containsArgument(first_argument)) {
            // Handle argument if it exists in the arguments map
            if (this.getArgumentClass(first_argument).permission() != null && !sender.hasPermission(this.getArgumentClass(first_argument).permission())) {
                sender.sendMessage(permissionMessage());
                return;
            }
            if (args.length == 1) {
                this.getArgumentClass(first_argument).execute(sender, args);

            }else {
                this.getArgumentClass(first_argument).handle(sender, args);

            }


        } else {
            if (this.getDynamicArgumentClass() != null) {
                // Handle argument if it matches dynamic arguments
                this.getDynamicArgumentClass().handle(sender, args);
            } else {
                // Send usage message if no arguments match
                sender.sendMessage(usage());
            }
        }
    }

    // Method to add static or dynamic arguments
    private void addArguments(ArgManager args) {
        addArguments(args, true);
    }

    // Overloaded method to add arguments with optional tab completion
    private void addArguments(ArgManager args, boolean useTabComplete) {


        if (args.name() == null) {
            // Add as dynamic argument if no command is provided
            addDynamicArgument(args);
        } else {
            String command = args.name();
            addArguments(command, args);
            stored_arguments.add(args);
        }

        if (useTabComplete) {
            // Add tab completion for the argument
            TabHandler th = getTabHandler();
            th.addTabComplete(1, new ArrayList<>(List.of(args.name())), this.command());

        }
    }

    // Overloaded method to add arguments with custom tab completions
    private void addArguments(ArgManager args, ArrayList<String> tab) {
        addArguments(args, tab, true);
    }

    // Overloaded method to add arguments with custom tab completions and optional tab completion flag
    private void addArguments(ArgManager args, ArrayList<String> tab, boolean useTabComplete) {

        if (args.dynamic()) {
            // Add as dynamic argument if dynamic flag is true
            addDynamicArgument(args);
        } else {
            String command = args.name();
            addArguments(command, args);
            stored_arguments.add(args);
        }

        if (useTabComplete) {
            // Add tab completion for the argument
            TabHandler th = getTabHandler();

            th.addTabComplete(1, tab, this.command());

        }
    }

    // Method to handle command execution
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // Allow console command sender
            if (!(sender instanceof ConsoleCommandSender ccs)) return true;
            if (args.length > 0) executeArgs(ccs, args);
            else execute(ccs);
        } else {
            Player p = (Player) sender;
            // Check for player permissions

            if (args.length > 0) executeArgs(p, args);
            else if (permission() != null && !sender.hasPermission(permission())) {
                sender.sendMessage(permissionMessage());
                return true;
            }
            else execute(p);

            return true;
        }

        return false;
    }

    // Methods to provide default command attributes (can be overridden)
   

    public boolean onlyPlayer() {
        return true;
    }

    

    // Method to get the singleton TabHandler instance
    public TabHandler getTabHandler() {
        return tabHandler;
    }

    // Methods to manage argument classes
    private ArgManager getArgumentClass(String arg) {
        return arguments.get(arg);
    }

    private boolean containsArgument(String arg) {
        return arguments.containsKey(arg);
    }

    private void addArguments(String command, ArgManager arg) {
        this.arguments.put(command, arg);
    }

    public ArgManager getDynamicArgumentClass() {
        return dynamicArguments;
    }

    private void addDynamicArgument(ArgManager dynamicArguments) {
        this.dynamicArguments = dynamicArguments;
    }

    public HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> getTabCompletes() {
        return tabCompletes;
    }

    // Method to register the command
    @Override
    public void register() {
        CotaCore.INSTANCE.getUserPlugin().getCommand(command()).setExecutor(this);
        TabHandler th = getTabHandler();
        HashMap<Integer, HashMap<ArrayList<String>, ArrayList<String>>> fixedTabCompletes =CommandUtils.getFixedTabCompletes(tabCompletes);
        for (Integer keys: fixedTabCompletes.keySet()) {
            for (ArrayList<String> tabs : fixedTabCompletes.get(keys).keySet()) {
                th.addTabComplete(keys, tabs, fixedTabCompletes.get(keys).get(tabs));
            }
        }
        CotaCore.INSTANCE.getUserPlugin().getCommand(command()).setTabCompleter(getTabHandler());










    }


    @Override
    public String permission() {
        return null;
    }

    @Override
    public String permissionMessage() {
        return "No permission!";
    }

    @Override
    public String usage() {
        return "Usage: /command";
    }

    @Override
    public String command() {
        return null;
    }
}