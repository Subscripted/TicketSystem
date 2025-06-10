package de.lorenz.ticketsystem.json;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class JsonElement {

    public boolean getAsBoolean() {
        throw unsupported("getAsBoolean");
    }

    public int getAsInt() {
        throw unsupported("getAsInt");
    }

    public double getAsDouble() {
        throw unsupported("getAsDouble");
    }

    public float getAsFloat() {
        throw unsupported("getAsFloat");
    }

    public long getAsLong() {
        throw unsupported("getAsLong");
    }

    public Number getAsNumber() {
        throw unsupported("getAsNumber");
    }

    public String getAsString() {
        throw unsupported("getAsString");
    }

    public Set<String> keySet() {
        throw unsupported("keySet");
    }

    public boolean contains(String key) {
        throw unsupported("contains");
    }

    public abstract JsonElement copy();

    @Override
    public String toString() {
        if (isJsonSimple()) return getAsJsonSimple().toString();
        if (isJsonArray()) return getAsJsonArray().toString();
        if (isJsonObject()) return getAsJsonObject().toString();
        throw new IllegalStateException("Element is not a valid JSON type");
    }

    public boolean isJsonObject() {
        return this instanceof JsonObject;
    }

    public JsonObject getAsJsonObject() {
        if (!isJsonObject()) throw unsupportedCast("JsonObject");
        return (JsonObject) this;
    }

    public boolean isJsonSimple() {
        return this instanceof JsonSimple;
    }

    public JsonSimple getAsJsonSimple() {
        if (!isJsonSimple()) throw unsupportedCast("JsonSimple");
        return (JsonSimple) this;
    }

    public boolean isJsonArray() {
        return this instanceof JsonArray;
    }

    public JsonArray getAsJsonArray() {
        if (!isJsonArray()) throw unsupportedCast("JsonArray");
        return (JsonArray) this;
    }

    private IllegalStateException unsupported(String operation) {
        return new IllegalStateException("Operation '" + operation + "' not supported by: " + getClass().getSimpleName());
    }

    private IllegalStateException unsupportedCast(String expected) {
        return new IllegalStateException("Expected " + expected + " but was: " + getClass().getSimpleName());
    }

    public JsonElement getByPath(JsonElement element, String path) {
        String[] keys = path.split("\\.");
        JsonElement current = element;

        for (String key : keys) {
            if (!current.isJsonObject()) {
                throw new IllegalArgumentException("Pfadteil '" + key + "' trifft auf kein Objekt.");
            }
            current = current.getAsJsonObject().get(key);
            if (current == null) {
                throw new IllegalArgumentException("Schlüssel '" + key + "' existiert nicht.");
            }
        }

        return current;
    }
}