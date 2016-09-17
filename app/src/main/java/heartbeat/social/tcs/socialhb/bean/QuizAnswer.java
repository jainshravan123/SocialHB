package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 05/09/16.
 */
public class QuizAnswer
{

    private int id;
    private String answer;
    private QuizQuestion question;
    private int right_ans;

    public int getRight_ans() {
        return right_ans;
    }

    public void setRight_ans(int right_ans) {
        this.right_ans = right_ans;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
