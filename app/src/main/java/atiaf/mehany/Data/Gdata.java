package atiaf.mehany.Data;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by سيد on 12/03/2017.
 */
public class Gdata {
    public static String TarekId ;

    public static String country_id ;
    public static String user_id ="";
    public static String company_logo ="";
    public static String company_name ;
    public static String company_des ;
    public static String user_img ="";
    public static String user_fname ;
    public static String user_lname ;
    public static String user_username ;
    public static String user_pass ;
    public static String user_email ;
    public static String user_cid ;
    public static String user_pack ;
    public static String user_packid ;
    public static String user_phone ;
    public static String worker_id ="";
    public static String worker_img ="";
    public static String worker_fname ;
    public static String worker_lname ;
    public static String worker_username ;
    public static String worker_pass ;
    public static String worker_email ;
    public static String worker_opernum ;
    public static String worker_cid ;
    public static String worker_pack ;
    public static String worker_job_id ;
    public static String worker_jobs = "";
    public static String worker_phone ;
    public static String delegatepackid ;
    public static String delegate_phone ;
    public static String delegate_image ;
    public static ArrayList<Workerimg> array = new ArrayList<>();
    public static String provider_rate ;
    public static boolean worker_status ;
    public static String teacher_id ="";
    public static String teacher_img ="";
    public static String teacher_fname ;
    public static String teacher_lname ;
    public static String teacher_username ;
    public static String teacher_pass ;
    public static String teacher_email ;
    public static String teacher_opernum ;
    public static String teacher_jobs = "";
    public static String teacher_stages = "";
    public static String teacher_subjects = "";
    public static String teacher_phone ;
    public static String teacher_cid ;
    public static ArrayList<Workerimg> teacherarray = new ArrayList<>();
    public static String teacher_rate ;
    public static boolean teacher_status ;

    public static String provider_maham;
    public static String provider_monhasa ;
    public static String provider_charge ;
    public static String provider_not ;
    public static String provider_cityid ;
    public static String provider_sid ;
    public static String provider_service ;
    public static String provider_country ;
    public static String provider_spec  ="";
    public static int pager  =1;
    public static String lat  ="";
    public static String lon  ="";
   // public static String url = "http://mhny.co/API/Clients/" ;     // old  url  http://192.232.214.91/~mahniapp/
   // public static String url_worker = "http://mhny.co/API/Workers/" ;
   // public static String url_teacher = "http://mhny.co/API/Teachers/" ;

    public static String url = "https://mhny.mtgofa.com/API/Clients/" ;     // old  url  http://192.232.214.91/~mahniapp/
    public static String url_worker = "https://mhny.mtgofa.com/API/Workers/" ;
    public static String url_teacher = "https://mhny.mtgofa.com/API/Teachers/" ;

    public static String refreshedToken  = "";
    public static boolean emailValidator(String mail) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }
    public static String getAddress1(Double lat, Double lon) {

        String Address1 = "";
        String Address2 = "";
        String City = "";
        String State = "";
        String Country = "";
        String County = "";
        String PIN = "";

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                    + lon + "&sensor=true&language=" + Locale.getDefault().getLanguage());
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            // Address2 = Address2 + long_name + ", ";
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }
                    }

                    // JSONArray mtypes = zero2.getJSONArray("types");
                    // String Type = mtypes.getString(0);
                    // Log.e(Type,long_name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Address1 + " " + Address2 + " " + City + " " + County + " " + State + " " + Country;
    }

    public static JSONObject getJSONfromURL(String url) {

        // initialize
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;

        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return jObject;
    }

    public static String getAppLang(){
        return  Locale.getDefault().getLanguage();
    }
}
