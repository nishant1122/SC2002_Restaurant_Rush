/**
 * A regular customer. Things that differ between customer types
 * (satisfaction loss, bill, food choice) are methods here, so that
 * subclasses can override them later.
 */
public class Customer {
    private final int id;
    private int satisfaction = 100;
    private CustomerState state = CustomerState.WAITING;
    private Table table;
    private Order order;
    private int servedTurn; // the turn the food was served

    public Customer(int id) {
        this.id = id;
    }

    // ----- behaviour that customer types can change -----

    public String getType() { return "Regular"; }

    /** Satisfaction lost for every turn spent waiting. */
    public int getSatisfactionLoss() { return 8; }

    /** The price this customer pays for a dish. */
    public int calculateBill(int basePriceCents) { return basePriceCents; }

    /** Rule: customer #n picks menu item number (n-1) mod menuSize. */
    public MenuItem chooseItem(Menu menu) {
        return menu.getItem((id - 1) % menu.size());
    }

    // ----- state changes -----

    public void sitAt(Table table) {
        this.table = table;
        table.seat(this);
        state = CustomerState.SEATED;
    }

    public void placeOrder(Order order) {
        this.order = order;
        state = CustomerState.ORDERED;
    }

    public void receiveFood(int turn) {
        order.markServed();
        servedTurn = turn;
        state = CustomerState.EATING;
    }

    public void finishEating() { state = CustomerState.READY_TO_PAY; }

    public void pay() {
        order.markPaid();
        state = CustomerState.PAID;
        leaveTable();
    }

    /** Customer gets upset and walks out without paying. */
    public void leaveUnhappy() {
        if (order != null) {
            order.cancel();
        }
        state = CustomerState.LEFT;
        leaveTable();
    }

    private void leaveTable() {
        if (table != null) {
            table.free();
            table = null;
        }
    }

    public void loseSatisfaction() {
        satisfaction = Math.max(0, satisfaction - getSatisfactionLoss());
    }

    /** Used by Happy Hour. Never goes above 100. */
    public void recoverSatisfaction(int amount) {
        satisfaction = Math.min(100, satisfaction + amount);
    }

    /** True while the customer has not been served yet (waiting for a table or for food). */
    public boolean isWaitingForService() {
        return state == CustomerState.WAITING || state == CustomerState.SEATED || state == CustomerState.ORDERED;
    }

    public int getId() { return id; }
    public int getSatisfaction() { return satisfaction; }
    public CustomerState getState() { return state; }
    public Table getTable() { return table; }
    public Order getOrder() { return order; }
    public int getServedTurn() { return servedTurn; }

    @Override
    public String toString() {
        return "C" + id + " (" + getType() + ")";
    }
}
