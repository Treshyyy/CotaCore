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
import java.util.Map;

public class CotaCommandV2 implements CommandExecutor, TabCompleter {

    public static class CommandMetadata {
        String permission;
        ExecuteTypes type;
        String requiredMethod;
        boolean suggest;

        public CommandMetadata(String permission, ExecuteTypes type, String requiredMethod, boolean suggest) {
            this.permission = permission;
            this.type = type;
            this.requiredMethod = requiredMethod;
            this.suggest = suggest;
        }
    }

    private final Map<Integer, List<Method>> methods = new HashMap<>();
    private final Map<String, CommandMetadata> argumentMeta = new HashMap<>();
    public final Map<Integer, List<String>> tabCompletes = new HashMap<>();

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
            Setup setup = this.getClass().getAnnotation(Setup.class);
            main_perm = setup.permission();
            main_usage = setup.usage();
            perm_message = perm_message();
        } else {
            Bukkit.getLogger().warning("Couldn't register command " + cmd + " because it is not setup properly! (Maybe @Setup is missing?)");
            return;
        }

        CotaCore.INSTANCE.getUserPlugin().getCommand(cmd).setExecutor(this);
        CotaCore.INSTANCE.getUserPlugin().getCommand(cmd).setTabCompleter(this);
        setupMethods();
    }

    public void onCommand() {}

    public String perm_message() {
        return "You don't have permission to do tha!";
    }

    private void setupMethods() {
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Argument.class)) {
                Argument arg = m.getAnnotation(Argument.class);
                int index = arg.value();
                methods.computeIfAbsent(index, k -> new ArrayList<>()).add(m);
                argumentMeta.put(m.getName(), new CommandMetadata(arg.permission(), arg.type(), arg.requiredMethod(), arg.suggest()));
                if (arg.suggest()) {
                    tabCompletes.computeIfAbsent(index, k -> new ArrayList<>()).add(m.getName());
                }
            }
        }
    }

    private boolean handleExecuteType(ExecuteTypes type) {
        if (type == ExecuteTypes.CONSOLE && cs instanceof Player) {
            tell("Only CONSOLE can execute this command!");
            return false;
        } else if (type == ExecuteTypes.PLAYER && cs instanceof ConsoleCommandSender) {
            tell("Only PLAYERS can execute this command!");
            return false;
        }
        return true;
    }

    private void commandHandler() {
        if (type != null && !handleExecuteType(type)) return;

        if (args.length > 0) {
            for (int i = args.length; i > 0; i--) {
                String argName = args[i - 1];
                List<Method> methodList = methods.get(i);
                if (methodList == null) continue;

                for (Method m : methodList) {
                    if (!m.getName().equalsIgnoreCase(argName)) continue;

                    CommandMetadata meta = argumentMeta.get(m.getName());
                    if (meta == null) continue;

                    if (meta.type != null && !handleExecuteType(meta.type)) return;

                    if (meta.permission != null && !meta.permission.isEmpty() && cs instanceof Player player) {
                        if (!player.hasPermission(meta.permission)) {
                            tell(perm_message);
                            return;
                        }
                    }

                    if (meta.requiredMethod != null && !meta.requiredMethod.isEmpty()) {
                        if (i - 2 >= 0 && meta.requiredMethod.equalsIgnoreCase(args[i - 2])) {
                            ProcessUtils.invokeMethod(m, this);
                            return;
                        }
                    } else {
                        ProcessUtils.invokeMethod(m, this);
                        return;
                    }
                }
            }
            tell(main_usage);
        } else {
            onCommand();
        }
    }

    public void tell(String m) {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase(this.cmd)) {
            this.args = args;
            this.cs = commandSender;
            if (commandSender instanceof Player player) {
                this.p = player;
                if (main_perm != null && !main_perm.isEmpty() && !player.hasPermission(main_perm)) {
                    tell(perm_message);
                    return true;
                }
            }
            commandHandler();
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length > 0) {
            List<String> possibleTabs = tabCompletes.get(args.length);
            if (possibleTabs != null) {
                for (String tab : possibleTabs) {
                    CommandMetadata meta = argumentMeta.get(tab);
                    if (meta == null) continue;

                    if (meta.permission != null && !meta.permission.isEmpty() && commandSender instanceof Player player && !player.hasPermission(meta.permission)) {
                        continue;
                    }
                    if (meta.requiredMethod != null && !meta.requiredMethod.isEmpty()) {
                        if (args.length >= 2 && meta.requiredMethod.equalsIgnoreCase(args[args.length - 2])) {
                            tabs.add(tab);
                        }
                    } else {
                        tabs.add(tab);
                    }
                }
            }
        }
        return tabs;
    }
}
