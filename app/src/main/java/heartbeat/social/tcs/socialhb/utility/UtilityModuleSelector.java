package heartbeat.social.tcs.socialhb.utility;

/**
 * Created by admin on 26/07/16.
 */
public class UtilityModuleSelector
{
    public String getUtilityModuleClassNameByUtilityModuleId(int id)
    {
        String module_name = "";

        switch(id)
        {
            case 1:
                module_name = "WasteToWealth";
                break;
            case 2:
                module_name = "EnviroEngineer";
                break;
        }
        return module_name;
    }
}
