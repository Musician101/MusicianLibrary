package io.musician101.musicianlibrary.java.storage.database.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;

public class SQLiteDataStorage<V> extends SQLDataStorage<V> {

    @Nonnull
    private final File configDir;

    public SQLiteDataStorage(@Nonnull File configDir, @Nonnull Function<Statement, List<V>> deserializer, @Nonnull Function<List<V>, List<String>> serializer) {
        super(Collections.emptyMap(), deserializer, serializer);
        this.configDir = configDir;

    }

    @Nonnull
    @Override
    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + configDir.getAbsolutePath() + "/storage.db");
    }
}
