/** The Cashier automatically takes ONE payment per turn, oldest customer first. */
public class Cashier {

    /** Returns a message, or null if nobody was ready to pay. */
    public String collectPayment(Restaurant restaurant) {
        for (Customer customer : restaurant.getCustomers()) { // arrival order = oldest first
            if (customer.getState() == CustomerState.READY_TO_PAY) {
                int bill = customer.getOrder().getPriceCents();
                restaurant.addRevenue(bill);
                customer.pay();
                return customer + " pays " + Money.format(bill) + " (satisfaction " + customer.getSatisfaction() + "). Table freed.";
            }
        }
        return null;
    }
}
