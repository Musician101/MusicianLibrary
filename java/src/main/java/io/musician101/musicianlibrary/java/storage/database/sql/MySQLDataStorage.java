package io.musician101.musicianlibrary.java.storage.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;

public class MySQLDataStorage<V> extends SQLDataStorage<V> {

    public MySQLDataStorage(@Nonnull Map<String, String> options, @Nonnull Function<Statement, List<V>> deserializer, @Nonnull Function<List<V>, List<String>> serializer) {
        super(options, deserializer, serializer);
    }

    @Nonnull
    @Override
    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, username, password);
    }
}
