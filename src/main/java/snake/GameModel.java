package snake;

import java.util.*;

/**
 * Model (MVC): Indeholder spiltilstand og grundlæggende data for Snake.
 */


public final class GameModel {
    // spillebrættets størrelse (ændres ikke efter konstruktion)
    public final int rows; // n
    public final int cols; // m

    // Slangen gemmes i rækkefølge(Deque) og som mængde(Set) for kollisionskontrol
    private final Deque<Cell> snake = new ArrayDeque<>();
    private final Set<Cell> occupied = new HashSet<>();

    // den aktuelle spilstatus
    private Direction dir = Direction.LEFT;
    private Cell food;
    private int score = 0;
    private boolean gameOver = false;

    // random generator til at placere maden tilfældigt
    private final Random rng = new Random();

    // Opretter modellen og initialiserer spillet
    public GameModel(int n, int m) {
        // Lav spilbræt
        this.rows = n;
        this.cols = m;

        // Reset alle relevante variabler
        reset();
    }

    
    // TODO: Implementér step(Direction): én spilrunde pr. tastetryk
    // TODO: Håndtér kollision med slangen og wrap-around (torus)

    // nulstiller spillet til starttilstanden
    public void reset() {

        // Issue 1: opret startslange (længde 2) i midten af brættet
        snake.clear();
        occupied.clear();

        // Midterposition på spillebrættet
        int r0 = rows / 2;
        int c0 = cols / 2;

        // Slangens hoved placeres i midten
        Cell head = new Cell(r0, c0);

        // Andet led placeres direkte under hovedet (wrap-around hvis nødvendigt)
        Cell second = new Cell((r0 + 1) % rows, c0);

        // hovedet skal ligge forrest i Deque/rækkefølgen
        snake.addFirst(head);
        snake.addLast(second);

        // Opdater mængden af optagne felter til kollisionskontrol
        occupied.add(head);
        occupied.add(second);

        // Reset game score
        this.score = 0;

        // Reset hvilken retning slangen starter med at gå
        this.dir = Direction.LEFT;

        // Reset game over
        this.gameOver = false;

        // Issue 3: Sæt maden tilfældigt udenfor slangen
        this.setFood();
    }

    //Placér mad tilfældigt, udenfor slangens krop
    private void setFood() {

        // Find en celle der ikke er optaget af slangens krop
        int XVærdi, YVærdi;
        Cell FoodCell;

        // While-loop kører indtil ubrugt celle er fundet
        while(true){
            // Brug random generator til at lave nogle tilfældige koordinator
            YVærdi = rng.nextInt(this.rows);
            XVærdi = rng.nextInt(this.cols);
        
            // Initialisér celle med fundne koordinater
            FoodCell = new Cell(YVærdi, XVærdi);

            // Tjek om celle findes i slangens krop
            if (this.occupied.contains(FoodCell)) {
                continue;
            } else {
                break;
            }

        }

        // Initialisér mad i den fundne tilgængelige celle
        this.food = FoodCell;
    }

    //getters så at Controller/View også kan bruge modellen
    public Iterable<Cell> getSnake() { return snake; }
    public Cell getFood() { return food; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public Direction getDirection() { return dir; }
}
