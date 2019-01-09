package jamper91.com.easyway.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jamper91.com.easyway.R;


/**
 * Created by @jvillafane on 14/06/2016.
 */
public class Administrador {

    private static Context context=null;
    private static Administrador instancia = null;
    private static Activity actividad;
    private static Hashtable<String, Typeface> fuentes = null;

    //region Constructores
    private Administrador(Context c, Activity actividad) {

        context = c;
        this.actividad = actividad;
        init_fonts(c);


    }
    private Administrador(Context c) {
        context = c;
        actividad = null;
        init_fonts(c);

    }
    private void init_fonts(Context c)
    {
        fuentes = new Hashtable<String, Typeface>();
    }

    //endregion

    //region Instanciadores
    public static Administrador getInstance(Context c, Activity a, LinkedHashMap<String, Animacion> e) {
        if (instancia == null) {

            instancia = new Administrador(c, actividad);
        }
        actividad = a;
        return instancia;
    }
    public static Administrador getInstance(Context c) {
        if (instancia == null) {

            instancia = new Administrador(c);
        }
        return instancia;
    }
    public static Administrador getInstance_sinelementos(Context c) {
        if (instancia == null) {

            instancia = new Administrador(c);
        }
        return instancia;
    }
    //endregion

    //region Funciones utiles

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean r = netInfo != null && netInfo.isConnectedOrConnecting();
        if(!r)
        {
            toast_error(0);
        }

        return r;

    }
    public void toast(int mensaje)
    {
        String s = this.context.getString(mensaje);
        Toast.makeText(this.context, s, Toast.LENGTH_SHORT).show();
    }
    public void toast(String mensaje)
    {
        Toast.makeText(this.context, mensaje, Toast.LENGTH_SHORT).show();
    }
    public void toast_error(int posicion)
    {
        if(posicion>=0)
        {
            String[] errores = this.context.getResources().getStringArray(R.array.errores);
            String m = errores[posicion];
            Toast.makeText(this.context, m, Toast.LENGTH_SHORT).show();
        }

    }

    public boolean validar_email(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public int getResourceDrawableId(String name)
    {
        int id = -1;
        try {
            id = context.getResources().getIdentifier(name, "drawable", context.getApplicationContext().getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public int getResourceId(String name)
    {
        int id = -1;
        try {
            id = context.getResources().getIdentifier(name, "id", context.getApplicationContext().getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public Drawable getDrawable(int id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }
    public void login(HashMap<String, String> datos, Class destino)
    {
        for(Map.Entry<String, String> entry : datos.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            escribir_preferencia(key,val);
        }

        Intent i = new Intent(this.context, destino);
        this.context.startActivity(i);

    }
    public void log_out(Class aClass)
    {
        SharedPreferences pref = obtener_preferencias();
        pref.edit().clear().commit();
        callIntent(aClass, null);

    }


    public String getCurrentDateAndTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static Typeface getTypeFace (String key)
    {
        return  fuentes.get(key);
    }

    public static void callIntent(Class destino, HashMap<String,String> parametros)
    {
        Intent i = new Intent(context, destino);
        if(parametros!=null)
        {
            i.putExtra(Constants.parameters, parametros);
        }

        context.startActivity(i);
    }
    public static void callIntent(Class destino, Object parametro, Class class_b)
    {
        Intent i = new Intent(context, destino);
        Gson gson = new Gson();
        i.putExtra(Constants.parameters, gson.toJson(parametro, class_b));
        context.startActivity(i);
    }





    private static String encrypt(String input) {
        // Simple encryption, not very strong!
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    private static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public String convertISO885915(String unreadable)
    {
//        String unreadable = "Ã¤Ã¶Ã¼ÃÃÃÃÃ¡Ã©Ã­Ã³ÃºÃÃÃÃÃÃ Ã¨Ã¬Ã²Ã¹ÃÃÃÃÃÃ±Ã";

        if(unreadable!=null)
        {
            try {
                String readable = new String(unreadable.getBytes("ISO-8859-15"), "UTF-8");
                return readable;
            } catch (UnsupportedEncodingException e) {
                return unreadable;
            }
        }else{
            return unreadable;
        }

    }

    public void exitApp(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.actividad.startActivity(a);
    }
    //endregion

    //region Preferences

    public SharedPreferences obtener_preferencias()
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void escribir_preferencia(String key, String value)
    {
        Log.i(Constants.tag, value);
        SharedPreferences pre= obtener_preferencias();
        SharedPreferences.Editor editor = pre.edit();
//        editor.putString(encrypt(key), encrypt(value));
        editor.putString(key, value);
        editor.commit();
    }

    public String obtener_preferencia(String key)
    {
        SharedPreferences pre= obtener_preferencias();
//        return pre.getString(decrypt(key), "");
        return pre.getString(key, "");
    }

    //endregion

}
