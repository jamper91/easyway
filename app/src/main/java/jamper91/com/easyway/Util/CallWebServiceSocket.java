package jamper91.com.easyway.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import jamper91.com.easyway.R;


/**
 * Created by @jvillafane on 11/07/2016.
 */
public class CallWebServiceSocket extends AsyncTask<Void, Void, String> {

    private ResponseListener responseListener=null;
    private Administrador admin=null;
    private Location mLastLocation=null;
    private Activity activity=null;
    private ProgressDialog progress;
    private String mensajeTrama="";
    private String ip="";
    private int port=0;

    AlertDialog.Builder builder=null;
    Dialog alertDialog=null;
    private String message="";
    private Drawable drawable=null;

    public CallWebServiceSocket(ResponseListener responseListener, Administrador admin, Location mLastLocation,
                                Activity activity, String mensajetrama, String ip, int port) {
        this.responseListener = responseListener;
        this.admin = admin;
        this.mLastLocation = mLastLocation;
        this.activity = activity;
        this.mensajeTrama=mensajetrama;
        this.ip = ip;
        this.port = port;
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

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
    }

    @Override
    protected String doInBackground(Void... voids) {
        return testFullTcp();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (progress!=null) {
            progress.dismiss();
        } else if(alertDialog!=null){
            alertDialog.dismiss();
        }
        if(response!=null)
        {
            responseListener.onResponse(response);
        }else {
            responseListener.onErrorResponse(null, null);
        }


    }

    //region Comunicación Servidor
    private String testFullTcp() {
        try {
            /**
             * Creación del socket
             */
            Socket socket = new Socket(ip, port);
            String v = comunicationSocket(socket);
            Log.d(Constants.tag, v+"");
            return v;
        } catch (IOException ex) {
            Log.e(Constants.tag, ex.getMessage());
            return null;
        }
    }
    private String comunicationSocket(Socket socket) {

        String r="";
        long startTime = System.currentTimeMillis();
        try {

            /**
             * Envio informacióm
             */

            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            Log.d(Constants.tag, "Mensaje ENVIAR: "+mensajeTrama);
            dOut.write(mensajeTrama.getBytes());
            dOut.flush();
            /**
             * Lectura de información
             */
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int read;
            long wait_time = 30000;
            long end_time = startTime + wait_time;
            while (true) {
                if ((System.currentTimeMillis() > end_time)) {
                    r = "2";
                    break;
                }
                int available = is.available();
                if (available > 0) {
                    if ((read = is.read(buffer)) != -1) {
                        r = new String(buffer, 0, read);
                        break;
                    }
                }

            }
            dOut.close();
            return r;

        } catch (IOException ex) {
            Log.e(Constants.tag, ex.getMessage());
            return null;
        }

    }
    //endregion
}
