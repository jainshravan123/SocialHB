package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 23/07/16.
 */
public class CSRInitCatData
{

    private int id;
    private int cat_id;
    private String cat_data_heading;
    private String cat_data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_data_heading() {
        return cat_data_heading;
    }

    public void setCat_data_heading(String cat_data_heading) {
        this.cat_data_heading = cat_data_heading;
    }

    public String getCat_data() {
        return cat_data;
    }

    public void setCat_data(String cat_data) {
        this.cat_data = cat_data;
    }
}
