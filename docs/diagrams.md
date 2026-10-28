# UML Diagrams

GitHub shows these diagrams directly. To get PNG pictures for the PDF report, open
`view-diagrams.html` in a browser and click **Download PNG** under each diagram
(or paste a `.mmd` file into https://mermaid.live).

## Stage 1 class diagram

Regular customers, menu, Waiter / Chef / Cashier, tables, full turn sequence, victory and defeat.

```mermaid
---
title: "Stage 1 - Basic restaurant operation"
---
classDiagram
    direction TB

    class Main {
        +main(String[] args)$ void
    }
    class GameController {
        <<control>>
        +MAX_TURNS: int$ = 18
        -restaurant: Restaurant
        -ui: ConsoleUI
        +play() void
        -playTurn(int turn) void
        -waiterStep(int turn) void
        -chefStep() void
        -isVictory() boolean
    }
    class ConsoleUI {
        <<boundary>>
        -scanner: Scanner
        +showTurnStart(int turn, Restaurant r) void
        +showTurnEnd(int turn, Restaurant r) void
        +showResult(boolean victory, Restaurant r) void
        +showMessage(String msg) void
        +showError(String msg) void
        +askWaiterTask() int
        +askCustomerId() int
        +askChefTask() int
    }
    class ArrivalSchedule {
        +customerArrivingOn(int turn) Customer
    }
    class Restaurant {
        -revenueCents: int
        +addCustomer(Customer c) void
        +seatCustomer(int id) String
        +findCustomer(int id) Customer
        +addRevenue(int cents) void
        +getPaidCount() int
        +getAverageSatisfaction() double
    }
    class Waiter {
        +seat(Restaurant r, int id) String
        +takeOrder(Restaurant r, int id) String
        +serve(Restaurant r, int id, int turn) String
    }
    class Chef {
        +prepare(Restaurant r, int id) String
    }
    class Cashier {
        +collectPayment(Restaurant r) String
    }
    class Customer {
        -id: int
        -satisfaction: int
        -state: CustomerState
        -servedTurn: int
        +getSatisfactionLoss() int
        +calculateBill(int baseCents) int
        +chooseItem(Menu menu) MenuItem
        +sitAt(Table t) void
        +placeOrder(Order o) void
        +receiveFood(int turn) void
        +finishEating() void
        +pay() void
        +leaveUnhappy() void
        +loseSatisfaction() void
        +isWaitingForService() boolean
    }
    class Table {
        -number: int
        +isFree() boolean
        +seat(Customer c) void
        +free() void
    }
    class Order {
        -priceCents: int
        -unitsDone: int
        -status: OrderStatus
        +prepareOneUnit() void
        +markServed() void
        +markPaid() void
        +cancel() void
    }
    class Menu {
        +addItem(MenuItem item) void
        +getItem(int index) MenuItem
        +size() int
    }
    class MenuItem {
        -name: String
        -priceCents: int
        -prepUnits: int
    }
    class Money {
        +format(int cents)$ String
    }
    class CustomerState {
        <<enumeration>>
        WAITING
        SEATED
        ORDERED
        EATING
        READY_TO_PAY
        PAID
        LEFT
    }
    class OrderStatus {
        <<enumeration>>
        PREPARING
        READY
        SERVED
        PAID
        CANCELLED
    }

    Main ..> GameController : creates
    GameController --> "1" ConsoleUI
    GameController --> "1" Restaurant
    GameController *-- "1" ArrivalSchedule
    GameController *-- "1" Waiter
    GameController *-- "1" Chef
    GameController *-- "1" Cashier

    Restaurant *-- "1" Menu
    Restaurant *-- "2" Table
    Restaurant o-- "0..6" Customer

    Customer "0..1" -- "0..1" Table : sits at
    Customer --> "0..1" Order
    Customer --> CustomerState
    Order --> "1" MenuItem
    Order --> OrderStatus
    Menu o-- "1..*" MenuItem

    ArrivalSchedule ..> Customer : creates
    Waiter ..> Restaurant
    Chef ..> Restaurant
    Cashier ..> Restaurant

    note for Order "ENCAPSULATION: status only changes through<br>prepareOneUnit() / markServed() / markPaid() / cancel()"
    note for Restaurant "COMPOSITION: Restaurant owns its Menu and Tables"
```

## Stage 2 class diagram

Adds `VIPCustomer` and `CriticCustomer` (green). Small additions: `Menu.getMostExpensive()` (Critic's choice) and `Money.percent()` (VIP discount). `GameController`, `Waiter`, `Chef`, `Cashier` and `ConsoleUI` are unchanged.

```mermaid
---
title: "Stage 2 - Customer behaviour (new classes in green)"
---
classDiagram
    direction TB

    class Main {
        +main(String[] args)$ void
    }
    class GameController {
        <<control>>
        +MAX_TURNS: int$ = 18
        -restaurant: Restaurant
        -ui: ConsoleUI
        +play() void
        -playTurn(int turn) void
        -waiterStep(int turn) void
        -chefStep() void
        -isVictory() boolean
    }
    class ConsoleUI {
        <<boundary>>
        -scanner: Scanner
        +showTurnStart(int turn, Restaurant r) void
        +showTurnEnd(int turn, Restaurant r) void
        +showResult(boolean victory, Restaurant r) void
        +showMessage(String msg) void
        +showError(String msg) void
        +askWaiterTask() int
        +askCustomerId() int
        +askChefTask() int
    }
    class ArrivalSchedule {
        +customerArrivingOn(int turn) Customer
    }
    class Restaurant {
        -revenueCents: int
        +addCustomer(Customer c) void
        +seatCustomer(int id) String
        +findCustomer(int id) Customer
        +addRevenue(int cents) void
        +getPaidCount() int
        +getAverageSatisfaction() double
    }
    class Waiter {
        +seat(Restaurant r, int id) String
        +takeOrder(Restaurant r, int id) String
        +serve(Restaurant r, int id, int turn) String
    }
    class Chef {
        +prepare(Restaurant r, int id) String
    }
    class Cashier {
        +collectPayment(Restaurant r) String
    }
    class Customer {
        -id: int
        -satisfaction: int
        -state: CustomerState
        -servedTurn: int
        +getSatisfactionLoss() int
        +calculateBill(int baseCents) int
        +chooseItem(Menu menu) MenuItem
        +sitAt(Table t) void
        +placeOrder(Order o) void
        +receiveFood(int turn) void
        +finishEating() void
        +pay() void
        +leaveUnhappy() void
        +loseSatisfaction() void
        +isWaitingForService() boolean
    }
    class VIPCustomer {
        +getSatisfactionLoss() int
        +calculateBill(int baseCents) int
    }
    class CriticCustomer {
        +getSatisfactionLoss() int
        +chooseItem(Menu menu) MenuItem
    }
    class Table {
        -number: int
        +isFree() boolean
        +seat(Customer c) void
        +free() void
    }
    class Order {
        -priceCents: int
        -unitsDone: int
        -status: OrderStatus
        +prepareOneUnit() void
        +markServed() void
        +markPaid() void
        +cancel() void
    }
    class Menu {
        +addItem(MenuItem item) void
        +getItem(int index) MenuItem
        +size() int
        +getMostExpensive() MenuItem
    }
    class MenuItem {
        -name: String
        -priceCents: int
        -prepUnits: int
    }
    class Money {
        +format(int cents)$ String
        +percent(int cents, int pct)$ int
    }
    class CustomerState {
        <<enumeration>>
        WAITING
        SEATED
        ORDERED
        EATING
        READY_TO_PAY
        PAID
        LEFT
    }
    class OrderStatus {
        <<enumeration>>
        PREPARING
        READY
        SERVED
        PAID
        CANCELLED
    }

    Main ..> GameController : creates
    GameController --> "1" ConsoleUI
    GameController --> "1" Restaurant
    GameController *-- "1" ArrivalSchedule
    GameController *-- "1" Waiter
    GameController *-- "1" Chef
    GameController *-- "1" Cashier

    Restaurant *-- "1" Menu
    Restaurant *-- "2" Table
    Restaurant o-- "0..6" Customer

    Customer <|-- VIPCustomer
    Customer <|-- CriticCustomer
    Customer "0..1" -- "0..1" Table : sits at
    Customer --> "0..1" Order
    Customer --> CustomerState
    Order --> "1" MenuItem
    Order --> OrderStatus
    Menu o-- "1..*" MenuItem

    ArrivalSchedule ..> Customer : creates
    Waiter ..> Restaurant
    Chef ..> Restaurant
    Cashier ..> Restaurant

    note for Order "ENCAPSULATION: status only changes through<br>prepareOneUnit() / markServed() / markPaid() / cancel()"
    note for Customer "POLYMORPHISM: VIP and Critic override<br>getSatisfactionLoss() / calculateBill() / chooseItem().<br>GameController has no type checks."
    note for Restaurant "COMPOSITION: Restaurant owns its Menu and Tables"

    style VIPCustomer fill:#d9f2e3,stroke:#2e8b57
    style CriticCustomer fill:#d9f2e3,stroke:#2e8b57
```

## Stage 3 class diagram

Adds `Host`, `ComboMeal` and `HappyHour` (green). Changed: `GameController` (Happy Hour + Host steps), `ConsoleUI` (new prompts), `Waiter.takeOrder()` (Happy Hour price), `Restaurant` (owns `HappyHour`), `Customer.recoverSatisfaction()`.

```mermaid
---
title: "Stage 3 - Host, ComboMeal, Happy Hour (new classes in green)"
---
classDiagram
    direction TB

    class Main {
        +main(String[] args)$ void
    }
    class GameController {
        <<control>>
        +MAX_TURNS: int$ = 18
        -restaurant: Restaurant
        -ui: ConsoleUI
        +play() void
        -playTurn(int turn) void
        -hostStep() void
        -waiterStep(int turn) void
        -chefStep() void
        -isVictory() boolean
    }
    class ConsoleUI {
        <<boundary>>
        -scanner: Scanner
        +showTurnStart(int turn, Restaurant r) void
        +showTurnEnd(int turn, Restaurant r) void
        +showResult(boolean victory, Restaurant r) void
        +showMessage(String msg) void
        +showError(String msg) void
        +askHappyHour() boolean
        +askHostTask() int
        +askWaiterTask() int
        +askCustomerId() int
        +askChefTask() int
    }
    class ArrivalSchedule {
        +customerArrivingOn(int turn) Customer
    }
    class Restaurant {
        -revenueCents: int
        -happyHour: HappyHour
        +addCustomer(Customer c) void
        +seatCustomer(int id) String
        +findCustomer(int id) Customer
        +addRevenue(int cents) void
        +getPaidCount() int
        +getAverageSatisfaction() double
    }
    class Waiter {
        +seat(Restaurant r, int id) String
        +takeOrder(Restaurant r, int id) String
        +serve(Restaurant r, int id, int turn) String
    }
    class Chef {
        +prepare(Restaurant r, int id) String
    }
    class Host {
        +seat(Restaurant r, int id) String
    }
    class Cashier {
        +collectPayment(Restaurant r) String
    }
    class Customer {
        -id: int
        -satisfaction: int
        -state: CustomerState
        -servedTurn: int
        +getSatisfactionLoss() int
        +calculateBill(int baseCents) int
        +chooseItem(Menu menu) MenuItem
        +sitAt(Table t) void
        +placeOrder(Order o) void
        +receiveFood(int turn) void
        +finishEating() void
        +pay() void
        +leaveUnhappy() void
        +loseSatisfaction() void
        +recoverSatisfaction(int amount) void
        +isWaitingForService() boolean
    }
    class VIPCustomer {
        +getSatisfactionLoss() int
        +calculateBill(int baseCents) int
    }
    class CriticCustomer {
        +getSatisfactionLoss() int
        +chooseItem(Menu menu) MenuItem
    }
    class Table {
        -number: int
        +isFree() boolean
        +seat(Customer c) void
        +free() void
    }
    class Order {
        -priceCents: int
        -unitsDone: int
        -status: OrderStatus
        +prepareOneUnit() void
        +markServed() void
        +markPaid() void
        +cancel() void
    }
    class Menu {
        +addItem(MenuItem item) void
        +getItem(int index) MenuItem
        +size() int
        +getMostExpensive() MenuItem
    }
    class MenuItem {
        -name: String
        -priceCents: int
        -prepUnits: int
    }
    class ComboMeal {
        -first: MenuItem
        -second: MenuItem
    }
    class HappyHour {
        +RECOVERY: int$ = 15
        -used: boolean
        -turnsLeft: int
        +canActivate() boolean
        +activate() void
        +isActive() boolean
        +endTurn() void
        +applyDiscount(int cents) int
    }
    class Money {
        +format(int cents)$ String
        +percent(int cents, int pct)$ int
    }
    class CustomerState {
        <<enumeration>>
        WAITING
        SEATED
        ORDERED
        EATING
        READY_TO_PAY
        PAID
        LEFT
    }
    class OrderStatus {
        <<enumeration>>
        PREPARING
        READY
        SERVED
        PAID
        CANCELLED
    }

    Main ..> GameController : creates
    GameController --> "1" ConsoleUI
    GameController --> "1" Restaurant
    GameController *-- "1" ArrivalSchedule
    GameController *-- "1" Waiter
    GameController *-- "1" Chef
    GameController *-- "1" Cashier
    GameController *-- "1" Host

    Restaurant *-- "1" Menu
    Restaurant *-- "2" Table
    Restaurant *-- "1" HappyHour
    Restaurant o-- "0..6" Customer

    Customer <|-- VIPCustomer
    Customer <|-- CriticCustomer
    Customer "0..1" -- "0..1" Table : sits at
    Customer --> "0..1" Order
    Customer --> CustomerState
    Order --> "1" MenuItem
    Order --> OrderStatus
    Menu o-- "1..*" MenuItem
    MenuItem <|-- ComboMeal
    ComboMeal o-- "2" MenuItem : made of

    ArrivalSchedule ..> Customer : creates
    Waiter ..> Restaurant
    Chef ..> Restaurant
    Host ..> Restaurant
    Waiter ..> HappyHour : applyDiscount
    Cashier ..> Restaurant

    note for Order "ENCAPSULATION: status only changes through<br>prepareOneUnit() / markServed() / markPaid() / cancel()"
    note for Customer "POLYMORPHISM: VIP and Critic override<br>getSatisfactionLoss() / calculateBill() / chooseItem().<br>GameController has no type checks."
    note for ComboMeal "INHERITANCE + COMPOSITION: a ComboMeal IS a MenuItem<br>made of two MenuItems (price 90% of sum, units = sum)"
    note for Restaurant "COMPOSITION: Restaurant owns its Menu, Tables and HappyHour"

    style Host fill:#d9f2e3,stroke:#2e8b57
    style ComboMeal fill:#d9f2e3,stroke:#2e8b57
    style HappyHour fill:#d9f2e3,stroke:#2e8b57
```

## Stage 2 sequence diagram

**Use case (Stage 2): serve a VIP customer from arrival to payment.**

- **Run:** `stage-2/tests/victory.txt`, turns 4-8 (customer C2, a VIP).
- **Starting conditions:** turn 4, Table 2 is free, C1 is eating at Table 1.
- **Outcome:** C2 pays $13.50 (Burger $15.00 - 10% VIP discount) with satisfaction 90 on turn 8.
  At the end of turn 18 the result check gives VICTORY (5 paid, $73.50, avg 75.6).

**How the interactions implement it**

1. **Arrival** - `GameController` asks `ArrivalSchedule` who arrives; on turn 4 it creates a `VIPCustomer`.
2. **Manager choice** - the manager tells the Waiter to seat C2 straight away. The loop shows that an invalid
   choice (no free table, wrong customer) is rejected and asked again without using up the task.
   Seating at once matters: every turn C2 waits costs satisfaction.
3. **Stage 2 behaviour (polymorphism)** - `GameController` just calls `loseSatisfaction()`; the VIP object
   answers `getSatisfactionLoss() = 5` (a Regular would lose 8). When the order is taken the `Waiter` calls
   `calculateBill(1500)` and the VIP returns 1350. No `instanceof` checks anywhere.
4. **Kitchen** - the Chef does one unit; the 1-unit Burger becomes READY. The Waiter already acted this turn,
   so it is served on turn 6 (guard: `status == READY`).
5. **Eating and payment** - on turn 7 the guard `servedTurn < turn` moves C2 to READY_TO_PAY; on turn 8 the
   Cashier picks the oldest READY_TO_PAY customer, adds the locked price to revenue and frees the table.
6. **Result** - after turn 18 `GameController` checks the three targets and shows VICTORY or DEFEAT.

```mermaid
---
title: "Stage 2 use case - Serve a VIP customer from arrival to payment (stage-2/tests/victory.txt, turns 4-8)"
---
sequenceDiagram
    autonumber
    actor M as Manager
    participant UI as :ConsoleUI
    participant GC as :GameController
    participant AS as :ArrivalSchedule
    participant R as :Restaurant
    participant W as :Waiter
    participant CH as :Chef
    participant CA as :Cashier
    participant V as c2:VIPCustomer
    participant O as o:Order

    Note over M,O: Start: turn 4, Table 2 is free, C1 is eating at Table 1.

    loop turn = 1..18 (turns 4-8 shown)
        rect rgb(240,240,240)
        Note over GC: TURN 4 - VIP arrives and is seated
        GC->>AS: customerArrivingOn(4)
        AS-->>GC: c2 = new VIPCustomer(2)
        GC->>R: addCustomer(c2)
        GC->>UI: showTurnStart(4, r)
        loop until the Waiter choice is valid
            GC->>UI: askWaiterTask(), askCustomerId()
            M->>UI: 1 (Seat), 2
            UI-->>GC: 1, 2
            GC->>W: seat(r, 2)
            W->>R: seatCustomer(2)
            alt no free table / c2 not WAITING
                R-->>GC: IllegalArgumentException
                GC->>UI: showError(msg) - choose again, task not used
            else Table 2 free
                R->>V: sitAt(table2)
                Note right of V: SEATED
            end
        end
        GC->>V: loseSatisfaction()
        V->>V: getSatisfactionLoss()
        Note right of V: VIP override returns 5 (Regular = 8)<br>100 -> 95
        end

        rect rgb(240,240,240)
        Note over GC: TURN 5 - order taken with VIP price, Chef cooks
        GC->>UI: askWaiterTask(), askCustomerId()
        M->>UI: 2 (Take order), 2
        GC->>W: takeOrder(r, 2)
        W->>V: chooseItem(menu)
        V-->>W: Burger (rule: item (2-1) mod 3)
        W->>V: calculateBill(1500)
        Note right of V: VIP override: 10% off
        V-->>W: 1350
        W->>O: new Order(Burger, 1350)
        W->>V: placeOrder(o)
        Note right of V: ORDERED, price locked at $13.50
        GC->>UI: askChefTask()
        M->>UI: 2
        GC->>CH: prepare(r, 2)
        alt order is PREPARING
            CH->>O: prepareOneUnit()
            Note right of O: 1/1 units -> READY
        else no order / not PREPARING
            CH-->>GC: IllegalArgumentException (ask again)
        end
        GC->>CA: collectPayment(r)
        Note right of CA: C1 pays this turn (oldest READY_TO_PAY)
        GC->>V: loseSatisfaction()
        Note right of V: still waiting for food: 95 -> 90
        end

        rect rgb(240,240,240)
        Note over GC: TURN 6 - serve (order was READY before the Waiter acted)
        M->>UI: 3 (Serve), 2
        GC->>W: serve(r, 2, 6)
        alt order status == READY
            W->>V: receiveFood(6)
            V->>O: markServed()
            Note right of V: EATING, satisfaction stops dropping
        end
        end

        rect rgb(240,240,240)
        Note over GC: TURN 7 - finish eating
        GC->>R: getCustomersInState(EATING)
        opt c2.getServedTurn() < turn (6 < 7)
            GC->>V: finishEating()
            Note right of V: READY_TO_PAY (pays next turn at the earliest)
        end
        end

        rect rgb(240,240,240)
        Note over GC: TURN 8 - automatic payment, oldest first
        GC->>CA: collectPayment(r)
        loop each customer in arrival order
            opt state == READY_TO_PAY (first one only)
                CA->>V: getOrder().getPriceCents()
                V-->>CA: 1350
                CA->>R: addRevenue(1350)
                CA->>V: pay()
                V->>O: markPaid()
                Note right of V: PAID, Table 2 freed,<br>satisfaction 90 recorded
            end
        end
        end
    end

    Note over GC: After turn 18 - result check
    GC->>R: getPaidCount(), getRevenueCents(), getAverageSatisfaction()
    alt paid >= 4 AND revenue >= $45 AND avg >= 60
        GC->>UI: showResult(true, r)
        Note over UI: VICTORY (5 paid, $73.50, avg 75.6)
    else
        GC->>UI: showResult(false, r)
        Note over UI: DEFEAT
    end
```
