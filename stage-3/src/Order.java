/** A customer's order. The price is locked in when the order is taken. */
public class Order {
    private final MenuItem item;
    private final int priceCents;
    private int unitsDone = 0;
    private OrderStatus status = OrderStatus.PREPARING;

    public Order(MenuItem item, int priceCents) {
        this.item = item;
        this.priceCents = priceCents;
    }

    /** Chef does one unit of work. When all units are done the order becomes READY. */
    public void prepareOneUnit() {
        unitsDone++;
        if (unitsDone >= item.getPrepUnits()) {
            status = OrderStatus.READY;
        }
    }

    public void markServed() { status = OrderStatus.SERVED; }
    public void markPaid() { status = OrderStatus.PAID; }
    public void cancel() { status = OrderStatus.CANCELLED; }

    public MenuItem getItem() { return item; }
    public int getPriceCents() { return priceCents; }
    public OrderStatus getStatus() { return status; }

    @Override
    public String toString() {
        String text = item.getName() + " " + Money.format(priceCents) + " [" + status;
        if (status == OrderStatus.PREPARING) {
            text += " " + unitsDone + "/" + item.getPrepUnits();
        }
        return text + "]";
    }
}
