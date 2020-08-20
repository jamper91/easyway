package jamper91.com.easyway.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jamper91.com.easyway.R;


/**
 * Created by @jvillafane on 11/07/2016.
 */
public class CallWebServiceJson {
    private Activity activity;
    private String url;
    private HashMap<String, Object> campos;
    private int tipo;
    private HashMap<String, String> headers;
    private ResponseListener listener;
    private Administrador admin;
    private RequestQueue requestQueue;
    private ProgressDialog progress;

    AlertDialog.Builder builder=null;
    Dialog alertDialog=null;
    private String message="";
    private Drawable drawable=null;
    private Context context=null;

    public CallWebServiceJson(Activity activity, String url, HashMap<String, Object> campos, HashMap<String, String> header, int tipo, ResponseListener listener, Administrador admin) {
        this.activity = activity;
        this.url = url;
        this.campos = campos;
        this.headers = header;
        this.tipo = tipo;
        this.listener = listener;
        this.admin  = admin;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void init_request_queue(){
        if(this.context!=null)
            this.requestQueue= Volley.newRequestQueue(context);
        else
            this.requestQueue= Volley.newRequestQueue(activity);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void execute()
    {
        init_request_queue();
        if(!message.isEmpty()){
            progress = ProgressDialog.show(activity, null,
                    message, true);
        }else if(drawable!=null){
//            LayoutInflater inflater = activity.getLayoutInflater();
//            builder = new AlertDialog.Builder(activity);
//            builder.setView(inflater.inflate(R.layout.dialog, null));
//            builder.setCancelable(false);
            alertDialog=new Dialog(activity);
            alertDialog.setContentView(R.layout.dialog);
            alertDialog.setCancelable(false);
            ImageView imageView = (ImageView) alertDialog.findViewById(R.id.img1);
            if (imageView!=null) {
                imageView.setImageDrawable(drawable);
            }

            alertDialog.show();
        }
        JSONObject body = new JSONObject(campos);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                this.tipo,
                this.url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progress!=null) {
                            progress.dismiss();
                        } else if(alertDialog!=null){
                            alertDialog.dismiss();
                        }
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("campos: ", campos.toString());
                        if (progress!=null) {
                            progress.dismiss();
                        } else if(alertDialog!=null){
                            alertDialog.dismiss();
                        }
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                        }

                        listener.onErrorResponse(volleyError.getLocalizedMessage());




                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }
}
