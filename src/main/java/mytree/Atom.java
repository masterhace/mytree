package mytree;

import java.util.Objects;

import org.apache.commons.text.StringEscapeUtils;

public class Atom <T> implements Cloneable {

    /* name */

    protected String name;

    /* content */

    protected T content;

    /* Constructors */

    public Atom(String name) {
        setName(name);
    }

    public Atom(String name, T content) {
        setName(name);
        setContent(content);
    }

    /* Getters and Setters */

    public String getName() {
        return name;
    }

    public T getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public void setContent(T content) {
        this.content = content;
    }

    void unsafeSetContent(Object content) {
        setContent((T) content);
    }

    public AtomList children() {
        return (AtomList) getContent();
    }

    /* clone, hashCode and equals */

    @Override
    public Atom<T> clone() {
        Atom<T> c;
        try {
            c = (Atom<T>) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }

        if (getContent() instanceof AtomList) {
            c.setContent((T) children().clone());
        }

        return c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContent());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Atom<?> a) {
            return Objects.equals(getName(), a.getName()) && Objects.equals(getContent(), a.getContent());
        }

        return false;
    }

    /* Help methods to check content */

    public boolean isContentNull() {
        return getContent() == null;
    }

    public boolean isContentClass(Class<?> c) {
        if (isContentNull()) {
            return false;
        } else {
            return getContent().getClass() == c;
        }
    }

    /* toString methods */

    @Override
    public String toString() {
        return toXML();
    }

    public String toJSON() {
        return null;
        // Todo
    }

    public String toXML() {
        // Todo: Rewrite
        StringBuilder contentToString = new StringBuilder();
        // Use StringBuilder to optimize performance

        if (isContentNull()) {
            // Skip
        }

        else if (content instanceof AtomList) {
            contentToString.append(children());
        }

        else if (content instanceof String) {
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