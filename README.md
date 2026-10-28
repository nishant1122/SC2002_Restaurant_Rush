# Restaurant Rush (SC2002 Group Assignment)

A turn-based restaurant management game played in the command line. You are the manager:
each turn you decide what the Host, Waiter and Chef do.

**Repository:** TODO `https://github.com/<user>/<repo>`

## The three stages

Each stage is its own complete, runnable game in its own folder:

| Folder | What it contains | Diagram |
|---|---|---|
| `stage-1/` | Regular customers, menu, Waiter / Chef / Cashier, tables, full turn sequence, victory / defeat | [class diagram](docs/class-diagram-stage1.mmd) |
| `stage-2/` | Stage 1 + VIP and Critic customers | [class diagram](docs/class-diagram-stage2.mmd), [sequence diagram](docs/sequence-diagram-stage2.mmd) |
| `stage-3/` | Stage 2 + Host, ComboMeal, Happy Hour | [class diagram](docs/class-diagram-stage3.mmd) |

All diagrams with explanations: [docs/diagrams.md](docs/diagrams.md).
For PNGs, open `docs/view-diagrams.html` in a browser and click **Download PNG**.

## Build and run (Java 17 or newer, no internet needed)

Go into the stage folder you want (`stage-1`, `stage-2` or `stage-3`), then:

```
cd stage-1
javac -d out src/*.java
java -cp out Main
```

Reproducible scripted runs (feed a file instead of typing):

```
java -cp out Main < tests/victory.txt
java -cp out Main < tests/defeat.txt
```

| Run | Result |
|---|---|
| `stage-1/tests/victory.txt` | 5 paid, $66.00, avg 79.2 -> VICTORY |
| `stage-2/tests/victory.txt` | 5 paid, $73.50, avg 75.6 -> VICTORY (VIP C2 pays $13.50) |
| `stage-3/tests/victory.txt` | 5 paid, $82.80, avg 83.8 -> VICTORY (Happy Hour, combos, Host) |
| `stage-N/tests/defeat.txt` | 0 paid -> DEFEAT |

For the GitHub tags: commit `stage-1/` and tag it `stage-1`, then add `stage-2/` and tag `stage-2`,
then add `stage-3/` and tag `stage-3`.

## How to play

Each turn the game shows the state, then asks:
1. *(Stage 3)* `Start Happy Hour now? (y/n)` - only until you use it once.
2. *(Stage 3)* Host: customer number to seat, or `0`.
3. Waiter: `1` seat, `2` take order, `3` serve, `0` wait - then the customer number.
4. Chef: customer number whose order to cook, or `0`.

Invalid choices are explained and you choose again (the task is not used up).

**Win** after turn 18: >= 4 paid customers AND revenue >= $45 AND average satisfaction of paid
customers >= 60.

### Documented rules
- **Food choice:** customer #n orders menu item `(n-1) mod menuSize`. Critics always order the most expensive item.
- **Customer types (Stage 2+):** C2 (turn 4) = VIP, C4 (turn 10) = Critic, others Regular.
- **Combos (Stage 3):** Salad + Pasta Combo ($24.30, 3 units), Burger + Salad Combo ($21.60, 2 units). They are chosen by the same food rule.
- **Cashier "oldest first":** earliest-arriving customer who is READY_TO_PAY.

## Project structure

```
stage-1/src, stage-1/tests   Stage 1 game (16 classes) + victory/defeat input scripts
stage-2/src, stage-2/tests   Stage 2 game (18 classes)
stage-3/src, stage-3/tests   Stage 3 game (21 classes)
docs/                        3 class diagrams + Stage 2 sequence diagram (.mmd), diagrams.md,
                             view-diagrams.html (renders them / downloads PNG), REPORT.md
```

## Stage comparisons

| Tag | What it contains |
|---|---|
| `stage-1` | Regular customers, menu, Waiter/Chef/Cashier, tables, full turn sequence, victory/defeat |
| `stage-2` | + VIP and Critic customers |
| `stage-3` | + Host, ComboMeal, Happy Hour |

### Stage 1 -> Stage 2
Compare: `diff -r stage-1/src stage-2/src` (or TODO `https://github.com/<user>/<repo>/compare/stage-1...stage-2`)

| File | Change | Why |
|---|---|---|
| `VIPCustomer.java` | **new** | Overrides `getSatisfactionLoss()` (5) and `calculateBill()` (10% off). |
| `CriticCustomer.java` | **new** | Overrides `getSatisfactionLoss()` (12) and `chooseItem()` (most expensive item). |
| `ArrivalSchedule.java` | changed | Registration only: create a VIP for C2 and a Critic for C4. |
| `Menu.java` | changed | Added `getMostExpensive()` for the Critic's preference. |
| `Money.java` | changed | Added `percent()` helper (rounds to the nearest cent) for the VIP discount. |
| `tests/*.txt` | new | Reproducible runs including a VIP and a Critic. |

`GameController`, `Waiter`, `Chef`, `Cashier` and `ConsoleUI` are **unchanged**: they already called
`loseSatisfaction()`, `chooseItem()` and `calculateBill()` on `Customer`, so the new types
work through polymorphism with no type checks.

### Stage 2 -> Stage 3
Compare: `diff -r stage-2/src stage-3/src` (or TODO `https://github.com/<user>/<repo>/compare/stage-2...stage-3`)

| File | Change | Why |
|---|---|---|
| `Host.java` | **new** | Seats one queued customer per turn; reuses `Restaurant.seatCustomer()`. |
| `ComboMeal.java` | **new** | Extends `MenuItem`, built from two items (price 90% of sum, units summed). |
| `HappyHour.java` | **new** | Once-per-game, 2-turn policy: 20% off orders, +15 satisfaction for waiting customers. |
| `GameController.java` | changed | Added the Happy Hour step at turn start, the Host step before the Waiter, and `happyHour.endTurn()`. Needed because these are new steps in the turn. |
| `ConsoleUI.java` | changed | New prompts (Happy Hour y/n, Host) and Happy Hour status display. |
| `Waiter.java` | changed | `takeOrder()` applies the Happy Hour discount after the customer's own discount (VIP first). |
| `Restaurant.java` | changed | Owns the `HappyHour` object (part of the game state). |
| `Customer.java` | changed | Added `recoverSatisfaction()` (capped at 100) for Happy Hour. |
| `Main.java` | changed | Registration only: adds the two combos to the menu. |

No stable game rules were rewritten; the Stage 1/2 turn logic is the same, which the Stage 3
victory run shows (same payment/eating timing as before).

## Video timestamps

| # | Segment | Time | Presenter |
|---|---|---|---|
| 1 | Game launching from the submitted project | TODO | TODO |
| 2 | Manager choices with constrained staff | TODO | TODO |
| 3 | A dish READY -> SERVED -> payment | TODO | TODO |
| 4 | VIP or Critic behaviour | TODO | TODO |
| 5 | A Stage 3 feature | TODO | TODO |
| 6 | One victory and one defeat | TODO | TODO |
| 7 | Stage tag comparisons and diagrams | TODO | TODO |
