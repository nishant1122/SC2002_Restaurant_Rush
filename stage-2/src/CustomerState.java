/** Where a customer is in their visit. */
public enum CustomerState {
    WAITING,       // in the waiting queue, no table yet
    SEATED,        // at a table, order not taken yet
    ORDERED,       // order taken, waiting for food
    EATING,        // food served
    READY_TO_PAY,  // finished eating
    PAID,          // paid and left happily
    LEFT           // satisfaction hit 0, left without paying
}
