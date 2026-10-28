/**
 * A combo is made of two existing menu items (composition).
 * Prep units = sum of both; price = 90% of both prices, rounded to the nearest cent.
 * Because it IS a MenuItem, the rest of the game (Menu, Order, Chef) uses it unchanged.
 */
public class ComboMeal extends MenuItem {
    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second) {
        super(first.getName() + " + " + second.getName() + " Combo",
                Money.percent(first.getPriceCents() + second.getPriceCents(), 90),
                first.getPrepUnits() + second.getPrepUnits());
        this.first = first;
        this.second = second;
    }

    public MenuItem getFirst() { return first; }
    public MenuItem getSecond() { return second; }
}
