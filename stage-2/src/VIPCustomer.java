/** VIP: more patient (loses 5 per turn) and gets 10% off the bill. */
public class VIPCustomer extends Customer {

    public VIPCustomer(int id) {
        super(id);
    }

    @Override
    public String getType() { return "VIP"; }

    @Override
    public int getSatisfactionLoss() { return 5; }

    @Override
    public int calculateBill(int basePriceCents) {
        return Money.percent(basePriceCents, 90);
    }
}
