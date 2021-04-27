package io.musician101.musicianlibrary.java.storage.database.mongo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.Document;

public interface MongoSerializable<T> {

    T deserialize(@Nullable Document document);

    Document serialize(@Nonnull T src);
}
