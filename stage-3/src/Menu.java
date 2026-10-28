import java.util.ArrayList;
import java.util.List;

/** The list of dishes customers can choose from. */
public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    public void addItem(MenuItem item) { items.add(item); }
    public MenuItem getItem(int index) { return items.get(index); }
    public int size() { return items.size(); }
    public List<MenuItem> getItems() { return items; }

    /** Used by the Critic. If two items cost the same, the first one wins. */
    public MenuItem getMostExpensive() {
        MenuItem best = items.get(0);
        for (MenuItem item : items) {
            if (item.getPriceCents() > best.getPriceCents()) {
                best = item;
            }
        }
        return best;
    }
}
