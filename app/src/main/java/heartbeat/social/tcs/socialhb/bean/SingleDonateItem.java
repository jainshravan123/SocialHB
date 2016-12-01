package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 21/11/16.
 */
public class SingleDonateItem {

    private int id;
    private User user;
    private DonateCategory donateCategory;
    private int noOfItems;
    private DonateItemStatus donateItemStatus;
    private String description;
    private String submission_date;
    private String view_by_admin;
    private String last_processed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DonateCategory getDonateCategory() {
        return donateCategory;
    }

    public void setDonateCategory(DonateCategory donateCategory) {
        this.donateCategory = donateCategory;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }

    public DonateItemStatus getDonateItemStatus() {
        return donateItemStatus;
    }

    public void setDonateItemStatus(DonateItemStatus donateItemStatus) {
        this.donateItemStatus = donateItemStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(String submission_date) {
        this.submission_date = submission_date;
    }

    public String getView_by_admin() {
        return view_by_admin;
    }

    public void setView_by_admin(String view_by_admin) {
        this.view_by_admin = view_by_admin;
    }

    public String getLast_processed() {
        return last_processed;
    }

    public void setLast_processed(String last_processed) {
        this.last_processed = last_processed;
    }
}
