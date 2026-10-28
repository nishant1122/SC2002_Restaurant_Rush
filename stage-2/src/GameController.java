/**
 * Control class: runs the 18 turns and applies the turn steps in order.
 * It asks the UI for the manager's choices and the staff objects do the work.
 */
public class GameController {
    public static final int MAX_TURNS = 18;

    private final Restaurant restaurant;
    private final ConsoleUI ui;
    private final ArrivalSchedule schedule = new ArrivalSchedule();
    private final Waiter waiter = new Waiter();
    private final Chef chef = new Chef();
    private final Cashier cashier = new Cashier();

    public GameController(Restaurant restaurant, ConsoleUI ui) {
        this.restaurant = restaurant;
        this.ui = ui;
    }

    public void play() {
        ui.showWelcome(restaurant.getMenu());
        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            playTurn(turn);
        }
        ui.showResult(isVictory(), restaurant);
    }

    private void playTurn(int turn) {
        // Step 1: arrivals
        Customer arrival = schedule.customerArrivingOn(turn);
        if (arrival != null) {
            restaurant.addCustomer(arrival);
        }
        ui.showTurnStart(turn, restaurant);
        if (arrival != null) {
            ui.showMessage(">> " + arrival + " arrives and joins the queue.");
        }

        // Steps 2 + 3: manager chooses; Waiter acts before Chef
        waiterStep(turn);
        chefStep();

        // Step 4: payment, then eating
        String payment = cashier.collectPayment(restaurant);
        if (payment != null) {
            ui.showMessage(">> Cashier: " + payment);
        }
        for (Customer c : restaurant.getCustomersInState(CustomerState.EATING)) {
            if (c.getServedTurn() < turn) { // served on an earlier turn
                c.finishEating();
                ui.showMessage(">> " + c + " finished eating and is READY_TO_PAY.");
            }
        }

        // Step 5: waiting customers lose satisfaction and may leave
        for (Customer c : restaurant.getCustomers()) {
            if (c.isWaitingForService()) {
                c.loseSatisfaction();
                if (c.getSatisfaction() == 0) {
                    c.leaveUnhappy();
                    ui.showMessage(">> " + c + " got fed up and LEFT without paying.");
                }
            }
        }
        ui.showTurnEnd(turn, restaurant);
    }

    /** Keeps asking until the Waiter's choice is valid. An invalid choice does not use up the task. */
    private void waiterStep(int turn) {
        while (true) {
            int task = ui.askWaiterTask();
            if (task == 0) {
                ui.showMessage(">> Waiter waits.");
                return;
            }
            if (task < 0 || task > 3) {
                ui.showError("Unknown waiter task " + task + ".");
                continue;
            }
            int id = ui.askCustomerId();
            try {
                String result;
                if (task == 1) {
                    result = waiter.seat(restaurant, id);
                } else if (task == 2) {
                    result = waiter.takeOrder(restaurant, id);
                } else {
                    result = waiter.serve(restaurant, id, turn);
                }
                ui.showMessage(">> Waiter: " + result);
                return;
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void chefStep() {
        while (true) {
            int id = ui.askChefTask();
            if (id == 0) {
                ui.showMessage(">> Chef waits.");
                return;
            }
            try {
                ui.showMessage(">> " + chef.prepare(restaurant, id));
                return;
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /** Step 6: all three targets must be met. */
    private boolean isVictory() {
        return restaurant.getPaidCount() >= 4
                && restaurant.getRevenueCents() >= 4500
                && restaurant.getAverageSatisfaction() >= 60;
    }
}
