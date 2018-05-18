package amber.promo.trivia;

import java.io.Serializable;

public class Highscore implements Serializable {

    // initialize properties
    private String name;
    private float highScore, correct, incorrect;

    // initialize constructor for firebase
    public Highscore() {
    }

    // initialize constructor
    public Highscore(String name, float correct, float incorrect) {
        this.name = name;
        this.correct = correct;
        this.incorrect = incorrect;

        this.highScore = this.correct / (this.correct + this.incorrect) * 100;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setHighScore(float highScore) {
        this.highScore = highScore;
    }

    public void setCorrect(float correct) {
        this.correct = correct;
    }

    public void setIncorrect(float incorrect) {
        this.incorrect = incorrect;
    }

    // getters
    public String getName() {

        return name;
    }

    public float getHighScore() {
        return highScore;
    }

    public float getCorrect() {
        return correct;
    }

    public float getIncorrect() {
        return incorrect;
    }
}
