package io.musician101.musicianlibrary.java.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DataStorage<S, V> {

    @Nonnull
    protected final List<V> data = new ArrayList<>();

    public void clear() {
        data.clear();
    }

    @Nonnull
    public Optional<V> getEntry(Predicate<V> filter) {
        return data.stream().filter(filter).findFirst();
    }

    @Nonnull
    public List<V> getData() {
        return data;
    }

    public void addEntry(@Nonnull V entry) {
        addEntry(entry, null);
    }

    public void addEntry(@Nonnull V entry, @Nullable Predicate<V> replaceFilter) {
        if (replaceFilter != null) {
            data.removeIf(replaceFilter);
        }

        data.add(entry);
    }

    @Nonnull
    public abstract Map<S, Exception> load();

    @Nonnull
    public abstract Map<S, Exception> save();
}
