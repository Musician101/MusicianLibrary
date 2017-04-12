package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class MLCommandExecutor<P extends PluginContainer> implements CommandExecutor {

    protected abstract P getPlugin();
}
