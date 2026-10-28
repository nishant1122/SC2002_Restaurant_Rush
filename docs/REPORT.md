# Restaurant Rush - Report (draft)

> Copy this into your PDF report. Diagrams are in `docs/diagrams.md`.
> Sections marked TODO need your group's own words (reflections, names).

## 1. Key abstractions and responsibilities

| Class | Type | Responsibility |
|---|---|---|
| `Main` | setup | Builds the menu and restaurant, starts the game. |
| `GameController` | control | Runs 18 turns and applies the six steps in order. Asks the UI for choices and hands them to staff. Checks victory. |
| `ConsoleUI` | boundary | Only class that prints or reads input. Validates that input is a number / y-n, but holds **no game rules**. |
| `Restaurant` | entity (aggregate) | Holds game state: menu, tables, customers, Happy Hour, revenue. Seats customers, computes statistics. |
| `ArrivalSchedule` | entity | Decides who arrives on which turn and of what type. |
| `Customer` / `VIPCustomer` / `CriticCustomer` | entity | Satisfaction, state, food choice and bill. Subclasses change behaviour by overriding. |
| `Table` | entity | Holds at most one customer. |
| `Order` | entity | Item, locked price, preparation progress, status. |
| `Menu`, `MenuItem`, `ComboMeal` | entity | What can be ordered. A combo is a menu item made of two items. |
| `Waiter`, `Chef`, `Cashier`, `Host` | entity (staff) | Each performs its own task and refuses invalid ones with a clear message. |
| `HappyHour` | entity (policy) | Tracks once-per-game activation, remaining turns, and the 20% discount. |
| `Money` | utility | Cents formatting and percentage rounding. |
| `CustomerState`, `OrderStatus` | enums | Named states instead of magic strings. |

## 2. OO concepts in the code

- **Encapsulation** - all fields are `private`. An `Order`'s status can only change through
  `prepareOneUnit()`, `markServed()`, `markPaid()`, `cancel()`; its price is `final`.
  A `Customer`'s satisfaction can't go below 0 or above 100 because it only changes through
  `loseSatisfaction()` / `recoverSatisfaction()`.
- **Inheritance** - `VIPCustomer` and `CriticCustomer` extend `Customer`; `ComboMeal` extends `MenuItem`.
- **Polymorphism** - `GameController` calls `c.loseSatisfaction()` and the `Waiter` calls
  `customer.chooseItem(menu)` and `customer.calculateBill(price)` on *any* customer.
  The right version runs automatically. There is no `if (c instanceof VIPCustomer)` in the controller.
- **Composition** - `Restaurant` owns its `Table`s, `Menu` and `HappyHour`; `GameController` owns
  its staff; `ComboMeal` is built from two `MenuItem`s.

## 3. SOLID (only where it really applies)

- **SRP** - UI only does input/output; `GameController` only does turn order; each staff class does
  one job; `HappyHour` only does the pricing policy; `ArrivalSchedule` only decides arrivals.
- **OCP** - Stage 2 added two customer types **without changing** `GameController`, `Waiter` or
  `Cashier`: new behaviour came from new subclasses. Stage 3's `ComboMeal` needed no change to
  `Order`, `Chef` or `Menu` because it is a `MenuItem`.
- **LSP** - a `VIPCustomer`, `CriticCustomer` or `ComboMeal` can be used anywhere its parent is
  expected and the game still works (same order/service flow).
- **ISP / DIP** - we did **not** add interfaces, because the brief says not to add them just to tick
  a box and our classes have only one implementation each. (Honest note: DIP is not really applied -
  `GameController` creates concrete staff objects. See trade-offs.)

## 4. Trade-offs and an alternative design

- **Waiting queue is derived, not stored.** The queue is "customers in state WAITING, in arrival
  order". This avoids keeping a separate list in sync. Trade-off: it is recomputed each time
  (fine for 6 customers).
- **Exceptions for invalid choices.** Staff throw `IllegalArgumentException` with a message; the
  controller catches it and asks again, so a mistake never uses up the task.
- **Alternative considered: a `Staff` superclass / `Task` objects (Command pattern).** Every staff
  action would be a `Task` object (`SeatTask`, `ServeTask`...) with `isValid()` and `execute()`.
  This is more extensible (e.g. undo, more roles) but adds many small classes. For a fixed set of
  four roles we chose simple methods on each staff class.
- **Alternative considered: Happy Hour as a `PricingPolicy` interface.** Would allow more promos
  later, but we have only one policy, so a single class is simpler.
- **Change in Stage 3:** `Waiter.takeOrder()` was edited to apply the Happy Hour price. Seating
  validation was already in `Restaurant.seatCustomer()` from Stage 1, so the new `Host` reuses it
  with a one-line method.

## 5. How the player's choices change the result

- **Seat vs serve vs order** - the Waiter has one task per turn. On turn 13 of the Stage 3 run, C5 has just
  been seated and wants to order, while the Critic's combo is READY. Serving the Critic first stops them losing
  12 points per turn; serving late would drop the average.
- **Kitchen priority** - the Chef can only work on one order per turn; cooking the impatient Critic
  first protects the average satisfaction.
- **Happy Hour timing** - cheaper bills (less revenue) but +15 satisfaction for waiting customers.
  Turning it on when a long Pasta order is cooking (turn 7) keeps C3 at 92 instead of 84.
- **Doing nothing** - the defeat runs show that if orders are never taken, customers lose
  satisfaction every turn until they reach 0 and leave without paying.

## 6. Functional evidence (test notes)

All runs use the base setting and are fully reproducible (no randomness):

```
cd stage-3
javac -d out src/*.java && java -cp out Main < tests/victory.txt
```
(same for `stage-1/` and `stage-2/`; each folder is a complete runnable game)

| Run | Script | Result |
|---|---|---|
| Stage 1 victory | `stage-1/tests/victory.txt` | 5 paid, $66.00, avg 79.2 -> VICTORY. Also shows two invalid inputs being rejected. |
| Stage 1 defeat | `stage-1/tests/defeat.txt` | Waiter seats C1, C2 and never takes orders. 0 paid -> DEFEAT. |
| Stage 2 victory | `stage-2/tests/victory.txt` | VIP C2 pays $13.50 (10% off), Critic C4 orders Pasta. 5 paid, $73.50, avg 75.6 -> VICTORY. |
| Stage 2 defeat | `stage-2/tests/defeat.txt` | Same neglect -> DEFEAT. |
| Stage 3 victory | `stage-3/tests/victory.txt` | Happy Hour on turn 7 (Pasta $14.40), Critic orders Salad+Pasta Combo $24.30, C5 orders Burger+Salad Combo $21.60, Host seats every customer. 5 paid, $82.80, avg 83.8 -> VICTORY. |
| Stage 3 defeat | `stage-3/tests/defeat.txt` | Host seats C1, C2, nobody takes orders -> customers leave -> DEFEAT. |

Food choice rule: customer #n orders menu item number `(n-1) mod menuSize`
(Stage 1/2 menu: Salad, Burger, Pasta. Stage 3 adds Salad+Pasta Combo and Burger+Salad Combo,
so C4/C5 now land on combos). The Critic always orders the most expensive item.

Customer types (Stage 2+): C2 (turn 4) is VIP, C4 (turn 10) is Critic, the rest are Regular.

## 7. Reflection

TODO: each member writes 3-5 sentences - what they built, what was hard, what they'd change.
