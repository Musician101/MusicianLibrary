package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.spigot.command.SpigotCommandArgument.Syntax;
import io.musician101.musicianlibrary.java.minecraft.util.Builder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SpigotCommandArgumentBuilder implements Builder<SpigotCommandArgumentBuilder, SpigotCommandArgument> {

    private String name;
    private List<Syntax> syntaxList = new ArrayList<>();

    SpigotCommandArgumentBuilder() {

    }

    @Nonnull
    public SpigotCommandArgumentBuilder addSyntax(@Nonnull Syntax syntax) {
        syntaxList.add(syntax);
        return this;
    }

    @Nonnull
    @Override
    public SpigotCommandArgument build() throws IllegalStateException {
        checkArgument(!syntaxList.isEmpty(), "Syntax list can not be empty.");
        checkArgument(!(syntaxList.contains(Syntax.REQUIRED) && syntaxList.contains(Syntax.OPTIONAL)), "Command arguments cannot be required AND optional.");
        checkNotNull(name, "Name cannot be null.");
        return new SpigotCommandArgument(name, syntaxList);
    }

    @Nonnull
    public SpigotCommandArgumentBuilder name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    @Nonnull
    @Override
    public SpigotCommandArgumentBuilder reset() {
        name = null;
        syntaxList = new ArrayList<>();
        return this;
    }
}
