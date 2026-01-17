package snake;

public class MainApp {

    public static void main(String[] args) {
        // Tjekker at der er givet præcis to argumenter (n og m)
        if (args.length != 2) {
            System.out.println("Brug: java -jar snake.jar <n> <m>");
            System.exit(1);
        }

        try {
            // Konverterer kommandolinjeparametre fra String til heltal
            int n = Integer.parseInt(args[0]);
            int m = Integer.parseInt(args[1]);

            // Opretter model, view og controller (MVC wiring)
            GameModel model = new GameModel(n, m);
            SnakePanel panel = new SnakePanel(model);
            GameController controller = new GameController(model, panel);
            new GameView(panel, controller);

        } catch (NumberFormatException e) {
            // Håndterer tilfælde hvor input ikke er heltal
            System.out.println("Brug: java -jar snake.jar <n> <m>  (n og m skal være heltal)");
            System.exit(1);
        }
    }
}