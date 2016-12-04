package io.musician101.musicianlibrary.java.minecraft.forge.command;

import io.musician101.musicianlibrary.java.util.TriConsumer;
import io.musician101.musicianlibrary.java.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractForgeCommand<M> extends CommandBase {
    protected final M modInstance;
    private final String description;
    private final String name;
    private final List<AbstractForgeCommand> subCommands = new ArrayList<>();
    private final ForgeCommandUsage usage;
    private TriConsumer<MinecraftServer, ICommandSender, List<String>> triConsumer = (server, sender, args) -> {};

    protected AbstractForgeCommand(M modInstance, String name, String description, ForgeCommandUsage usage) {
        this.modInstance = modInstance;
        this.name = name;
        this.description = description;
        this.usage = usage;
        build();
    }

    protected void addArgument(AbstractForgeCommand command) {
        subCommands.add(command);
    }

    protected abstract void build();

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 0) {
            if ("help".equalsIgnoreCase(args[0]))
                new ForgeHelpCommand<>(modInstance, sender, this).execute(server, sender, moveArguments(args));
            else
                subCommands.stream().filter(cmd ->
                        cmd.getName().equals(args[0])).collect(Utils.singletonCollector()).execute(server, sender, moveArguments(args));
        }
        else
            triConsumer.accept(server, sender, Arrays.asList(moveArguments(args)));
    }

    public String getCommandHelpInfo(ICommandSender sender) {
        return getUsage(sender) + description;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    public List<AbstractForgeCommand> getSubCommands() {
        return subCommands;
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

    protected String[] moveArguments(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty())
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }

    protected void setConsumer(TriConsumer<MinecraftServer, ICommandSender, List<String>> triConsumer) {
        this.triConsumer = triConsumer;
    }
}
