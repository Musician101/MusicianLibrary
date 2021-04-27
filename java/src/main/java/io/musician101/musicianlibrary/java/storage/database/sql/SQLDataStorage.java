package io.musician101.musicianlibrary.java.storage.database.sql;

import io.musician101.musicianlibrary.java.storage.database.DatabaseStorage;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;

public abstract class SQLDataStorage<V> extends DatabaseStorage<V> {

    @Nonnull
    protected final Function<Statement, List<V>> deserializer;
    @Nonnull
    protected final Function<List<V>, List<String>> serializer;

    public SQLDataStorage(@Nonnull Map<String, String> options, @Nonnull Function<Statement, List<V>> deserializer, @Nonnull Function<List<V>, List<String>> serializer) {
        super(options);
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    @Nonnull
    @Override
    public Map<String, Exception> load() {
        try {
            Connection connection = openConnection();
            Statement statement = connection.createStatement();
            data.addAll(deserializer.apply(statement));
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
            List<String> queries = serializer.apply(data);
            queries.forEach(s -> {
                try {
                    statement.addBatch(s);
                }
                catch (SQLException e) {
                    errors.put(s, e);
                }
            });

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
