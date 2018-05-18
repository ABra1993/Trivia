package amber.promo.trivia;

import java.io.Serializable;

public class RandomQuestion implements Serializable {
    /** The following class stores a random question */

    // initialized properties
    private String correctAnswer, question;
    private String[] answers;

    // setters
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    // getters
    public String getCorrectAnswer() {


        return correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }
}
