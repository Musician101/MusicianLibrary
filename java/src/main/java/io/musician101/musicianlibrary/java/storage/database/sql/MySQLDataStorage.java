package io.musician101.musicianlibrary.java.storage.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import javax.annotation.Nonnull;

public class MySQLDataStorage<V> extends SQLDataStorage<V> {

    public MySQLDataStorage(@Nonnull Map<String, String> options, @Nonnull SQLStatementSerializable<V> serializable) {
        super(options, serializable);
    }

    @Nonnull
    @Override
    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, username, password);
    }
}
