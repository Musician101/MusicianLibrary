package io.musician101.musicianlibrary.java.minecraft.command;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommandUsage<M, A extends AbstractCommandArgument> {
    private final int minArgs;
    private final M usage;

    @SafeVarargs
    protected AbstractCommandUsage(A... arguments) {
        this(0, arguments);
    }

    @SafeVarargs
    protected AbstractCommandUsage(int minArgs, A... arguments) {
        this.usage = parseUsage(Arrays.asList(arguments));
        this.minArgs = minArgs;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public M getUsage() {
        return usage;
    }

    protected abstract M parseUsage(List<A> arguments);
}
