package io.musician101.musicianlibrary.java.minecraft.forge.command;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ForgeCommandExecutor {

    void execute(MinecraftServer server, ICommandSender sender, List<String> args) throws CommandException;
}
