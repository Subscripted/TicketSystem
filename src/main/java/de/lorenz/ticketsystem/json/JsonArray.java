package de.lorenz.ticketsystem.json;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JsonArray extends JsonElement implements Iterable<JsonElement> {

    List<JsonElement> elements = new ArrayList<>();

    public JsonArray(List<JsonElement> elements) {
        this.elements.addAll(elements);
    }

    public JsonArray() {
    }

    public int size() {
        return elements.size();
    }

    public void add(JsonElement e) {
        elements.add(e);
    }

    public void add(Object object) {
        elements.add(new JsonSimple(object));
    }

    public void remove(int index) {
        elements.remove(index);
    }

    public void remove(JsonElement element) {
        elements.remove(element);
    }

    public JsonElement get(int index) {
        return elements.get(index);
    }

    public Set<JsonElement> asSet() {
        return new HashSet<>(elements);
    }

    @Override
    public JsonElement copy() {
        JsonArray clone = new JsonArray();
        elements.forEach(e -> clone.add(e.copy()));
        return clone;
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(JsonElement::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public Iterator<JsonElement> iterator() {
        JsonElement[] snapshot = elements.toArray(new JsonElement[0]);
        AtomicInteger index = new AtomicInteger();
        return new Iterator<>() {
            public boolean hasNext() {
                return index.get() < snapshot.length;
            }

            public JsonElement next() {
                return snapshot[index.getAndIncrement()];
            }
        };
    }
}
