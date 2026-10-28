import java.util.List;
import java.util.Scanner;

/**
 * Boundary class: the only class that reads input or prints the game state.
 * It contains NO game rules - it just shows things and returns what the player typed.
 */
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println("  !! " + message + " Please choose again.");
    }

    public void showWelcome(Menu menu) {
        System.out.println("=========================================");
        System.out.println("           RESTAURANT RUSH");
        System.out.println("=========================================");
        System.out.println("Goal after turn 18: >= 4 paid customers, revenue >= $45.00,");
        System.out.println("and average satisfaction of paid customers >= 60.");
        System.out.println("Menu:");
        for (MenuItem item : menu.getItems()) {
            System.out.println("  - " + item);
        }
    }

    public void showTurnStart(int turn, Restaurant r) {
        System.out.println();
        System.out.println("================ TURN " + turn + " / " + GameController.MAX_TURNS + " ================");
        System.out.println("Revenue: " + Money.format(r.getRevenueCents())
                + "   Avg satisfaction (paid): " + formatAverage(r.getAverageSatisfaction()));
        HappyHour hh = r.getHappyHour();
        if (hh.isActive()) {
            System.out.println("Happy Hour: ACTIVE, " + hh.getTurnsLeft() + " turn(s) left including this one");
        } else {
            System.out.println("Happy Hour: " + (hh.canActivate() ? "available (once per game)" : "used"));
        }
        System.out.println("Waiting queue: " + listCustomers(r.getWaitingQueue()));
        for (Table t : r.getTables()) {
            Customer c = t.getCustomer();
            if (c == null) {
                System.out.println("Table " + t.getNumber() + ": free");
            } else {
                String order = c.getOrder() == null ? "no order yet" : c.getOrder().toString();
                System.out.println("Table " + t.getNumber() + ": " + c + " " + c.getState()
                        + ", satisfaction " + c.getSatisfaction() + ", order: " + order);
            }
        }
        System.out.println("Staff tasks available:");
        System.out.println("  Host   -> seat: " + listCustomers(r.getWaitingQueue()));
        System.out.println("  Waiter -> seat: " + listCustomers(r.getWaitingQueue())
                + " | take order: " + listCustomers(r.getCustomersInState(CustomerState.SEATED))
                + " | serve: " + listCustomers(r.getCustomersWithOrder(OrderStatus.READY)));
        System.out.println("  Chef   -> cook: " + listCustomers(r.getCustomersWithOrder(OrderStatus.PREPARING)));
    }

    public void showTurnEnd(int turn, Restaurant r) {
        System.out.println("--- End of turn " + turn + ": served " + r.getServedCount()
                + ", unhappy departures " + r.getLeftCount()
                + ", revenue " + Money.format(r.getRevenueCents())
                + ", avg satisfaction " + formatAverage(r.getAverageSatisfaction())
                + ", turns used " + turn + "/" + GameController.MAX_TURNS + " ---");
    }

    public void showResult(boolean victory, Restaurant r) {
        System.out.println();
        System.out.println("=============== GAME OVER ===============");
        System.out.println("Paid customers: " + r.getPaidCount() + " (need 4)");
        System.out.println("Revenue: " + Money.format(r.getRevenueCents()) + " (need $45.00)");
        System.out.println("Avg satisfaction: " + formatAverage(r.getAverageSatisfaction()) + " (need 60)");
        System.out.println(victory ? "*** VICTORY! ***" : "*** DEFEAT ***");
    }

    // ----- input -----

    public boolean askHappyHour() {
        while (true) {
            System.out.print("Start Happy Hour now? (y/n): ");
            if (!scanner.hasNextLine()) {
                System.out.println("(no more input - choosing n)");
                return false;
            }
            String line = scanner.nextLine().trim().toLowerCase();
            System.out.println(line);
            if (line.equals("y") || line.equals("n")) {
                return line.equals("y");
            }
            System.out.println("  !! Please type y or n.");
        }
    }

    public int askHostTask() {
        return readInt("Host: customer number to seat (0 = wait): ");
    }

    public int askWaiterTask() {
        System.out.println("Waiter: 1) Seat  2) Take order  3) Serve  0) Wait");
        return readInt("Waiter task: ");
    }

    public int askCustomerId() {
        return readInt("Customer number (e.g. 1 for C1): ");
    }

    public int askChefTask() {
        return readInt("Chef: customer number whose order to cook (0 = wait): ");
    }

    /** Keeps asking until the player types a whole number. */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                System.out.println("(no more input - choosing 0)");
                return 0;
            }
            String line = scanner.nextLine().trim();
            System.out.println(line); // echo, so scripted runs are easy to read
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  !! '" + line + "' is not a number.");
            }
        }
    }

    // ----- formatting helpers -----

    private String listCustomers(List<Customer> list) {
        if (list.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (Customer c : list) {
            sb.append(c).append(" sat ").append(c.getSatisfaction()).append("  ");
        }
        return sb.toString().trim();
    }

    private String formatAverage(double avg) {
        return avg < 0 ? "N/A" : String.format("%.1f", avg);
    }
}
