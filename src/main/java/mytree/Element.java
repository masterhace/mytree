package mytree;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.text.StringEscapeUtils;

/**
 * An implementation of tree structure.
 * <p>
 * The key of this implementation is the {@code Element} class which only has 2 fields: {@code name} and {@code content}.
 * The type of {@code name} is always {@code String}, and it permits all types to store in {@code content}.
 * <p>
 * {@code ElementList} is another class that extends {@code ArrayList}, only designed for storing a list of {@code Element}. It's important to know to store an {@code ElementList} in {@code content} to make a tree structure like XML and JSON.
 *
 * @author  Me
 * @see     ElementList
 */

public class Element implements Cloneable {

    /**
     * The name of the {@code Element}.
     */

    public String name;

    /**
     * The content of the {@code Element}.
     * <p>
     * It can store everything, but only {@code ElementList} and simple types like {@code String} and {@code Integer} is recommended.
     * It can also store {@code ElementList} which stores a list of {@code Element}.
     */

    public Object content;

    /**
     * Constructs an {@code Element} with {@code name}.
     */

    public Element(String name) {
        this.name = name;
    }

    /**
     * Constructs an {@code Element} with {@code name} and {@code content}.
     */

    public Element(String name, Object content) {
        this.name = name;
        this.content = content;
    }

    /**
     * The {@code clone} method deep copies the {@code Element}.
     * <p>
     * The objects other than {@code ElementList} stored in {@code content} would get shallow copied since they could be unchangeable.
     * <p>
     * The {@code ElementList} stored in {@code content} would get deep copied recursively.
     * @return a clone of this {@code Element}.
     */

    @Override
    public Element clone() {
        Element c;
        try {
            c = (Element) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }

        if (isContentClass(ElementList.class)) {
            c.content = getChildren().clone();
        }

        return c;
    }

    /**
     * Returns the hash code of this {@code Element}.
     * @return a hash code value for this {@code Element}.
     */

    @Override
    public int hashCode() {
        return Objects.hash(name, content);
    }

    /**
     * Compares two {@code Element}.
     * @return {@code true} if the given {@code Object} is equal to this {@code Element}.
     */

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Element e) {
            return Objects.equals(name, e.name) && Objects.equals(content, e.content);
        }

        return false;
    }

    /*
      Help methods to check content
     */

    /**
     * Returns the {@code Class} object of {@code content}.
     * @return The {@code Class} object that represents the runtime class of {@code content}.
     */

    public Class<?> getContentClass() {
        if (isContentNull()) {
            return null;
        } else {
            return content.getClass();
        }
    }

    /**
     * Check if {@code content} is null.
     * @return {@code true} if {@code content} is null.
     */

    public boolean isContentNull() {
        return content == null;
    }

    /**
     * Check if {@code content} is the given type.
     * @param c the {@code Class} object.
     * @return {@code true} if {@code content} is the given type.
     */

    public boolean isContentClass(Class<?> c) {
        return getContentClass() == c;
    }

    /*
      Access content when its type is ElementList
     */

    /**
     * Adds the given {@code Element} to the {@code ElementList} stored in {@code content}.
     * @param e the {@code Element} object to be added.
     */

    public void addChild(Element e) {
        getChildren().add(e);
    }

    /**
     * Adds the given {@code Element} to the {@code ElementList} stored in {@code content} with index.
     * @param e the {@code Element} object to be added.
     * @param index index at which the given {@code Element} is to be inserted.
     */

    public void addChild(Element e, int index) {
        getChildren().add(index, e);
    }

    /**
     * Removes the {@code Element} from the {@code ElementList} stored in {@code content} with index.
     * @param index the index of the {@code Element} object to be added.
     * @return the {@code Element} that was removed from the {@code ElementList}.
     */

    public Element removeChild(int index) {
        return getChildren().remove(index);
    }

    /**
     * Removes first occurrence of the given {@code Element} from the {@code ElementList} stored in {@code content}.
     * @param e the {@code Element} object to be removed.
     * @return {@code true} if the {@code ElementList} contained the given {@code Element}.
     */

    public boolean removeChild(Element e) {
        return getChildren().remove(e);
    }

    /**
     * Returns the given index of {@code Element} from {@code ElementList} stored in {@code content}.
     * @param index the index of the {@code Element} object to be gotten.
     * @return the {@code Element} at the given index of the {@code ElementList}.
     */

    public Element getChild(int index) {
        return getChildren().get(index);
    }

    /**
     * Replaces the given index of {@code Element} from {@code ElementList} stored in {@code content} with the given {@code Element} object.
     * @param index the index of the {@code Element} object to be replaced.
     * @param e the given {@code Element} to be stored.
     * @return the replaced {@code Element}.
     */

    public Element setChild(int index, Element e) {
        return getChildren().set(index, e);
    }

    /**
     * Modifies the given index of {@code Element} from {@code ElementList} stored in {@code content} with the given {@code name}.
     * @param index the index of the {@code Element} object to be replaced.
     * @param name the given {@code name} to be stored.
     */

    public void modChild(int index, String name) {
        getChild(index).name = name;
    }

    /**
     * Modifies the given index of {@code Element} from {@code ElementList} stored in {@code content} with the given {@code name} and {@code Object}.
     * @param index the index of the {@code Element} object to be replaced.
     * @param name the given {@code name} to be stored.
     * @param o the given {@code Object} to be stored.
     */

    public void modChild(int index, String name, Object o) {
        getChild(index).name = name;
        getChild(index).content = o;
    }

    public int moveChild(int startIndex, int destinationIndex) {
        if (startIndex == destinationIndex) {
            return startIndex;
            // Do nothing if destinationIndex == startIndex
        }

        if (startIndex < destinationIndex) {
            destinationIndex--;
            // Minus 1 to avoid move to wrong position
        }

        addChild(removeChild(startIndex), destinationIndex);
        return destinationIndex;
    }

    public int transferChild(int startIndex, int destinationIndex) {
        getChild(destinationIndex).addChild(removeChild(startIndex));

        if (startIndex < destinationIndex) {
            return destinationIndex - 1;
        } else {
            return destinationIndex;
        }
    }

    public int[] searchChild(String name) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < childCount(); i++) {
            if (Objects.equals(getChild(i).name, name)) {
                list.add(i);
            }
        }

        return list.stream().mapToInt(Integer::intValue).toArray();
        // Unbox Integer
    }

    public boolean containsChild(Element e) {
        return getChildren().contains(e);
    }

    public int childCount() {
        return getChildren().size();
    }

    public ElementList getChildren() {
        return (ElementList) content;
    }

    public void clearChildren() {
        getChildren().clear();
    }

    /*
        Access content when its type is String
     */

    public String getContentString() {
        return (String) content;
    }

    public void setContentString(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return toXML();
    }

    public String toJSON() {
        return null;
        // Todo
    }

    public String toXML() {
        StringBuilder contentToString = new StringBuilder();
        // Use StringBuilder to optimize performance

        if (isContentNull()) {
            // Skip
        }

        else if (isContentClass(ElementList.class)) {
            for (Element e : getChildren()) {
                contentToString.append(e);
            }
            // Iterate ElementList by myself since its toString() looks like json
        }

        else if (isContentClass(String.class)) {
            contentToString.append(
                    StringEscapeUtils.escapeXml10(String.valueOf(content))
            );
            // Replace special characters with escape characters
        }

        else {
            contentToString.append(content);
        }

        return "<" + name + ">" + contentToString + "</" + name + ">";
    }
}