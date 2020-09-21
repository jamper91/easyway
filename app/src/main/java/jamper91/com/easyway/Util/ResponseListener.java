package jamper91.com.easyway.Util;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by @jvillafane on 11/07/2016.
 */
public interface ResponseListener {
    public void onResponse(String response);
    public void onResponse(JSONObject response);
    public void onResponse(JSONArray response);
    public void onErrorResponse(String error, VolleyError volleyError);
}
