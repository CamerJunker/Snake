package snake;

public class GameModel {
    // Variabel for spillers highscore
    private int gameScore;

    // Lav en GameModel object
    public GameModel(){
        // Reset variabler
        reset();
    }

    // Spiller får et point
    public void incScore(){
        gameScore++;
    }

    // Giver nuværende highscore
    public void getScore() {
        return gameScore;
    }

    // Sæt highscore til 0 når spiller taber
    public void reset(){
        gameScore = 0;
    }


    



}
