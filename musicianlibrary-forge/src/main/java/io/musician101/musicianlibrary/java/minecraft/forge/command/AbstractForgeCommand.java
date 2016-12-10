package io.musician101.musicianlibrary.java.minecraft.forge.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractForgeCommand<M> extends CommandBase {

    protected final M modInstance;
    private final Map<String, AbstractForgeCommand<M>> commands = new HashMap<>();
    private final String description;
    private final Map<String, ForgeCommandExecutor> executors = new HashMap<>();
    private final String name;
    private final ForgeCommandUsage usage;
    private ForgeCommandExecutor executor = (server, sender, args) -> {
    };

    protected AbstractForgeCommand(M modInstance, String name, String description, ForgeCommandUsage usage) {
        this.modInstance = modInstance;
        this.name = name;
        this.description = description;
        this.usage = usage;
        build();
    }

    protected void addArgument(String name, ForgeCommandExecutor executor) {
        executors.put(name, executor);
    }

    protected void addCommand(AbstractForgeCommand<M> command) {
        commands.put(command.getName(), command);
    }

    protected abstract void build();

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 0) {
            if (executors.containsKey(args[0]))
                executors.get(args[0]).execute(server, sender, shiftArgumentList(args));
            else if (commands.containsKey(args[0]))
                commands.get(args[0]).execute(server, sender, shiftArgumentArray(args));
        }
        else
            executor.execute(server, sender, shiftArgumentList(args));
    }

    public String getCommandHelpInfo(ICommandSender sender) {
        return getUsage(sender) + description;
    }

    public Map<String, AbstractForgeCommand<M>> getCommands() {
        return commands;
    }

    public Map<String, ForgeCommandExecutor> getExecutors() {
        return executors;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return usage.getUsage();
    }

    protected boolean minArgsMet(ICommandSender sender, int argsLength, ITextComponent message) {
        if (argsLength >= usage.getMinArgs())
            return true;

        sender.sendMessage(message);
        return false;
    }

    protected void sendMessages(ICommandSender sender, ITextComponent... messages) {
        Arrays.stream(messages).forEach(sender::sendMessage);
    }

    protected void setExecutor(ForgeCommandExecutor executor) {
        this.executor = executor;
    }

    protected String[] shiftArgumentArray(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty())
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }

    protected String[] shiftArgumentArray(List<String> args) {
        return shiftArgumentArray(args.toArray(new String[0]));
    }

    protected List<String> shiftArgumentList(String[] args) {
        return shiftArgumentList(Arrays.asList(args));
    }

    protected List<String> shiftArgumentList(List<String> args) {
        if (!args.isEmpty())
            args.remove(0);

        return args;
    }
}
