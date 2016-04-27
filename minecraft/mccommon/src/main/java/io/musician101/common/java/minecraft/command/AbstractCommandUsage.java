package io.musician101.common.java.minecraft.command;

import java.util.List;

public abstract class AbstractCommandUsage<M, A extends AbstractCommandArgument>
{
    private int minArgs = 0;
    private M usage;

    protected AbstractCommandUsage(List<A> arguments)
    {
        this(arguments, 0);
    }

    protected AbstractCommandUsage(List<A> arguments, int minArgs)
    {
        this.usage = parseUsage(arguments);
        this.minArgs = minArgs;
    }

    public int getMinArgs()
    {
        return minArgs;
    }

    public M getUsage()
    {
        return usage;
    }

    protected abstract M parseUsage(List<A> arguments);
}
