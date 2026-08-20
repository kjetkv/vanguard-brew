package io.code.vanguard.brew;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class is used to enforce the usage of Streams for the input to a kata.
 * <p/>
 * No, we can never predict everything a developer will do,
 * but this gives at least a little nudge in the direction of using Streams...
 *
 * @param <T> the type of the objects in the list
 */
public final class StreamEnforcingList<T> extends ArrayList<T> {
    public StreamEnforcingList(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("🚨 ALARM: Manual loop detected! The conveyor belt requires .stream() processing!");
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        throw new UnsupportedOperationException("🚨 ALARM: Collection.forEach() detected! Please use .stream() instead!");
    }

    @SafeVarargs
    public static <T> List<T> list(T... elements) {
        return new StreamEnforcingList<>(List.of(elements));
    }

    @Override
    public String toString() {
        return stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}