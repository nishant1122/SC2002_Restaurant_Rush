/**
 * Decides who arrives on which turn: one customer on turns 1, 4, 7, 10, 13 and 16.
 * C2 (turn 4) is a VIP and C4 (turn 10) is a Critic; the rest are Regular.
 */
public class ArrivalSchedule {

    /** Returns the new customer for this turn, or null if nobody arrives. */
    public Customer customerArrivingOn(int turn) {
        if (turn > 16 || turn % 3 != 1) {
            return null;
        }
        int id = turn / 3 + 1; // turn 1 -> C1, turn 4 -> C2, ... turn 16 -> C6
        if (id == 2) {
            return new VIPCustomer(id);
        }
        if (id == 4) {
            return new CriticCustomer(id);
        }
        return new Customer(id);
    }
}
