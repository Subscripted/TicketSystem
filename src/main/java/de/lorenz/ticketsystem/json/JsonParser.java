package de.lorenz.ticketsystem.json;

import de.lorenz.ticketsystem.exceptions.InvalidJsonException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JsonParser {

    private final String json;

    public static JsonElement parseString(String json) throws InvalidJsonException {
        try {
            return new JsonParser(json).get();
        } catch (InvalidJsonException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidJsonException("'" + json + "' is not valid json", e);
        }
    }

    public JsonElement get() throws InvalidJsonException {
        if (json == null) return null;
        String trimmed = trimStringOutsideQuotes(json);

        if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) return new JsonSimple(trimmed);
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) return handleObject(trimmed);
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) return handleArray(trimmed);

        throw new InvalidJsonException(trimmed);
    }

    private String trimStringOutsideQuotes(String json) {
        StringBuilder result = new StringBuilder();
        boolean inQuotes = false;
        for (char c : json.toCharArray()) {
            if (c == '"') inQuotes = !inQuotes;
            if (!Character.isWhitespace(c) || inQuotes) result.append(c);
        }
        return result.toString();
    }

    private JsonObject handleObject(String json) throws InvalidJsonException {
        try {
            JsonObject object = new JsonObject();
            String body = removeBrackets(json, '{', '}');
            List<String> entries = splitTopLevel(body, ',');

            for (String entry : entries) {
                List<String> kv = splitTopLevel(entry, ':');
                if (kv.size() != 2) throw new InvalidJsonException(json);

                String key = stripQuotes(kv.get(0).trim());
                String valueRaw = kv.get(1).trim();

                JsonElement value = parseValue(valueRaw);
                object.add(key, value);
            }
            return object;
        } catch (Exception e) {
            throw new InvalidJsonException(json, e);
        }
    }

    private JsonArray handleArray(String json) throws InvalidJsonException {
        try {
            JsonArray array = new JsonArray();
            String body = removeBrackets(json, '[', ']');
            List<String> items = splitTopLevel(body, ',');

            for (String item : items) {
                array.add(parseValue(item.trim()));
            }
            return array;
        } catch (Exception e) {
            throw new InvalidJsonException(json, e);
        }
    }

    private JsonElement parseValue(String raw) throws InvalidJsonException {
        if (raw.startsWith("{") && raw.endsWith("}")) return handleObject(raw);
        if (raw.startsWith("[") && raw.endsWith("]")) return handleArray(raw);
        return new JsonSimple(stripQuotes(raw));
    }

    private String stripQuotes(String s) {
        return s.replaceAll("^\"|\"$", "");
    }

    private String removeBrackets(String s, char open, char close) {
        if (s.length() >= 2 && s.charAt(0) == open && s.charAt(s.length() - 1) == close)
            return s.substring(1, s.length() - 1);
        return s;
    }

    private List<String> splitTopLevel(String input, char delimiter) {
        List<String> result = new ArrayList<>();
        int depth = 0;
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '"') inQuotes = !inQuotes;
            if (!inQuotes) {
                if (c == '{' || c == '[') depth++;
                else if (c == '}' || c == ']') depth--;
                else if (c == delimiter && depth == 0) {
                    result.add(current.toString());
                    current.setLength(0);
                    continue;
                }
            }
            current.append(c);
        }
        result.add(current.toString());
        return result.stream().filter(s -> !s.isBlank()).collect(Collectors.toList());
    }
}