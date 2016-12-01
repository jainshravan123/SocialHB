package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 29/11/16.
 */
public class OfficeAddress {

    private int id;
    private String address_name;
    private String comp_name;
    private String building_name;
    private String postal_code;
    private String locality_name;
    private City city;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getLocality_name() {
        return locality_name;
    }

    public void setLocality_name(String locality_name) {
        this.locality_name = locality_name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
