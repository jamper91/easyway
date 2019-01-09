package jamper91.com.easyway.Util;

import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Jorge on 03/04/2015.
 */
public class Animacion {

    private View elemento;
    private Techniques efecto_in=null;
    private Techniques efector_out=null;
    private boolean animar_principio=true;

    private int  duration = 0;


    public Animacion(View elemento, Techniques efecto) {
        this.elemento = elemento;
        this.efecto_in = efecto;
        this.elemento.setVisibility(View.INVISIBLE);
        obtener_efecto_out();
    }


    public Animacion(View elemento, Techniques efecto, Typeface fuente)
    {
        this.elemento=elemento;
        if(elemento instanceof TextView)
        {
            TextView aux= (TextView)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof Button)
        {
            Button aux= (Button)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof RadioButton)
        {
            RadioButton aux= (RadioButton)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof EditText)
        {
            EditText aux= (EditText)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof AutoCompleteTextView)
        {
            AutoCompleteTextView aux= (AutoCompleteTextView)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }
        this.elemento.setVisibility(View.INVISIBLE);
        this.efecto_in = efecto;
        obtener_efecto_out();
    }

    public Animacion(View elemento, Techniques efecto, Typeface fuente, boolean animar_principio)
    {

        this.elemento=elemento;
        if(elemento instanceof TextView)
        {
            TextView aux= (TextView)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof Button)
        {
            Button aux= (Button)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof RadioButton)
        {
            RadioButton aux= (RadioButton)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof EditText)
        {
            EditText aux= (EditText)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }else if(elemento instanceof AutoCompleteTextView)
        {
            AutoCompleteTextView aux= (AutoCompleteTextView)this.elemento;
            aux.setTypeface(fuente);
            this.elemento=aux;
        }
        this.elemento.setVisibility(View.GONE);
        this.efecto_in = efecto;
        this.animar_principio = animar_principio;
        obtener_efecto_out();
    }

    public int getDuration()
    {
        if(efecto_in!=null && animar_principio)
        {
            return 50;
        }else if(efecto_in==null && animar_principio){
            return 0;
        }else{
            return 0;
        }
    }


    public void animar()
    {
        if(efecto_in!=null && animar_principio)
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    elemento.setVisibility(View.VISIBLE);
                    YoYo.with(efecto_in)
                            .duration(300)
                            .playOn(elemento);
                }
            }, 1 * 300);
        }else if(efecto_in==null && animar_principio){
            this.elemento.setVisibility(View.VISIBLE);
        }


    }
    public void animar(final int duration)
    {
        if(efecto_in!=null && animar_principio)
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    elemento.setVisibility(View.VISIBLE);
                    YoYo.with(efecto_in)
                            .duration(duration)
                            .playOn(elemento);
                }
            }, 1 * 300);
        }else if(efecto_in==null && animar_principio){
            this.elemento.setVisibility(View.VISIBLE);
        }


    }

    public void forzar_animacion()
    {
        if(efecto_in!=null)
        {
            elemento.setVisibility(View.VISIBLE);
            YoYo.with(efecto_in)
                    .duration(300)
                    .playOn(elemento);

        }
    }
    public void animar_out()
    {
        if(efector_out!=null)
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    YoYo.with(efector_out)
                            .duration(100)
                            .playOn(elemento);
                }
            }, 1 * 100);
        }

    }

    public View getElemento() {
        return elemento;
    }

    public void setElemento(View elemento) {
        this.elemento = elemento;
    }

    public Techniques getEfecto_in() {
        return efecto_in;
    }

    public void setEfecto_in(Techniques efecto_in) {
        this.efecto_in = efecto_in;
    }

    public void obtener_efecto_out()
    {
        if(efecto_in!=null)
        {
            switch (efecto_in)
            {
                case SlideInDown:
                    efector_out= Techniques.SlideOutDown;
                    break;
                case SlideInUp:
                    efector_out= Techniques.SlideOutUp;
                    break;
                case SlideInLeft:
                    efector_out= Techniques.SlideOutLeft;
                    break;
                case SlideInRight:
                    efector_out= Techniques.SlideOutRight;
                    break;

                case FadeIn:
                    efector_out = Techniques.FadeOut;
                    break;


            }
        }else{
            efector_out=null;
        }

    }

    public String getText()
    {
        if(elemento instanceof TextView)
        {
            return ((TextView) elemento).getText().toString();
        }else if(elemento instanceof EditText)
        {
            return ((EditText) elemento).getText().toString();
        }

        return "";
    }
    public void setText(String text)
    {
        if(elemento instanceof TextView)
        {
            ((TextView) elemento).setText(text);
        }else if(elemento instanceof EditText)
        {
            ((EditText) elemento).setText(text);
        }
    }

    public int getItemSelected()
    {
        if(elemento instanceof Spinner)
        {
            return ((Spinner) elemento).getSelectedItemPosition();
        }
        return -1;
    }
}