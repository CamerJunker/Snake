package snake;

public class MainApp {

    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 100;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Brug: java -jar snake.jar <n> <m>");
            System.exit(1);
        }

        try {
            int n = Integer.parseInt(args[0]);
            int m = Integer.parseInt(args[1]);

            if (n < MIN_SIZE || n > MAX_SIZE || m < MIN_SIZE || m > MAX_SIZE) {
                System.out.println("Fejl: n og m skal være mellem 5 og 100.");
                System.exit(1);
            }

            new GameView(n, m);

        } catch (NumberFormatException e) {
            System.out.println("Brug: java -jar snake.jar <n> <m>  (n og m skal være heltal)");
            System.exit(1);
        }
    }
}
