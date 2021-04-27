package io.musician101.musicianlibrary.java.storage.file;

import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public abstract class DataFileStorage<V> extends DataStorage<File, V> {

    @Nullable
    protected final TypeSerializerCollection typeSerializerCollection;
    @Nonnull
    protected final String extension;
    @Nonnull
    private final ConfigurateLoader loader;
    @Nonnull
    protected final File storageDir;
    @Nonnull
    protected final TypeToken<V> typeToken;

    protected DataFileStorage(@Nonnull File storageDir, @Nonnull ConfigurateLoader loader, @Nonnull String extension, @Nonnull TypeToken<V> typeToken) {
        this(storageDir, loader, extension, typeToken, null);
    }

    protected DataFileStorage(@Nonnull File storageDir, @Nonnull ConfigurateLoader loader, @Nonnull String extension, @Nonnull TypeToken<V> typeToken, @Nullable TypeSerializerCollection typeSerializerCollection) {
        this.storageDir = storageDir;
        this.loader = loader;
        this.extension = extension;
        this.typeToken = typeToken;
        this.typeSerializerCollection = typeSerializerCollection;
    }

    @Nonnull
    protected abstract Path getPath(V entry);

    @Nonnull
    @Override
    public Map<File, Exception> load() {
        File[] files = storageDir.listFiles();
        if (files == null) {
            return Collections.emptyMap();
        }

        Map<File, Exception> errors = new HashMap<>();
        Stream.of(files).filter(file -> file.getName().endsWith(extension)).forEach(file -> {
            try {
                data.add(loader.loader(file.toPath(), typeSerializerCollection).loadToReference().referenceTo(typeToken).get());
            }
            catch (IOException e) {
                errors.put(file, e);
            }
        });

        return errors;
    }

    @Nonnull
    @Override
    public Map<File, Exception> save() {
        Map<File, Exception> errors = new HashMap<>();
        data.forEach(entry -> {
            Path path = getPath(entry);
            try {
                loader.loader(path, typeSerializerCollection).loadToReference().referenceTo(typeToken).setAndSave(entry);
            }
            catch (IOException e) {
                errors.put(path.toFile(), e);
            }
        });

        return errors;
    }
}
