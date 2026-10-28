/**
 * Food critic: impatient (loses 12 per turn).
 * Special behaviour: always orders the most expensive item on the menu
 * (to test the kitchen), instead of following the normal choice rule.
 */
public class CriticCustomer extends Customer {

    public CriticCustomer(int id) {
        super(id);
    }

    @Override
    public String getType() { return "Critic"; }

    @Override
    public int getSatisfactionLoss() { return 12; }

    @Override
    public MenuItem chooseItem(Menu menu) {
        return menu.getMostExpensive();
    }
}
