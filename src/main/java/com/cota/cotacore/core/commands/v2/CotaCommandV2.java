package com.cota.cotacore.core.commands.v2;

import com.cota.cotacore.CotaCore;
import com.cota.cotacore.core.commands.v2.processor.ExecuteType;
import com.cota.cotacore.core.commands.v2.processor.ExecuteTypes;
import com.cota.cotacore.core.commands.v2.processor.ProcessUtils;
import com.cota.cotacore.core.commands.v2.processor.Setup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CotaCommandV2 implements CommandExecutor, TabCompleter {

    private HashMap<Integer, List<Method>> methods = new HashMap<>();
    private HashMap<String, String> req_arg_for_arg = new HashMap<>();
    private HashMap<Integer, List<String>> tabCompletes = new HashMap<>();
    private HashMap<String, String> arg_permission = new HashMap<>();
    private HashMap<String, ExecuteTypes> arg_type = new HashMap<>();
    private ExecuteTypes type;
    private String main_perm;
    private String main_usage;
    private String perm_message;
    public String cmd;
    public String[] args;
    public Player p;
    public CommandSender cs;
    public CotaCommandV2(String cmd) {
        this.cmd = cmd;

        if (this.getClass().isAnnotationPresent(ExecuteType.class)) {
            type = this.getClass().getAnnotation(ExecuteType.class).value();

        }
        if (this.getClass().isAnnotationPresent(Setup.class)) {
            main_perm = this.getClass().getAnnotation(Setup.class).permission();
            main_usage = this.getClass().getAnnotation(Setup.class).usage();
            perm_message = this.getClass().getAnnotation(Setup.class).permissionMessage();
        }else {
            Bukkit.getLogger().warning("Couldn't register command " + cmd + " because it is not setup properly! (Maybe @Setup is missing?)");
            return;
        }

        CotaCore.INSTANCE.getUserPlugin().getCommand(cmd).setExecutor(this);
        CotaCore.INSTANCE.getUserPlugin().getCommand(cmd).setTabCompleter(this);
        setupMethods();

    }

    public void onCommand(){

    }

    private void commandHandler(){
        if (type != null) {
           if (!handleExecuteType(type)) {
               return;
           }
        }



        if (args.length > 0) {

            if (methods.containsKey(args.length)) {
                boolean found_arg = false;
                int current_arg = args.length;

                for (Method method : methods.get(current_arg)) {

                    //Getting the current latest argument
                    if (args[current_arg-1].equals(method.getName())) {

                        found_arg = true;
                        //Checking permisison


                        //checking the type
                        if (arg_type.containsKey(method.getName())) {
                            if (!handleExecuteType(arg_type.get(method.getName()))) {
                                return;
                            }
                        }

                        if (arg_permission.containsKey(method.getName())) {
                            if (cs instanceof Player p) {
                                if (!p.hasPermission(arg_permission.get(method.getName()))) {
                                    tell(perm_message);
                                    return;
                                }
                            }

                        }

                        //Checking if a previous ARGUMENT is required for this current ARGUMENT
                        if (req_arg_for_arg.containsKey(method.getName())) {
                            //If the current argument requires an arg, but it is the first argument then executing the method (Non sense)
                            if (current_arg-2 <0) {
                                ProcessUtils.invokeMethod(method, this);
                                return;
                            }
                            //If the current argument requires an arg, and it's a match then executing the method (Success)
                            if (req_arg_for_arg.get(method.getName()).equals(args[current_arg-2])) {
                                ProcessUtils.invokeMethod(method, this);

                            }
                        }else {
                            //If the current argument doesn't require an arg, then executing the method (Success)
                            ProcessUtils.invokeMethod(method, this);
                        }

                    }
                }

                if (!found_arg) {
                    tell(main_usage);
                }

            }


        }else {
            onCommand();
        }
    }



    private void setupMethods() {
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Argument.class)) {
                Argument arg = m.getAnnotation(Argument.class);
                List<Method> methods = this.methods.getOrDefault(arg.value(), new ArrayList<>());
                if (!methods.contains(m)) methods.add(m);
                this.methods.put(arg.value(), methods);
                if (!arg.permission().isEmpty()) {
                    this.arg_permission.put(m.getName(), arg.permission());

                }
                this.arg_type.put(m.getName(), arg.type());



                if (!arg.requiredMethod().isEmpty()) {
                    req_arg_for_arg.put(m.getName(), arg.requiredMethod());
                }

                List<String> tabs = this.tabCompletes.getOrDefault(arg.value(), new ArrayList<>());
                if (!tabs.contains(m.getName())) {
                    tabs.add(m.getName());
                }
                tabCompletes.put(arg.suggest() ? arg.value() : 0, tabs);


            }
        }
    }






    private boolean handleExecuteType(ExecuteTypes type) {

        if (type == ExecuteTypes.CONSOLE && cs instanceof Player p) {
            tell("Only CONSOLE can execute this command!");
            return false;
        }else if (type == ExecuteTypes.PLAYER && cs instanceof ConsoleCommandSender) {
            tell("Only PLAYERS can execute this command!");
            return false;
        }

        return true;


    }

    public void tell(String m) {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equals(this.cmd)) {
            this.args = args;
            this.cs = commandSender;
            if (commandSender instanceof Player cp) {
                this.p=cp;
                if (!main_perm.equals("")) {
                    if (!cp.hasPermission(main_perm)) {
                        tell(perm_message);
                        return true;
                    }
                }
            }
            commandHandler();
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length > 0) {

            List<String> tabs = new ArrayList<>();
           // if (tabCompletes.containsKey(args.length)) {
            if (tabCompletes.containsKey(args.length)) {
                for (String tab : tabCompletes.get(args.length)) {
                    if (req_arg_for_arg.containsKey(tab)) {
                        String arg = req_arg_for_arg.get(tab);
                        if (arg.equals(args[args.length -2])) {
                            tabs.add(tab);

                        }
                    }else {
                        tabs.add(tab);

                    }
                }
            }







            //}
            return tabs;


        }
        return new ArrayList<>();
    }
}
