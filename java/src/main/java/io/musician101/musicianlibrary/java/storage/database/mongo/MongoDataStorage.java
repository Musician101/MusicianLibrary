package io.musician101.musicianlibrary.java.storage.database.mongo;

import com.google.common.base.Strings;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.musician101.musicianlibrary.java.storage.database.DatabaseStorage;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDataStorage<V> extends DatabaseStorage<V> {

    private final String uri;
    private MongoDatabase mongoDatabase;
    private final String table;
    private final MongoSerializable<V> serializable;
    private final Function<V, Bson> filters;

    public MongoDataStorage(@Nonnull Map<String, String> options, @Nonnull MongoSerializable<V> serializable, @Nonnull Function<V, Bson> filters) {
        super(options);
        this.uri = options.get("mongo_uri");
        this.table = options.get("table");
        this.serializable = serializable;
        this.filters = filters;
    }

    @Override
    @Nonnull
    public Map<String, Exception> load() {
        MongoClient mongoClient;
        if (!Strings.isNullOrEmpty(uri)) {
            mongoClient = MongoClients.create(uri);
        }
        else {
            MongoCredential credential = null;
            if (!Strings.isNullOrEmpty(username)) {
                credential = MongoCredential.createCredential(username, address, password.toCharArray());
            }

            String[] addressSplit = address.split(":");
            String host = addressSplit[0];
            int port = addressSplit.length > 1 ? Integer.parseInt(addressSplit[1]) : 27017;
            ServerAddress address = new ServerAddress(host, port);
            if (credential == null) {
                mongoClient = MongoClients.create(uri);
            }
            else {
                mongoClient = MongoClients.create(MongoClientSettings.builder().applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(address))).credential(MongoCredential.createCredential(username, database, password.toCharArray())).build());
            }
        }

        mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> c = mongoDatabase.getCollection(table);
        c.find().forEach(d -> data.add(serializable.deserialize(d)));
        mongoClient.close();
        return Collections.emptyMap();
    }

    @Override
    @Nonnull
    public Map<String, Exception> save() {
        MongoCollection<Document> c = mongoDatabase.getCollection(table);
        data.forEach(entry -> c.replaceOne(filters.apply(entry), serializable.serialize(entry)));
        return Collections.emptyMap();
    }
}
