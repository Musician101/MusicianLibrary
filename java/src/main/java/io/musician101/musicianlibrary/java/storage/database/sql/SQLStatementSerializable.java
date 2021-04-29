package io.musician101.musicianlibrary.java.storage.database.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.annotation.Nonnull;

public interface SQLStatementSerializable<T> {

    @Nonnull
    List<T> fromStatement(@Nonnull Statement statement) throws SQLException;

    void toStatement(@Nonnull Statement statement, @Nonnull List<T> obj) throws SQLException;
}
