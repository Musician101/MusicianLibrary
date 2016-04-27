package io.musician101.common.java.minecraft.command;

import java.util.List;

public abstract class AbstractCommand<I, M, U extends AbstractCommandUsage<M, A>, A extends AbstractCommandArgument<M>, P extends AbstractCommandPermissions<M>, C extends AbstractCommand<I, M, U, A, P, C, S>, S>
{
    protected final I plugin;
    protected final List<C> subCommands;
    protected final M description;
    protected final P permissions;
    protected final String name;
    protected final U usage;

    protected AbstractCommand(I plugin, String name, M description, U usage, P permissions, List<C> subCommands)
    {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permissions = permissions;
        this.subCommands = subCommands;
    }

    protected abstract boolean minArgsMet(S source, int argsLength, M message);

    public U getUsage()
    {
        return usage;
    }

    public List<C> getSubCommands()
    {
        return subCommands;
    }
}
