package mytree;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.text.StringEscapeUtils;

/**
 * An implementation of tree structure.
 * <p>
 * The key of this implementation is the Element class which only has 2 fields: name and content.
 * The type of name is always String, and it permits all types to store in content.
 * <p>
 * ElementList is another class that extends ArrayList, only designed for storing a list of Element. It's important to know to store an ElementList in content to make a tree structure like XML and JSON.
 *
 * @author  Me
 * @see     ElementList
 */

public class Element implements Cloneable {

    /**
     * The name of the Element
     */

    public String name;

    /**
     * The content of the Element.
     * <p>
     * It can store everything, but only ElementList and simple types like String and Integer is recommended.
     * It can also store ElementList which stores a list of Element
     */

    public Object content;

    /**
     * Constructs an Element with name
     */

    public Element(String name) {
        this.name = name;
    }

    /**
     * Constructs an Element with name and content
     */

    public Element(String name, Object content) {
        this.name = name;
        this.content = content;
    }

    /**
     * The method clone() deep copies the Element.
     * <p>
     * The objects other than ElementList stored in content would get shallow copied since they could be unchangeable.
     * <p>
     * The ElementList stored in content would get deep copied recursively.
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
     * Returns the hash code of the Element
     */

    @Override
    public int hashCode() {
        return Objects.hash(name, content);
    }

    /**
     * Compare two Elements
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

    public Class<?> getContentClass() {
        if (isContentNull()) {
            return null;
        } else {
            return content.getClass();
        }
    }

    public boolean isContentNull() {
        return content == null;
    }

    public boolean isContentClass(Class<?> c) {
        return getContentClass() == c;
    }

    /*
      Help methods to access content to avoid casting
      Exception: all methods would throw ClassCastException
     */

    /*
      Access content when its type is ElementList
     */

    public void addChild(Element e) {
        getChildren().add(e);
    }

    public void addChild(Element e, int index) {
        getChildren().add(index, e);
    }

    public Element removeChild(int index) {
        return getChildren().remove(index);
        // Remove while return
    }

    public Element getChild(int index) {
        return getChildren().get(index);
    }

    public Element setChild(int index, Element e) {
        return getChildren().set(index, e);
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

    public ElementList getChildren() {
        return (ElementList) content;
    }

    public void clearChildren() {
        getChildren().clear();
    }

    public int childCount() {
        return getChildren().size();
    }

    public boolean containsChild(Element e) {
        return getChildren().contains(e);
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