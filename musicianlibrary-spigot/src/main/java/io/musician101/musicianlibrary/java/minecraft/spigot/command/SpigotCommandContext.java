package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public final class SpigotCommandContext {

    private final Multimap<String, Object> parsedArgs;

    public SpigotCommandContext() {
        this.parsedArgs = ArrayListMultimap.create();
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getAll(String key) {
        return Collections.unmodifiableCollection((Collection<? extends T>) parsedArgs.get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOne(String key) {
        Collection<Object> values = parsedArgs.get(key);
        if (values.size() != 1) {
            return Optional.empty();
        }

        return Optional.ofNullable((T) values.iterator().next());
    }

    public void putArg(String key, Object value) {
        parsedArgs.put(key, value);
    }

    public boolean checkPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

    public boolean hasAny(String key) {
        return parsedArgs.containsKey(key);
    }
}
