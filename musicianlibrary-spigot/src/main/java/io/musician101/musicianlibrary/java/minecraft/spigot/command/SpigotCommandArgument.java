package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

public class SpigotCommandArgument {

    private final String name;
    private final List<Syntax> syntaxList;

    SpigotCommandArgument(String name, List<Syntax> syntaxList) {
        this.name = name;
        this.syntaxList = syntaxList;
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
        if (syntaxList.contains(Syntax.REPLACE)) {
            name = ChatColor.ITALIC + name;
        }

        if (syntaxList.contains(Syntax.MULTIPLE)) {
            name = name + "...";
        }

        if (syntaxList.contains(Syntax.OPTIONAL)) {
            name = "[" + name + "]";
        }

        if (syntaxList.contains(Syntax.REQUIRED)) {
            name = "<" + name + ">";
        }

        return name;
    }

    public enum Syntax {
        LITERAL,
        MULTIPLE,
        REPLACE,
        REQUIRED,
        OPTIONAL
    }
}
