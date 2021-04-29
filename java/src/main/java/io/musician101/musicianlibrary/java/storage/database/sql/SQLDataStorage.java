package io.musician101.musicianlibrary.java.storage.database.sql;

import io.musician101.musicianlibrary.java.storage.database.DatabaseStorage;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public abstract class SQLDataStorage<V> extends DatabaseStorage<V> {

    @Nonnull
    private final SQLStatementSerializable<V> serializable;

    public SQLDataStorage(@Nonnull Map<String, String> options, @Nonnull SQLStatementSerializable<V> serializable) {
        super(options);
        this.serializable = serializable;
    }

    @Nonnull
    @Override
    public Map<String, Exception> load() {
        try {
            Connection connection = openConnection();
            Statement statement = connection.createStatement();
            data.addAll(serializable.fromStatement(statement));
            statement.close();
            connection.close();
            return Collections.emptyMap();
        }
        catch (SQLException e) {
            return Collections.singletonMap(e.getMessage(), e);
        }
    }

    @Nonnull
    protected abstract Connection openConnection() throws SQLException;

    @Nonnull
    @Override
    public Map<String, Exception> save() {
        Map<String, Exception> errors = new HashMap<>();
        try {
            Connection connection = openConnection();
            Statement statement = connection.createStatement();
            serializable.toStatement(statement, data);
            statement.executeBatch();
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            errors.put(e.getMessage(), e);
        }

        return errors;
    }
}
