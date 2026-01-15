package snake;

import java.util.*;

/**
 * Model (MVC): Indeholder spiltilstand og grundlæggende data for Snake.
 */


public final class GameModel {
    // spillebrættets størrelse (ændres ikke efter konstruktion)
    private final int rows; // n
    private final int cols; // m

    // Slangen gemmes i rækkefølge(Deque) og som mængde(Set) for kollisionskontrol
    private final Deque<Cell> snake = new ArrayDeque<>();
    private final Set<Cell> occupied = new HashSet<>();

    // den aktuelle spilstatus
    private Direction dir = Direction.LEFT;
    private Cell food;
    private int score = 0;
    private GameState state = GameState.PLAYING;

    // random generator til at placere maden tilfældigt
    private final Random rng = new Random();

    // Opretter modellen og initialiserer spillet
    public GameModel(int n, int m) {
        //inputvalidering som gør koden mere robust
        if (n < 5 || n > 100 || m < 5 || m > 100) {
            throw new IllegalArgumentException( "n og m skal være i [5..100]");
        }

        // Lav spilbræt
        this.rows = n;
        this.cols = m;

        // Reset alle relevante variabler
        reset();
    }

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
        this.state = GameState.PLAYING;

        // Issue 3: Sæt maden tilfældigt udenfor slangen
        this.setFood();
    }

    /**
    * Udfører én spilrunde baseret på brugerens input.
    * @param requested den ønskede retning (fra controller). Ignoreres hvis null eller modsatrettet.
    */
    public void step(Direction requested) {
        if (state != GameState.PLAYING) return;

        //opdater retning men forbyder en 180 graders vending
        if (requested != null && requested != dir.opposite()) {
            dir = requested;
        }

        // nuværende hoved og hale
        Cell head = snake.peekFirst();
        Cell tail = snake.peekLast();

        // beregn næste hovedposition (wrap around håndteres)
        int nextR = wrapRow(head.r() + dir.dr);
        int nextC = wrapCol(head.c() + dir.dc);
        Cell nextHead = new Cell(nextR, nextC);

        // tjek om slangen spiser mad i dette step
        boolean grows = nextHead.equals(food);

        // Kollisionsregelen
        // hvis næste felt er occupied af slangen -> gameOver
        // undtagelse hvis feltet er occupied af slangens hale og slangen IKKE vokser
        if (occupied.contains(nextHead)) {
            boolean intoTail = nextHead.equals(tail);
            if (!(intoTail && !grows)) {
                state = GameState.GAME_OVER;
                return;
            }
        }

        // Flytter -> lægger nyt hoved på
        snake.addFirst(nextHead);
        occupied.add(nextHead);

        // Hvis slangen vokser
        if (grows) {
            // spiser mad: score++ og placérer ny mad. slangen vokser så halen fjernes ikke.
            score++;
            setFood();
        } else {
            // en normal bevægelse -> fjerner halen fordi slangen vokser ikke, men rykker sig og slangen har rykket sig ét felt
            Cell removed = snake.removeLast();
            occupied.remove(removed);
        }
    }

    // Wrap-around for række (torus)
    private int wrapRow(int r) {
        return (r % rows + rows) % rows;
    }

    // Wrap-around for kolonne (torus)
    private int wrapCol(int c) {
        return (c % cols + cols) % cols;
    }

    //Placér mad tilfældigt, udenfor slangens krop
    private void setFood() {
        if (occupied.size() == rows * cols) {
            state = GameState.GAME_OVER;
            return;
        }

        List<Cell> free = new ArrayList<>(rows * cols - occupied.size());
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell candidate = new Cell(r, c);
                if (!occupied.contains(candidate)) {
                    free.add(candidate);
                }
            }
        }

        int pick = rng.nextInt(free.size());
        this.food = free.get(pick);
    }

    //getters så at Controller/View også kan bruge modellen
    // getSnake gjort til read-only: ikke krav eller nødvendigt men er personlig præference fordi det er mere stabilt og, 
    // sikrer at værdien ikke kan ændres af ekstern kode og dermed forhindrer en utilsigtet ændring som kunne føre til bugs
    public Iterable<Cell> getSnake() { 
        return Collections.unmodifiableCollection(snake); 
    }
    public Cell getFood() { return food; }
    public int getScore() { return score; }
    public GameState getState() { return state; }
    public boolean isGameOver() { return state == GameState.GAME_OVER; }
    public Direction getDirection() { return dir; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }
}