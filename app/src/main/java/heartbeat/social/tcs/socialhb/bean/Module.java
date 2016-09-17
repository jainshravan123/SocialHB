package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 18/07/16.
 */
public class Module
{
    private int id;
    private String module_name;
    private String module_icon;
    private int module_status;
    private int module_icon_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getModule_icon() {
        return module_icon;
    }

    public void setModule_icon(String module_icon) {
        this.module_icon = module_icon;
    }

    public int getModule_status() {
        return module_status;
    }

    public void setModule_status(int module_status) {
        this.module_status = module_status;
    }

    public int getModule_icon_id() {
        return module_icon_id;
    }

    public void setModule_icon_id(int module_icon_id) {
        this.module_icon_id = module_icon_id;
    }
}
