/** The Host can seat ONE queued customer per turn, in addition to the Waiter's task. */
public class Host {

    public String seat(Restaurant restaurant, int customerId) {
        return restaurant.seatCustomer(customerId);
    }
}
