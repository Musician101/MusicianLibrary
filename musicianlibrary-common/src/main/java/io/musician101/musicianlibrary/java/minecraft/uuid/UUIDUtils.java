package io.musician101.musicianlibrary.java.minecraft.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class UUIDUtils {

    private UUIDUtils() {

    }

    public static String getNameOf(UUID uuid) throws IOException {
        return getNames(Collections.singletonList(uuid)).get(uuid);
    }

    public static Map<UUID, String> getNames(List<UUID> uuids) throws IOException {
        Map<UUID, String> map = new HashMap<>();
        for (UUID uuid : uuids) {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile" + uuid.toString().replace("-", "")).openConnection();
            JsonObject response = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
            String name = response.has("name") ? response.get("name").getAsString() : null;
            if (name == null)
                continue;

            String cause = response.has("cause") ? response.get("cause").getAsString() : null;
            String errorMessage = response.has("errorMessage") ? response.get("errorMessage").getAsString() : null;
            if (cause != null && cause.length() > 0)
                throw new IllegalStateException(errorMessage);

            map.put(uuid, name);
        }

        return map;
    }

    public static UUID getUUIDOf(String name) throws IOException {
        return getUUIDs(Collections.singletonList(name)).get(name.toLowerCase());
    }

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
            List objects = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), List.class);
            List<MinecraftProfile> profiles = new ArrayList<>();
            for (Object object : objects)
                profiles.add(new GsonBuilder().registerTypeHierarchyAdapter(MinecraftProfile.class, new MinecraftProfileTypeAdapter()).create().fromJson(object.toString(), MinecraftProfile.class));

            profiles.forEach(profile -> map.put(profile.getName(), profile.getUUID()));
        }

        return map;
    }

    public static class MinecraftProfile {

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
