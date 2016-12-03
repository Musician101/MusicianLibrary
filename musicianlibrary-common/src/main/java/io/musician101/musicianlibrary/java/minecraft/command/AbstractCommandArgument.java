package io.musician101.musicianlibrary.java.minecraft.command;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractCommandArgument<M> {
    protected final List<Syntax> syntaxList;
    protected String name;

    protected AbstractCommandArgument(String name) {
        this(name, Syntax.LITERAL);
    }

    protected AbstractCommandArgument(String name, Syntax... syntaxes) {
        this.syntaxList = Arrays.asList(syntaxes);
        if (syntaxList.contains(Syntax.REQUIRED) && syntaxList.contains(Syntax.OPTIONAL))
            throw new IllegalArgumentException("Common arguments cannot be both Optional and Required.");

        this.name = name;
    }

    public abstract M format();

    public enum Syntax {
        LITERAL,
        MULTIPLE,
        REPLACE,
        REQUIRED,
        OPTIONAL
    }
}
