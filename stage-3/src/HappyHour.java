/**
 * Pricing policy the manager can switch on ONCE per game, at the start of a turn.
 * It lasts for that turn and the next one. Orders taken while active get 20% off.
 */
public class HappyHour {
    public static final int RECOVERY = 15;

    private boolean used = false;
    private int turnsLeft = 0;

    public boolean canActivate() { return !used; }

    public void activate() {
        used = true;
        turnsLeft = 2; // this turn + next turn
    }

    public boolean isActive() { return turnsLeft > 0; }
    public int getTurnsLeft() { return turnsLeft; }

    /** Called at the end of every turn. */
    public void endTurn() {
        if (turnsLeft > 0) {
            turnsLeft--;
        }
    }

    /** 20% off while active. Applied AFTER the customer's own discount (e.g. VIP). */
    public int applyDiscount(int priceCents) {
        return isActive() ? Money.percent(priceCents, 80) : priceCents;
    }
}
