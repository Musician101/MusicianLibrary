package io.musician101.musicianlibrary.java.storage.database.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import javax.annotation.Nonnull;

public class SQLiteDataStorage<V> extends SQLDataStorage<V> {

    @Nonnull
    private final File file;

    public SQLiteDataStorage(@Nonnull File file, @Nonnull SQLStatementSerializable<V> serializable) {
        super(Collections.emptyMap(), serializable);
        this.file = file;

    }

    @Nonnull
    @Override
    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
    }
}
