package snake;
// MVC afdeling: Model
// Repræsenterer de 4 forskællige retninger slangen kan bevæge sig

public enum Direction {
    UP(-1, 0), // Flytter sig opad så række/row falder
    DOWN(1, 0), // Flytter sig nedad så række/row vokser
    LEFT(0, -1), // flytter sig til venstre så kolonne/column falder
    RIGHT(0, 1); // Flytter sig til højre så kolonne/kolumn vokser

    // Ændring i række (dr) og kolonne (dc) for bevægelsen.
    public final int dr, dc;

    // Opretter en bevægelsesretning med tilhørende ændringer i koordinater.
    Direction(int dr, int dc) {
        this.dr = dr;
        this.dc = dc;
    }

    // returnerer den modsatte bevægelsesretning, så at slangen ikke kan vende sig 180 grader i et træk
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
