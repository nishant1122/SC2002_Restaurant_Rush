/** Decides who arrives on which turn: one customer on turns 1, 4, 7, 10, 13 and 16. */
public class ArrivalSchedule {

    /** Returns the new customer for this turn, or null if nobody arrives. */
    public Customer customerArrivingOn(int turn) {
        if (turn > 16 || turn % 3 != 1) {
            return null;
        }
        int id = turn / 3 + 1; // turn 1 -> C1, turn 4 -> C2, ... turn 16 -> C6
        return new Customer(id);
    }
}
