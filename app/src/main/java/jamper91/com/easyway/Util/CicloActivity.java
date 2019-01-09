package jamper91.com.easyway.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mukesh.permissions.AppPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by @jvillafane on 05/10/2016.
 */
public abstract class CicloActivity extends AppCompatActivity {
    public Administrador admin;
    private LinkedHashMap<String, Animacion> elementos;
    public int layout;
    public HashMap<String, String> parametros=null;
    String [] permisos=null;

    //region Ciclo de vida
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.runtimePermission = new AppPermissions(this);
        elementos = new LinkedHashMap<>();
    }

    public void init(Context context, Activity activity, int layout)
    {
        this.admin = Administrador.getInstance(context, activity, elementos);
        this.layout = layout;
        setContentView(layout);
        Intent i=getIntent();
        getParameters(i.getExtras());
        initGui();
        animar_in(0);
        //Calculo el tiempo que se demorara en animar
        final int t = elementos.size()*300;
        Thread thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(t);
                    }
                }
                catch(InterruptedException ex){
                }
            }
        };
        thread.start();
        getData();
        initOnClick();
    }


    public void getParameters(Bundle b){
        try {
            parametros = (HashMap<String, String>) b.getSerializable(Constants.parameters);
        } catch (Exception e) {
            parametros=null;
        }
    }
    public abstract void initGui();
    public abstract void getData();
    public abstract void initOnClick();
    @Override
    protected void onStart() {
        super.onStart();
        getPermissions();
    }
    //endregion


    public void add_on_click(int elemento, View.OnClickListener clickListener)
    {
        try {
            elementos.get(elemento+"").getElemento().setOnClickListener(clickListener);
        } catch (Exception e) {
            admin.toast("No se puede generar evento onClick para el elemento"+elemento);
        }
    }
    public void addElemento(Animacion animacion)
    {
        elementos.put(animacion.getElemento().getId()+"", animacion);
    }
    //region Animacion
    public Animacion getElemento(int key)
    {
        return elementos.get(key+"");
    }
    public Animacion getAnimacion(int key)
    {
        return (Animacion) elementos.get(key+"");
    }
    public void animar_in(int pos) {
        try {
            Animacion a = (new ArrayList<Animacion>(this.elementos.values())).get(pos);
            a.animar();
            pos++;
            if (pos < elementos.values().size()) {
                final int posi = pos;
                Animacion b = (new ArrayList<Animacion>(this.elementos.values())).get(posi);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        animar_in(posi);
                    }
                }, b.getDuration());
            }
        } catch (Exception e) {
            pos=0;
        } finally {
            pos=0;
        }
    }
    //endregion

    //region Permisos
    public void setPermissions(String [] permissions){
        this.permisos = permissions;
    }

    private AppPermissions runtimePermission;
    private void getPermissions()
    {
        if(permisos!=null)
        {
            if(runtimePermission.hasPermission(permisos))
            {
                hasAllPermissions();
            }else{
                runtimePermission.requestPermission(permisos, mRequestCode);
            }
        }
    }
    public abstract void hasAllPermissions();
    private int mRequestCode=154;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestCode) { //The request code you passed along with the request.
            //grantResults holds a list of all the results for the permissions requested.
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    //Cierro la app
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }
            }
            getPermissions();
        }
    }
    //endregion

}
