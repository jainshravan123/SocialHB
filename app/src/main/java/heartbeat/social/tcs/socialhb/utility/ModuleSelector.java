package heartbeat.social.tcs.socialhb.utility;

/**
 * Created by admin on 22/07/16.
 */
public class ModuleSelector
{

    public String getModuleNameByModuleId(int id)
    {
        String module_name = "";

        switch(id)
        {
            case 1:
                module_name = "hbi_icon";
                break;
            case 2:
                module_name = "geo_icon";
                break;
            case 3:
                module_name = "csr_icon";
                break;
            case 4:
                module_name = "utility_icon";
                break;
        }
        return module_name;
    }

    public String getClassNameByModuleId(int id){
        String class_name = "";

        switch(id)
        {
            case 1:
                class_name = "HeartBeatIndexModule";
                break;
            case 2:
                class_name = "GEOModule";
                break;
            case 3:
                class_name = "CSRInitModule";
                break;
            case 4:
                class_name = "UtilityModule";
                break;
        }
        return class_name;
    }


}
