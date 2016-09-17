package heartbeat.social.tcs.socialhb.bean;

import java.util.ArrayList;

/**
 * Created by admin on 05/09/16.
 */
public class QuizQuestionAnswer
{
    private QuizQuestion            quizQuestion;
    private ArrayList<QuizAnswer> quizAnswer_list;

    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public ArrayList<QuizAnswer> getQuizAnswer_list() {
        return quizAnswer_list;
    }

    public void setQuizAnswer_list(ArrayList<QuizAnswer> quizAnswer_list) {
        this.quizAnswer_list = quizAnswer_list;
    }
}
