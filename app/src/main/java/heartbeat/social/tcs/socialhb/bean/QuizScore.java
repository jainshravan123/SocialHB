package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 05/09/16.
 */
public class QuizScore
{

    private int id;
    private String quiz_id;
    private User user;
    private int area_of_interest_cat_id;
    private int score;
    private String start_time;
    private String completion_time;
    private int status;
    private int no_of_qus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getArea_of_interest_cat_id() {
        return area_of_interest_cat_id;
    }

    public void setArea_of_interest_cat_id(int area_of_interest_cat_id) {
        this.area_of_interest_cat_id = area_of_interest_cat_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNo_of_qus() {
        return no_of_qus;
    }

    public void setNo_of_qus(int no_of_qus) {
        this.no_of_qus = no_of_qus;
    }
}
