/**
 * Small helper for money. All amounts are stored as whole cents (int)
 * so we never get rounding errors from doubles.
 */
public class Money {

    /** 1530 -> "$15.30" */
    public static String format(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
