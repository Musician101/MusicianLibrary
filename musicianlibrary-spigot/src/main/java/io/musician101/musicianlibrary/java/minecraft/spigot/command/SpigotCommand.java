package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCommand<I extends JavaPlugin> {

    private final BiFunction<CommandSender, List<String>, Boolean> biFunction;
    private final String description;
    private final String name;
    private final SpigotCommandPermissions permissions;
    private final I plugin;
    private final Map<String, SpigotCommand<I>> subCommands;
    private final SpigotCommandUsage usage;

    SpigotCommand(I plugin, BiFunction<CommandSender, List<String>, Boolean> biFunction, Map<String, SpigotCommand<I>> subCommands, SpigotCommandPermissions permissions, SpigotCommandUsage usage, String description, String name) {
        this.plugin = plugin;
        this.biFunction = biFunction;
        this.subCommands = subCommands;
        this.permissions = permissions;
        this.usage = usage;
        this.description = description;
        this.name = name;
    }

    public static <I extends JavaPlugin> SpigotCommandBuilder<I> builder() {
        return new SpigotCommandBuilder<>();
    }

    private boolean execute(@Nonnull String arg, @Nonnull CommandSender sender, @Nonnull List<String> args) {
        return subCommands.containsKey(arg) && subCommands.get(arg).execute(sender, shiftArguments(args));
    }

    public boolean execute(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        if (!permissions.testPermissions(sender)) {
            return false;
        }

        if (!(usage.getMinArgs() < args.size())) {
            return false;
        }

        if (args.size() > 0) {
            String arg = args.get(0);
            if (arg.equalsIgnoreCase("help")) {
                return getHelpCommand(plugin).execute(sender, Collections.emptyList());
            }

            return execute(args.get(0), sender, shiftArguments(args));
        }

        return biFunction.apply(sender, args);
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public String getHelp() {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    private SpigotCommand<I> getHelpCommand(I plugin) {
        return SpigotCommand.<I>builder().name("help").description("Display help info for " + ChatColor.stripColor(getUsage())).usage(SpigotCommandUsage.of(SpigotCommandArgument.of(ChatColor.stripColor(getUsage())), SpigotCommandArgument.of("help"))).permissions(SpigotCommandPermissions.blank()).function((sender, args) -> {
            sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.GREEN + " =====");
            sender.sendMessage(getHelp());
            getSubCommands().values().forEach(command -> sender.sendMessage(command.getHelp()));
            return true;
        }).build(plugin);
    }

    public String getName() {
        return name;
    }

    @Nonnull
    public Map<String, SpigotCommand<I>> getSubCommands() {
        return subCommands;
    }

    @Nonnull
    public String getUsage() {
        return usage.getUsage();
    }

    private List<String> shiftArguments(@Nonnull List<String> args) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> shiftedArgs = new ArrayList<>(args);
        shiftedArgs.remove(0);
        return shiftedArgs;
    }
}
