package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCommand<I extends JavaPlugin> {

    private BiFunction<CommandSender, List<String>, Boolean> biFunction;
    private String description;
    private String name;
    private SpigotCommandPermissions permissions;
    private Map<String, SpigotCommand<I>> subCommands;
    private SpigotCommandUsage usage;

    private SpigotCommand() {

    }

    private boolean execute(@Nonnull String arg, @Nonnull CommandSender sender, @Nonnull List<String> args) {
        return subCommands.containsKey(arg) && subCommands.get(arg).execute(sender, shiftArguments(args));
    }

    public boolean execute(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        if (!minArgsMet(args.size())) {
            return false;
        }

        if (args.size() > 0) {
            return execute(args.get(0), sender, shiftArguments(args));
        }

        return biFunction.apply(sender, args);
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    protected void setDescription(@Nonnull String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    protected void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public Map<String, SpigotCommand<I>> getSubCommands() {
        return subCommands;
    }

    protected void setSubCommands(@Nonnull Map<String, SpigotCommand<I>> subCommands) {
        this.subCommands = subCommands;
    }

    @Nonnull
    public String getUsage() {
        return usage.getUsage();
    }

    protected void setUsage(@Nonnull SpigotCommandUsage usage) {
        this.usage = usage;
    }

    protected boolean minArgsMet(int amount) {
        return usage.getMinArgs() < amount;
    }

    protected void setBiFunction(@Nonnull BiFunction<CommandSender, List<String>, Boolean> biFunction) {
        this.biFunction = biFunction;
    }

    protected void setPermissions(@Nonnull SpigotCommandPermissions permissions) {
        this.permissions = permissions;
    }

    protected List<String> shiftArguments(@Nonnull String... args) {
        return shiftArguments(Arrays.asList(args));
    }

    protected List<String> shiftArguments(@Nonnull List<String> args) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> shiftedArgs = new ArrayList<>(args);
        shiftedArgs.remove(0);
        return shiftedArgs;
    }

    protected boolean testPermissions(@Nonnull CommandSender sender) {
        return permissions.testPermissions(sender);
    }

    public static <I extends JavaPlugin> SpigotCommandBuilder<I> builder() {
        return new SpigotCommandBuilder<>();
    }

    @Nonnull
    public String getHelp() {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    @Nonnull
    protected SpigotCommand<I> getHelpCommand(I plugin) {
        return SpigotCommand.<I>builder().name("help").description("Display help info for " + ChatColor.stripColor(getUsage()))
                .usage(SpigotCommandUsage.of(SpigotCommandArgument.of(ChatColor.stripColor(getUsage())), SpigotCommandArgument.of("help")))
                .permissions(SpigotCommandPermissions.blank())
                .function((sender, args) -> {
                    sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.GREEN + " =====");
                    sender.sendMessage(getHelp());
                    getSubCommands().values().forEach(command -> sender.sendMessage(command.getHelp()));
                    return true;
                }).build();
    }

    public static class SpigotCommandBuilder<I extends JavaPlugin> {

        private BiFunction<CommandSender, List<String>, Boolean> biFunction = (sender, args) -> true;
        private String description;
        private String name;
        private SpigotCommandPermissions permissions;
        private Map<String, SpigotCommand<I>> subCommands = new HashMap<>();
        private SpigotCommandUsage usage;

        SpigotCommandBuilder() {

        }

        @Nonnull
        public SpigotCommandBuilder<I> addCommand(@Nonnull SpigotCommand<I> command) {
            subCommands.put(command.getName(), command);
            return this;
        }

        @Nonnull
        public SpigotCommand<I> build() throws IllegalStateException {
            SpigotCommand<I> sc = new SpigotCommand<>();
            if (description == null) {
                throw new IllegalStateException("Description has not been set.");
            }

            if (name == null) {
                throw new IllegalStateException("Name has not been set.");
            }

            if (permissions == null) {
                throw new IllegalStateException("Permissions have not been set.");
            }

            if (usage == null) {
                throw new IllegalStateException("Usage has not been set.");
            }

            sc.setDescription(description);
            sc.setName(name);
            sc.setPermissions(permissions);
            sc.setUsage(usage);
            sc.setSubCommands(subCommands);
            if (biFunction == null) {
                sc.setBiFunction((sender, args) -> {
                    sender.sendMessage(sc.getHelp());
                    return true;
                });
            }
            else {
                sc.setBiFunction(biFunction);
            }

            return sc;
        }

        @Nonnull
        public SpigotCommandBuilder<I> reset() {
            biFunction = (sender, args) -> true;
            description = null;
            name = null;
            permissions = null;
            subCommands = new HashMap<>();
            usage = null;
            return this;
        }

        @Nonnull
        public SpigotCommandBuilder<I> function(@Nonnull BiFunction<CommandSender, List<String>, Boolean> biFunction) {
            this.biFunction = biFunction;
            return this;
        }

        @Nonnull
        public SpigotCommandBuilder<I> description(@Nonnull String description) {
            this.description = description;
            return this;
        }

        @Nonnull
        public SpigotCommandBuilder<I> name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        @Nonnull
        public SpigotCommandBuilder<I> permissions(@Nonnull SpigotCommandPermissions permissions) {
            this.permissions = permissions;
            return this;
        }

        @Nonnull
        public SpigotCommandBuilder<I> usage(@Nonnull SpigotCommandUsage usage) {
            this.usage = usage;
            return this;
        }
    }
}
