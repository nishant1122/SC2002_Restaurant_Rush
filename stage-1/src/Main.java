/** Starts the game with the base setting (2 tables, 3 menu items). */
public class Main {
    public static void main(String[] args) {
        MenuItem salad = new MenuItem("Salad", 900, 1);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);

        Menu menu = new Menu();
        menu.addItem(salad);
        menu.addItem(burger);
        menu.addItem(pasta);

        Restaurant restaurant = new Restaurant(menu, 2);
        new GameController(restaurant, new ConsoleUI()).play();
    }
}
