package snake;

public class MainApp {

    private static GameView gameView = null;
    private static boolean PopupOpen;

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

            // Opretter og viser spilvinduet
            startGame(n,m, gameView);

        } catch (NumberFormatException e) {
            // Håndterer tilfælde hvor input ikke er heltal
            System.out.println("Brug: java -jar snake.jar <n> <m>  (n og m skal være heltal)");
            System.exit(1);
        }
    }

    public static void startGame(int n, int m, GameView gameView) {
        // If the GameView does not exist already
        if (gameView == null) {
            gameView = new GameView(n, m);

        // If the GameView exists already
        } else {
            gameView.CloseGameView();
            gameView = new GameView(n, m);
        }
    }

    // Change the state of whether the popup menu is open or not
    public static void PopupStateChange(boolean value){
        PopupOpen = value;
    }
    
    // Get the state of whether the popup menu is open
    public static boolean getPopupState(){return PopupOpen;}
}
