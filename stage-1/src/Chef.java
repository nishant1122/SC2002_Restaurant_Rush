/** The Chef does ONE preparation unit on one order per turn. */
public class Chef {

    public String prepare(Restaurant restaurant, int customerId) {
        Customer customer = restaurant.findCustomer(customerId);
        if (customer == null || customer.getOrder() == null) {
            throw new IllegalArgumentException("Customer " + customerId + " has no order.");
        }
        Order order = customer.getOrder();
        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new IllegalArgumentException("Order of " + customer + " does not need cooking (it is " + order.getStatus() + ").");
        }
        order.prepareOneUnit();
        return "Chef works on " + customer + "'s " + order + ".";
    }
}
