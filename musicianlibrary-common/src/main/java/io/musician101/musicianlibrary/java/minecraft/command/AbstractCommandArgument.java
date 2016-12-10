package io.musician101.musicianlibrary.java.minecraft.command;

import io.musician101.musicianlibrary.java.minecraft.MLResettableBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;


public abstract class AbstractCommandArgument<M> {

    protected String name;
    protected List<Syntax> syntaxList;

    @Nonnull
    public abstract M format();

    protected void setName(String name) {
        this.name = name;
    }

    protected void setSyntaxList(List<Syntax> syntaxList) {
        this.syntaxList = syntaxList;
    }

    public enum Syntax {
        LITERAL,
        MULTIPLE,
        REPLACE,
        REQUIRED,
        OPTIONAL
    }

    protected static abstract class AbstractCommandArgumentBuilder<A, B extends AbstractCommandArgumentBuilder<A, B, M>, M> implements MLResettableBuilder<A, B> {

        protected String name;
        protected List<Syntax> syntaxList = new ArrayList<>();

        @Nonnull
        public abstract B addSyntax(@Nonnull Syntax syntax);

        @Nonnull
        public abstract B setName(@Nonnull String name);
    }
}
