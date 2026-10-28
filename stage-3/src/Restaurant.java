import java.util.ArrayList;
import java.util.List;

/**
 * Holds the whole game state: menu, tables, customers and revenue.
 * The waiting queue is simply the WAITING customers in arrival order.
 */
public class Restaurant {
    private final Menu menu;
    private final List<Table> tables = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>(); // everyone who arrived, in arrival order
    private final HappyHour happyHour = new HappyHour();
    private int revenueCents = 0;

    public Restaurant(Menu menu, int tableCount) {
        this.menu = menu;
        for (int i = 1; i <= tableCount; i++) {
            tables.add(new Table(i));
        }
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /** Seats a queued customer at the first free table. Used by both the Waiter and the Host. */
    public String seatCustomer(int customerId) {
        Customer customer = findCustomer(customerId);
        if (customer == null || customer.getState() != CustomerState.WAITING) {
            throw new IllegalArgumentException("Customer " + customerId + " is not in the waiting queue.");
        }
        Table table = findFreeTable();
        if (table == null) {
            throw new IllegalArgumentException("No free table. A table is freed only when its customer pays or leaves.");
        }
        customer.sitAt(table);
        return customer + " is seated at Table " + table.getNumber() + ".";
    }

    public Customer findCustomer(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Table findFreeTable() {
        for (Table t : tables) {
            if (t.isFree()) {
                return t;
            }
        }
        return null;
    }

    public void addRevenue(int cents) { revenueCents += cents; }

    // ----- lists used for display -----

    public List<Customer> getCustomersInState(CustomerState state) {
        List<Customer> result = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getState() == state) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Customer> getWaitingQueue() { return getCustomersInState(CustomerState.WAITING); }

    /** Customers whose order is in the given status (e.g. READY to serve, PREPARING to cook). */
    public List<Customer> getCustomersWithOrder(OrderStatus status) {
        List<Customer> result = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getOrder() != null && c.getOrder().getStatus() == status) {
                result.add(c);
            }
        }
        return result;
    }

    // ----- statistics -----

    public int getPaidCount() { return getCustomersInState(CustomerState.PAID).size(); }
    public int getLeftCount() { return getCustomersInState(CustomerState.LEFT).size(); }

    /** Customers who have received their food (eating, ready to pay or paid). */
    public int getServedCount() {
        return getCustomersInState(CustomerState.EATING).size()
                + getCustomersInState(CustomerState.READY_TO_PAY).size()
                + getPaidCount();
    }

    /** Average satisfaction of paid customers, or -1 if nobody has paid yet. */
    public double getAverageSatisfaction() {
        List<Customer> paid = getCustomersInState(CustomerState.PAID);
        if (paid.isEmpty()) {
            return -1;
        }
        int total = 0;
        for (Customer c : paid) {
            total += c.getSatisfaction();
        }
        return (double) total / paid.size();
    }

    public Menu getMenu() { return menu; }
    public HappyHour getHappyHour() { return happyHour; }
    public List<Table> getTables() { return tables; }
    public List<Customer> getCustomers() { return customers; }
    public int getRevenueCents() { return revenueCents; }
}
