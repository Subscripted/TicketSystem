package de.lorenz.ticketsystem.json;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JsonObject extends JsonElement {

    Map<String, JsonElement> entries = new HashMap<>();

    public JsonElement get(String key) {
        if (!entries.containsKey(key)) {
            throw new IllegalStateException("'" + key + "' doesn't exist in: " + this);
        }
        return entries.get(key);
    }

    public void add(String key, JsonElement e) {
        entries.put(key, e);
    }

    public void addProperty(String key, Object value) {
        entries.put(key, value == null ? null : new JsonSimple(value));
    }

    public void addProperty(String key, String value) {
        addProperty(key, (Object) value);
    }

    public void addProperty(String key, int value) {
        addProperty(key, (Object) value);
    }

    public void addProperty(String key, double value) {
        addProperty(key, (Object) value);
    }

    public void addProperty(String key, long value) {
        addProperty(key, (Object) value);
    }

    public void addProperty(String key, float value) {
        addProperty(key, (Object) value);
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<>(entries.keySet());
    }

    @Override
    public String toString() {
        return entries.entrySet().stream()
                .map(e -> "\"" + e.getKey() + "\":" + e.getValue())
                .collect(Collectors.joining(",", "{", "}"));
    }

    @Override
    public JsonElement copy() {
        JsonObject clone = new JsonObject();
        entries.forEach(clone::add);
        return clone;
    }

    @Override
    public boolean contains(String key) {
        return entries.containsKey(key);
    }
}
