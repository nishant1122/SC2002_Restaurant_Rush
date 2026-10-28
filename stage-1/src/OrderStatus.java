/** Life cycle of an order. */
public enum OrderStatus {
    PREPARING,  // taken by the Waiter, Chef still working on it
    READY,      // all preparation units done, waiting to be served
    SERVED,     // on the customer's table
    PAID,       // customer has paid
    CANCELLED   // customer left before being served
}
