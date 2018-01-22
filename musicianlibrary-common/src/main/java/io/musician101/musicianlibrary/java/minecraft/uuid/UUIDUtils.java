package io.musician101.musicianlibrary.java.minecraft.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UUIDUtils {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(MinecraftProfile.class, new MinecraftProfileTypeAdapter()).create();

    private UUIDUtils() {

    }

    @Nullable
    public static String getNameOf(UUID uuid) throws IOException {
        return getNames(Collections.singletonList(uuid)).get(uuid);
    }

    @Nonnull
    public static Map<UUID, String> getNames(List<UUID> uuids) throws IOException {
        Map<UUID, String> map = new HashMap<>();
        for (UUID uuid : uuids) {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile" + uuid.toString().replace("-", "")).openConnection();
            JsonObject response = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
            String name = response.has("name") ? response.get("name").getAsString() : null;
            if (name == null) {
                continue;
            }

            String cause = response.has("cause") ? response.get("cause").getAsString() : null;
            String errorMessage = response.has("errorMessage") ? response.get("errorMessage").getAsString() : null;
            if (cause != null && cause.length() > 0) {
                throw new IllegalStateException(errorMessage);
            }

            map.put(uuid, name);
        }

        return map;
    }

    @Nullable
    public static UUID getUUIDOf(String name) throws IOException {
        return getUUIDs(Collections.singletonList(name)).get(name.toLowerCase());
    }

    @Nonnull
    public static Map<String, UUID> getUUIDs(List<String> names) throws IOException {
        Map<String, UUID> map = new HashMap<>();
        int requests = (int) Math.ceil(names.size() / 100D);
        for (int i = 0; i < requests; i++) {
            URL url = new URL("https://api.mojang.com/profiles/minecraft");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            StreamSupport.stream(new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonArray.class).spliterator(), false).map(profile -> GSON.fromJson(profile, MinecraftProfile.class)).forEach(profile -> map.put(profile.getName(), profile.getUUID()));
        }

        return map;
    }

    static class MinecraftProfile {

        private final String name;
        private final UUID uuid;

        public MinecraftProfile(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public UUID getUUID() {
            return uuid;
        }
    }
}
