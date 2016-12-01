package heartbeat.social.tcs.socialhb.bean;

/**
 * Created by admin on 18/07/16.
 */
public class Web_API_Config
{

    //Local Server
    //public static String root_domain_url              =           "http://192.168.56.1:8080/SocialHearbeatWebServices/api/";

    //Local Server with Maven
    public static String root_domain_url                =           "http://192.168.56.1:8080/socialheartbeat/api/";

    //Hosting Server
    //public static String root_domain_url                =           "https://socialheartbeat-bloggie.rhcloud.com/api/";
    public static String root_image_url                 =           "https://socialheartbeat-bloggie.rhcloud.com/api/";

    public static String user_login_api                 =           root_domain_url + "users/login";
    public static String user_basic_info                =           root_domain_url + "users/";
    public static String user_profile_api               =           root_domain_url + "users/profile/";
    public static String enabled_modules                =           root_domain_url + "modules";
    public static String csr_init_enabled_modules       =           root_domain_url + "csrInitCategories";
    public static String csr_init_single_module         =           root_domain_url + "csrInitCategories/";
    public static String csr_init_category_data         =           root_domain_url + "csrInitCategories/cat_data/";

    public static String utility_enabled_module         =           root_domain_url + "utility/enabledCategory";

    public static String user_profile_update_api        =           root_domain_url + "users/profile/updateProfile";
    public static String user_profile_pic_update_api    =           root_domain_url + "";

    //API for donating the url
    public static String donate_item                    =           root_domain_url + "donate/donateItem";
    public static String donated_item_status            =           root_domain_url + "donate/itemStatus/";


    //API for firebase push notifications
    public static String without_signin_firebase_push_notification_API  =   root_domain_url + "notify/regiterTokenBeforeSignIn";
    public static String firebase_update_user_id_for_registered_token_API    = root_domain_url + "notify/updateUserIDAfterSignIn";
    public static String firebase_push_notification_API                 =   root_domain_url + "notify/regiterToken";


    //API for Quiz Part
    public static String root_domain_for_quiz                       = root_domain_url + "quiz/";
    public static String get_all_quiz_qus                           = root_domain_for_quiz + "all_questions";
    public static String get_limited_qus                            = root_domain_for_quiz + "limited_questions/";                            //Add Number of Questions how many you want to fetch
    public static String get_all_quiz_ans                           = root_domain_for_quiz + "all_answers";
    public static String get_specific_ans                           = root_domain_for_quiz + "answer/";                                 //Add Question ID for fetching the data from this api
    public static String get_all_qus_with_answers                   = root_domain_for_quiz + "questionsWithAnswers";
    public static String get_single_qus_with_answers                = root_domain_for_quiz + "questionsWithAnswers/";                    // Add Question ID for fetching the data from this api
    public static String get_all_qus_with_ans_without_right_ans     = root_domain_for_quiz + "questionsWithAnswersWithoutRightAns";
    public static String get_limited_qus_with_ans_without_right_ans = root_domain_for_quiz + "limitedQuestionsWithAnswersWithoutRightAns/";
    public static String get_single_qus_with_ans_without_right_ans  = root_domain_for_quiz + "questionsWithAnswersWithoutRightAns/";     // Add Question ID for fetching the data from this api
    public static String calculating_quiz_score                     = root_domain_for_quiz + "calculateScore";
    public static String posting_starting_quiz_data                 = root_domain_for_quiz + "postStartingQuizData";
    public static String posting_final_quiz_score                   = root_domain_for_quiz + "postQuizScoreData";
    public static String get_quiz_prev_score_details                = root_domain_for_quiz + "getQuizScore";


    //API for Geolocation Module
    public static String officeAddressAPI                           = root_domain_url + "office_address/";
    public static String areaOfInterestAPI                          = root_domain_url + "users/area_of_interest";


    //API for Facts
    public static String allFactsAPI                                = root_domain_url + "facts";
}
