package jamper91.com.easyway.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import jamper91.com.easyway.R;


/**
 * Created by @jvillafane on 11/07/2016.
 */
public class CallWebServiceString {
    private Activity activity;
    private String url;
    private HashMap<String, String> campos;
    private HashMap<String, String> headers;
    private int tipo;
    private ResponseListener listener;
    private Administrador admin;
    private RequestQueue requestQueue;
    private ProgressDialog progress;

    AlertDialog.Builder builder=null;
    Dialog alertDialog=null;
    private String message="";
    private Drawable drawable=null;
    public CallWebServiceString(Activity activity, String url, HashMap<String, String> campos, HashMap<String, String> headers, int tipo, ResponseListener listener, Administrador admin) {
        this.activity = activity;
        this.url = url;
        this.campos = campos;
        this.headers = headers;
        this.tipo = tipo;
        this.listener = listener;
        this.admin  = admin;
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
        if(!message.isEmpty()){
            progress = ProgressDialog.show(activity, null,
                    message, true);
        }else if(drawable!=null){
            alertDialog=new Dialog(activity);
            alertDialog.setContentView(R.layout.dialog);
            alertDialog.setCancelable(false);
            ImageView imageView = (ImageView) alertDialog.findViewById(R.id.img1);
            if (imageView!=null) {
                imageView.setImageDrawable(drawable);
            }

            alertDialog.show();
        }

        StringRequest stringRequest = new StringRequest(
                this.tipo,
                this.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                    public void onErrorResponse(VolleyError error) {
                        Log.e("campos: ", campos.toString());
                        if (progress!=null) {
                            progress.dismiss();
                        } else if(alertDialog!=null){
                            alertDialog.dismiss();
                        }
                        listener.onErrorResponse(error.getMessage());


                    }
                }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        return campos;

                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return headers;
                    }

                };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
