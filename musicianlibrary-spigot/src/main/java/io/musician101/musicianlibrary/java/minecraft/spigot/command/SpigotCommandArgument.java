package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;


public class SpigotCommandArgument {

    private String name;
    private List<Syntax> syntaxList;

    SpigotCommandArgument() {

    }

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

    public static SpigotCommandArgumentBuilder builder() {
        return new SpigotCommandArgumentBuilder();
    }

    public static SpigotCommandArgument of(@Nonnull String name, @Nonnull Syntax... syntaxes) {
        SpigotCommandArgumentBuilder builder = builder().name(name);
        Stream.of(syntaxes).forEach(builder::addSyntax);
        return builder.build();
    }

    public static SpigotCommandArgument of(@Nonnull String name) {
        return builder().name(name).build();
    }

    @Nonnull
    public String format() {
        String name = this.name;
        if (syntaxList.contains(Syntax.REPLACE))
            name = ChatColor.ITALIC + name;

        if (syntaxList.contains(Syntax.MULTIPLE))
            name = name + "...";

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = "[" + name + "]";

        if (syntaxList.contains(Syntax.REQUIRED))
            name = "<" + name + ">";

        return name;
    }

    public static class SpigotCommandArgumentBuilder {

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
        public SpigotCommandArgument build() throws IllegalStateException {
            if (name == null)
                throw new IllegalStateException("Name has not been set.");

            if (syntaxList.isEmpty())
                syntaxList.add(Syntax.LITERAL);

            if (syntaxList.contains(Syntax.REQUIRED) && syntaxList.contains(Syntax.OPTIONAL))
                throw new IllegalStateException("Common arguments cannot be both Optional and Required.");

            SpigotCommandArgument argument = new SpigotCommandArgument();
            argument.setName(name);
            argument.setSyntaxList(syntaxList);
            return argument;
        }

        @Nonnull
        public SpigotCommandArgumentBuilder reset() {
            name = null;
            syntaxList = new ArrayList<>();
            return this;
        }

        @Nonnull
        public SpigotCommandArgumentBuilder name(@Nonnull String name) {
            this.name = name;
            return this;
        }
    }
}
