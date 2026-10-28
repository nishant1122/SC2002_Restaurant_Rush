/** The Waiter can do ONE of these per turn: seat, take an order, or serve. */
public class Waiter {

    public String seat(Restaurant restaurant, int customerId) {
        return restaurant.seatCustomer(customerId);
    }

    /** The customer picks their own dish; the price is locked now. */
    public String takeOrder(Restaurant restaurant, int customerId) {
        Customer customer = restaurant.findCustomer(customerId);
        if (customer == null || customer.getState() != CustomerState.SEATED) {
            throw new IllegalArgumentException("Customer " + customerId + " is not seated and waiting to order.");
        }
        MenuItem item = customer.chooseItem(restaurant.getMenu());
        int price = customer.calculateBill(item.getPriceCents());    // customer discount first (VIP)
        price = restaurant.getHappyHour().applyDiscount(price);      // then Happy Hour
        customer.placeOrder(new Order(item, price));
        String note = restaurant.getHappyHour().isActive() ? " (Happy Hour price)" : "";
        return customer + " orders " + item.getName() + " for " + Money.format(price) + note + ".";
    }

    /** Only an order that is already READY can be served. */
    public String serve(Restaurant restaurant, int customerId, int turn) {
        Customer customer = restaurant.findCustomer(customerId);
        if (customer == null || customer.getOrder() == null) {
            throw new IllegalArgumentException("Customer " + customerId + " has no order.");
        }
        if (customer.getOrder().getStatus() != OrderStatus.READY) {
            throw new IllegalArgumentException("Order of " + customer + " is not READY (it is " + customer.getOrder().getStatus() + ").");
        }
        customer.receiveFood(turn);
        return customer + " is served " + customer.getOrder().getItem().getName() + ".";
    }
}
