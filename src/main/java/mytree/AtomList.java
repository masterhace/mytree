package mytree;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class AtomList extends ArrayList<Atom<?>> {
    @Override
    public AtomList clone() {
        AtomList c = new AtomList();

        for (Atom<?> a : this) {
            c.add(a.clone());
        }

        return c;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Atom<?> a : this) {
            stringBuilder.append(a);
        }

        return stringBuilder.toString();
    }

    /* Helpers */

    public void mod(int index, String name) {
        get(index).setName(name);
    }

    public void mod(int index, Object content) {
        get(index).unsafeSetContent(content);
    }

    public void mod(int index, String name, Object content) {
        mod(index, name);
        mod(index, content);
    }

    public int move(int startIndex, int destinationIndex) {
        if (startIndex == destinationIndex) {
            return startIndex;
            // Do nothing
        }

        if (startIndex < destinationIndex) {
            destinationIndex--;
            // Minus 1 to avoid move to wrong position
        }

        add(destinationIndex, remove(startIndex));
        return destinationIndex;
    }

    public int transfer(int startIndex, int destinationIndex) {
        get(destinationIndex).children().add(remove(startIndex));

        if (startIndex < destinationIndex) {
            return destinationIndex - 1;
        } else {
            return destinationIndex;
        }
    }

    public int[] search(String name) {
        IntStream.Builder builder = IntStream.builder();

        for (int i = 0; i < size(); i++) {
            if (!(get(i) == null)) {
                if (Objects.equals(get(i).getName(), name)) {
                    builder.accept(i);
                }
            }
        }

        return builder.build().toArray();
    }
}
