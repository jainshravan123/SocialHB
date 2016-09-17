package heartbeat.social.tcs.socialhb.utility;

/**
 * Created by admin on 22/07/16.
 */
public class CSRInitModuleSelector
{
    public String getModuleNameByModuleId(int id)
    {
        String module_name = "";

        switch(id)
        {
            case 1:
                module_name = "csr_init_env";
                break;
            case 2:
                module_name = "csr_init_education";
                break;
            case 3:
                module_name = "csr_init_health";
                break;
            case 4:
                module_name = "csr_init_affirmative_action";
                break;
        }
        return module_name;
    }
}
