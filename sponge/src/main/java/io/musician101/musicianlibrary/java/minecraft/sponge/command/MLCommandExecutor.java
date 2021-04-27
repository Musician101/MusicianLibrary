package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.plugin.PluginContainer;

public abstract class MLCommandExecutor<P extends PluginContainer> implements CommandExecutor {

    protected abstract P getPlugin();
}
