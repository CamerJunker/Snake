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
    private final Deque<Cell> prevSnake = new ArrayDeque<>();

    // Mure og ormehuller
    private final Set<Cell> walls = new HashSet<>();
    private final Map<Cell, Cell> wormholes = new HashMap<>();
    private boolean wallsEnabled = false;
    private boolean wormholesEnabled = false;

    // den aktuelle spilstatus
    private Direction dir = Direction.LEFT;
    private Cell food;
    private int score = 0;
    private GameState state = GameState.PLAYING;
    private long startTimeMs;
    private long lastStepTimeMs = System.currentTimeMillis();
    private int stepDelayMs = 100;
    private long elapsedTime;
    private boolean teleportedLastStep = false;

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
        walls.clear();
        wormholes.clear();

        // Midterposition på spillebrættet
        int r0 = rows / 2;
        int c0 = cols / 2;

        // Slangens hoved placeres i midten
        Cell head = new Cell(r0, c0);

        // Andet led placeres direkte under hovedet (wrap-around hvis nødvendigt)
        Cell second = new Cell((r0 + 1) % rows, c0);

        // Beregn næste position for at undgå at placere mur der
        int nextC = wrapCol(c0 - 1);
        Cell nextHead = new Cell(r0, nextC);

        // hovedet skal ligge forrest i Deque/rækkefølgen
        snake.addFirst(head);
        snake.addLast(second);

        // Opdater mængden af optagne felter til kollisionskontrol
        occupied.add(head);
        occupied.add(second);
        snapshotSnake();

        // Tilføj mure og ormehuller hvis aktiveret
        if (wallsEnabled) {
            generateWalls(head, second, nextHead);
        }
        if (wormholesEnabled) {
            generateWormholes(head, second, nextHead);
        }

        // Reset game score
        this.score = 0;

        // Reset hvilken retning slangen starter med at gå
        this.dir = Direction.LEFT;

        // Reset game over
        this.state = GameState.PLAYING;
        this.startTimeMs = System.currentTimeMillis();
        this.lastStepTimeMs = this.startTimeMs;
        this.elapsedTime = 0;

        // Issue 3: Sæt maden tilfældigt udenfor slangen
        this.setFood();
    }

    /**
    * Udfører én spilrunde baseret på brugerens input.
    * @param requested den ønskede retning (fra controller). Ignoreres hvis null eller modsatrettet.
    */
    public void step(Direction requested) {
        if (state != GameState.PLAYING) return;
        snapshotSnake();
        teleportedLastStep = false;

        //opdater retning men forbyder en 180 graders vending
        if (requested != null && requested != dir.opposite()) {
            dir = requested;
        }

        // nuværende hoved og hale
        Cell head = snake.peekFirst();
        Cell tail = snake.peekLast();

        // beregn næste hovedposition (wrap-around, men mure begrænser)
        int nextR = wrapRow(head.r() + dir.dr);
        int nextC = wrapCol(head.c() + dir.dc);
        Cell nextHead = new Cell(nextR, nextC);

        // Tjek om på mur
        if (walls.contains(nextHead)) {
            state = GameState.GAME_OVER;
            return;
        }

        boolean teleported = false;
        // Tjek ormehuller
        if (wormholes.containsKey(nextHead)) {
            nextHead = wormholes.get(nextHead);
            teleported = true;
        }

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

        // Hvis vi har teleporteret, så snap interpolation til den nye position
        if (teleported) {
            snapshotSnake();
            teleportedLastStep = true;
        }

        // Tjek om spilleren har vundet (slangen fylder alle tilladte felter)
        if (snake.size() >= getAvailableCellCount()) {
            state = GameState.WON;
            return;
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
        if (occupied.size() >= getAvailableCellCount()) {
            state = GameState.WON;
            return;
        }

        List<Cell> free = new ArrayList<>(rows * cols - occupied.size());
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell candidate = new Cell(r, c);
                if (!occupied.contains(candidate) && !walls.contains(candidate) && !wormholes.containsKey(candidate)) {
                    free.add(candidate);
                }
            }
        }

        if (free.isEmpty()) {
            state = GameState.WON;
            return;
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

    public Iterable<Cell> getPrevSnake() {
        return Collections.unmodifiableCollection(prevSnake);
    }
    
    public Cell getFood() { return food; }
    public int getScore() { return score; }
    public GameState getState() { return state; }
    public boolean isGameOver() { return state == GameState.GAME_OVER; }
    public Direction getDirection() { return dir; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public long getLastStepTimeMs() { return lastStepTimeMs; }
    public int getStepDelayMs() { return stepDelayMs; }
    public long getElapsedSeconds() {
        if (state == GameState.PLAYING) {
            return this.elapsedTime + (System.currentTimeMillis() - startTimeMs) / 1000;
        } else {
            return this.elapsedTime;
        }
        
    }
    public Iterable<Cell> getWalls() {
        return Collections.unmodifiableCollection(walls);
    }
    public Map<Cell, Cell> getWormholes() {
        return Collections.unmodifiableMap(wormholes);
    }
    public boolean didTeleportLastStep() {
        return teleportedLastStep;
    }

    public void pause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
            // Add time passed
            this.elapsedTime += (System.currentTimeMillis() - startTimeMs) / 1000;
        }
    }

    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
            // Set start time to now
            startTimeMs = System.currentTimeMillis();
        }
    }

    public void setLastStepTimeMs(long lastStepTimeMs) {
        this.lastStepTimeMs = lastStepTimeMs;
    }

    public void setStepDelayMs(int stepDelayMs) {
        if (stepDelayMs > 0) {
            this.stepDelayMs = stepDelayMs;
        }
    }

    public boolean isWallsEnabled() {
        return wallsEnabled;
    }

    public boolean isWormholesEnabled() {
        return wormholesEnabled;
    }

    public void setWallsEnabled(boolean wallsEnabled) {
        this.wallsEnabled = wallsEnabled;
    }

    public void setWormholesEnabled(boolean wormholesEnabled) {
        this.wormholesEnabled = wormholesEnabled;
    }

    private void snapshotSnake() {
        prevSnake.clear();
        prevSnake.addAll(snake);
    }

    private int getAvailableCellCount() {
        return rows * cols - walls.size() - wormholes.size();
    }

    private void generateWalls(Cell head, Cell second, Cell nextHead) {
        int interiorRows = Math.max(0, rows - 2);
        int interiorCols = Math.max(0, cols - 2);
        int interiorCells = interiorRows * interiorCols;
        int desiredWalls = Math.max(8, (rows * cols) / 15);
        int maxWalls = Math.max(0, interiorCells - 3);
        int targetWalls = Math.min(desiredWalls, maxWalls);

        if (targetWalls <= 0) return;

        List<Cell> candidates = new ArrayList<>(interiorCells);
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                Cell candidate = new Cell(r, c);
                if (candidate.equals(head) || candidate.equals(second) || candidate.equals(nextHead)) {
                    continue;
                }
                candidates.add(candidate);
            }
        }

        Collections.shuffle(candidates, rng);
        for (int i = 0; i < Math.min(targetWalls, candidates.size()); i++) {
            walls.add(candidates.get(i));
        }
    }

    private void generateWormholes(Cell head, Cell second, Cell nextHead) {
        int desiredPairs = Math.max(1, Math.min(4, (rows * cols) / 200));
        List<Cell> free = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell candidate = new Cell(r, c);
                if (candidate.equals(head) || candidate.equals(second) || candidate.equals(nextHead)) {
                    continue;
                }
                if (occupied.contains(candidate) || walls.contains(candidate)) {
                    continue;
                }
                free.add(candidate);
            }
        }

        Collections.shuffle(free, rng);
        int pairs = Math.min(desiredPairs, free.size() / 2);
        for (int i = 0; i < pairs; i++) {
            Cell a = free.get(i * 2);
            Cell b = free.get(i * 2 + 1);
            wormholes.put(a, b);
            wormholes.put(b, a);
        }
    }
}
