package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 05/09/16.
 */
public class QuizSelectedAnswer
{

    private QuizQuestion quizQuestion;
    private QuizAnswer   quizAnswer;

    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public QuizAnswer getQuizAnswer() {
        return quizAnswer;
    }

    public void setQuizAnswer(QuizAnswer quizAnswer) {
        this.quizAnswer = quizAnswer;
    }
}
