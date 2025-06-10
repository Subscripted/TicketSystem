package de.lorenz.ticketsystem.json;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonSimple extends JsonElement {

    Object value;

    @Override
    public String getAsString() {
        return toString();
    }

    @Override
    public String toString() {
        return isPrimitive() ? value.toString() : "\"" + value + "\"";
    }

    private boolean isPrimitive() {
        return isBoolean() || isInt() || isFloat() || isDouble() || isLong();
    }

    @Override
    public boolean getAsBoolean() {
        return Boolean.parseBoolean(value.toString());
    }

    public boolean isBoolean() {
        return "true".equalsIgnoreCase(value.toString()) || "false".equalsIgnoreCase(value.toString());
    }

    @Override
    public Number getAsNumber() {
        if (isInt()) return getAsInt();
        if (isLong()) return getAsLong();
        if (isFloat()) return getAsFloat();
        if (isDouble()) return getAsDouble();
        throw new NumberFormatException("Unbekannter Zahlentyp: " + value);
    }

    @Override
    public int getAsInt() {
        return parseNumber(Integer::parseInt, "int");
    }

    @Override
    public float getAsFloat() {
        return parseNumber(Float::parseFloat, "float");
    }

    @Override
    public double getAsDouble() {
        return parseNumber(Double::parseDouble, "double");
    }

    @Override
    public long getAsLong() {
        return parseNumber(Long::parseLong, "long");
    }

    public boolean isInt() {
        return isParsableAs(Integer::parseInt);
    }

    public boolean isFloat() {
        return isParsableAs(Float::parseFloat);
    }

    public boolean isDouble() {
        return isParsableAs(Double::parseDouble);
    }

    public boolean isLong() {
        return isParsableAs(Long::parseLong);
    }

    private <T> T parseNumber(ParserFunction<String, T> parser, String type) {
        try {
            return parser.apply(value.toString());
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse '" + value + "' as " + type, e);
        }
    }

    private boolean isParsableAs(ParserFunction<String, ?> parser) {
        try {
            parser.apply(value.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public JsonElement copy() {
        return new JsonSimple(value);
    }

    @Override
    public Set<String> keySet() {
        return Set.of();
    }

    @FunctionalInterface
    private interface ParserFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
