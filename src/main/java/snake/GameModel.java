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
        this.rows = n;
        this.cols = m;
        reset();
    }

    // TODO: Implementér reset(): placer startslangen (længde 2) i midten af brættet
    // TODO: Implementér reset(): nulstil score, direction og gameOver
    // TODO: Implementér reset(): placer mad på en tilfældig tom celle
    // TODO: Implementér step(Direction): én spilrunde pr. tastetryk
    // TODO: Håndtér kollision med slangen og wrap-around (torus)

    // nulstiller spillet til starttilstanden
    public void reset() {
        // ...
    }
}
