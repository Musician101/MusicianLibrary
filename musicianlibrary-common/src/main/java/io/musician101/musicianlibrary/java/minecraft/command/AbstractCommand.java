package io.musician101.musicianlibrary.java.minecraft.command;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractCommand<M, U extends AbstractCommandUsage, P extends AbstractCommandPermissions, C extends AbstractCommand, S>
{
    protected final List<C> subCommands;
    protected final M description;
    protected final P permissions;
    protected final String name;
    protected final U usage;

    protected AbstractCommand(String name, M description, U usage, P permissions, List<C> subCommands)
    {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permissions = permissions;
        this.subCommands = subCommands;
    }

    @SuppressWarnings("unused")
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
