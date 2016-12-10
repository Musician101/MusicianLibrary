package io.musician101.musicianlibrary.java.minecraft.command;

import io.musician101.musicianlibrary.java.minecraft.MLResettableBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class AbstractCommandUsage<A extends AbstractCommandArgument<M>, M> {

    private int minArgs;
    private M usage;

    public int getMinArgs() {
        return minArgs;
    }

    protected void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public M getUsage() {
        return usage;
    }

    protected void setUsage(M usage) {
        this.usage = usage;
    }

    protected static abstract class AbstractCommandUsageBuilder<A extends AbstractCommandArgument<M>, B extends AbstractCommandUsageBuilder<A, B, M, T>, M, T extends AbstractCommandUsage<A, M>> implements MLResettableBuilder<T, B> {

        protected int minArgs = 0;
        protected List<A> usage = new ArrayList<>();

        @Nonnull
        public abstract B addArgument(@Nonnull A argument);

        @Nonnull
        protected abstract M parseUsage(@Nonnull List<A> arguments);

        @Nonnull
        public abstract B setMinArgs(int minArgs);
    }
}
