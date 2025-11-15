package mytree;

import java.util.ArrayList;

public class ElementList extends ArrayList<Element> {
    @Override
    public ElementList clone() {
        ElementList c = new ElementList();

        for (Element e : this) {
            c.add(e.clone());
        }

        return c;
    }
}
