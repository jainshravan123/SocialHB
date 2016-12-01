package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 30/11/16.
 */
public class Fact {

    private int id;
    private String fact;
    private String short_desc;
    private String long_desc;
    private HostedUrl  hostedUrl;
    private HostedImage hostedImage;
    private City city;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public HostedUrl getHostedUrl() {
        return hostedUrl;
    }

    public void setHostedUrl(HostedUrl hostedUrl) {
        this.hostedUrl = hostedUrl;
    }

    public HostedImage getHostedImage() {
        return hostedImage;
    }

    public void setHostedImage(HostedImage hostedImage) {
        this.hostedImage = hostedImage;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
