package snake;

public enum Difficulty {
    EASY("Let", 140, 1),
    NORMAL("Normal", 100, 2),
    HARD("Svær", 70, 3);

    private final String label;
    private final int baseDelayMs;
    private final int speedupMs;

    Difficulty(String label, int baseDelayMs, int speedupMs) {
        this.label = label;
        this.baseDelayMs = baseDelayMs;
        this.speedupMs = speedupMs;
    }

    public int getBaseDelayMs() {
        return baseDelayMs;
    }

    public int getSpeedupMs() {
        return speedupMs;
    }

    @Override
    public String toString() {
        return label;
    }
}
