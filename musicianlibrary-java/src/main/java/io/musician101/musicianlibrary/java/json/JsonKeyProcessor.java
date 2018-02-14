package io.musician101.musicianlibrary.java.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import io.musician101.musicianlibrary.java.json.adapter.AdapterOf;
import io.musician101.musicianlibrary.java.json.adapter.AdapterType;
import io.musician101.musicianlibrary.java.json.adapter.TypeOf;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.reflections.Reflections;

public class JsonKeyProcessor {

    public static final Gson GSON;
    private static final List<JsonKeyImpl<?, ?>> KEYS = new ArrayList<>();

    static {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Reflections reflections = new Reflections();
        reflections.getTypesAnnotatedWith(JsonKeyCatalog.class).forEach(aClass -> {
            List<Field> fields = Arrays.asList(aClass.getFields());
            Stream.of(aClass.getFields()).filter(field -> Modifier.isStatic(field.getModifiers())).map(field -> {
                try {
                    return field.get(null);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Error! Failed to parse an instance of JsonKey in the catalog " + aClass.getCanonicalName() + ". Index: " + fields.indexOf(field));
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).filter(JsonKeyImpl.class::isInstance).map(JsonKeyImpl.class::cast).forEach(jsonKey -> handleJsonKey(jsonKey, builder));
        });

        reflections.getTypesAnnotatedWith(JsonKey.class).forEach(aClass -> {
            JsonKey jsonKey = aClass.getAnnotation(JsonKey.class);
            Class<?> typeAdapter = jsonKey.typeAdapter();
            try {
                handleJsonKey(new JsonKeyImpl<>(jsonKey.key(), TypeToken.get(aClass), jsonKey.typeAdapter().newInstance()), builder);
            }
            catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Error! " + typeAdapter.getCanonicalName() + " does not have a valid no args constructor.");
            }
        });

        reflections.getTypesAnnotatedWith(JsonKeys.class).forEach(aClass -> {
            JsonKeys jsonKeys = aClass.getAnnotation(JsonKeys.class);
            Class<?> typeAdapter =jsonKeys.typeAdapter();
            Stream.of(jsonKeys.keys()).forEach(key -> {
                try {
                    handleJsonKey(new JsonKeyImpl<>(key, TypeToken.get(aClass), typeAdapter.newInstance()), builder);
                }
                catch (InstantiationException | IllegalAccessException e) {
                    System.out.println("Error! " + typeAdapter.getCanonicalName() + " does not have a valid no args constructor.");
                }
            });
        });

        reflections.getTypesAnnotatedWith(TypeOf.class).forEach(aClass -> {
            Class<?> typeAdapter = aClass.getAnnotation(TypeOf.class).value();
            try {
                builder.registerTypeAdapter(aClass, typeAdapter.newInstance());
            }
            catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Error! " + typeAdapter.getCanonicalName() + " does not have a valid no args constructor.");
            }
        });

        reflections.getTypesAnnotatedWith(AdapterOf.class).forEach(aClass -> {
            Optional<TypeToken<?>> adapterType = Stream.of(aClass.getFields()).filter(field -> Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(AdapterType.class)).map(field -> {
                try {
                    return (TypeToken<?>) field.get(null);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Error! Failed to get the value of " + field.getName() + " in " + aClass.getCanonicalName());
                    return (TypeToken<?>) null;
                }
            }).filter(Objects::nonNull).findFirst();
            if (!adapterType.isPresent()) {
                System.out.println("Error! " + aClass.getCanonicalName() + " has annotation " + AdapterOf.class.getCanonicalName() + " but does not contain a static field with annotation " + AdapterType.class.getCanonicalName());
                return;
            }

            try {
                builder.registerTypeAdapter(adapterType.get().getType(), aClass.newInstance());
            }
            catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Error! " + aClass.getCanonicalName() + " does not have a valid no args constructor.");
            }
        });

        GSON = builder.create();
    }

    private static void handleJsonKey(JsonKeyImpl jsonKey, GsonBuilder builder) {
        Optional typeAdapter = jsonKey.getTypeAdapter();
        if (typeAdapter.isPresent()) {
            if (!(typeAdapter.get() instanceof JsonSerializer<?> || typeAdapter.get() instanceof JsonDeserializer<?> || typeAdapter.get() instanceof InstanceCreator<?> || typeAdapter.get() instanceof TypeAdapter<?>)) {
                System.out.println("Warning: " + jsonKey.getKey() + " has an invalid type adapter. Ignore this if this is for a primitive type.");
            }

            builder.registerTypeAdapter(jsonKey.getTokenType().getType(), typeAdapter.get());
            KEYS.add(jsonKey);
        }
        else {
            System.out.println(jsonKey.getKey() + " does not have a type adapter. Skipping.");
        }
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <J extends JsonElement, V> Optional<JsonKeyImpl<J, V>> getJsonKey(String key) {
        return KEYS.stream().map(jsonKey -> {
            try {
                return (JsonKeyImpl<J, V>) jsonKey;
            }
            catch (ClassCastException e) {
                return null;
            }
        }).filter(Objects::nonNull).filter(jsonKey -> key.equals(jsonKey.getKey())).findFirst();
    }
}
