package com.example.adrianmontes.peticionhttpexternarss;

import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //vamos a crear una conexion con un rss, de manera que tendriamos que hacerlo con hilos
    String urlConexion="http://ep00.epimg.net/rss/elpais/portada.xml";
    String salida;
    TextView noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noticias =(TextView) findViewById(R.id.textView);

    }

    public void abrirNoticias(View view) {
        Conexion objetoConecxion=new Conexion();
        objetoConecxion.execute();
    }


    public class Conexion extends AsyncTask<Void,Integer,String> {


        @Override
        protected String doInBackground(Void... params) {

            String Salida;
            int i = 0, j = 0;
            try {

                //ahora creamos un objeto de tipo url y le pasamos la url que creamos
                URL url = new URL(urlConexion);
                //Abrimos la conexion
                HttpURLConnection Connection = (HttpURLConnection) url.openConnection();
                //ahora nos identificamos, da igual lo que pongamos pero siempre hay que decirle al servidor
                //quien somos, se denomina cabecera http.
                Connection.setRequestProperty("User-Agent", "Mozilla/5.0(Linux;Android 1.5; es-ES) Ejemplo Http");
                int respuesta = Connection.getResponseCode();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    //ahora cojo toda la informacion, lo paso a bufferedReader para ponerlo por lineas.
                    BufferedReader lector = new BufferedReader(new InputStreamReader(Connection.getInputStream()));
                    //ahora lo recorro
                    String linea = lector.readLine();
                    while (linea != null) {
                        //ahora cojemos la etiqueta del xml que nos interesa, para que con el indexOf lo busque
                        //y le indico que empieze a coger el texto desde el final de ella.
                        if (linea.indexOf("<title><![CDATA[") >= 0) {
                            //ahora le digo que desde que encuentra la cadena titulo +16 para que empieze a buscar juesto despues de la linea que encontro
                            //ya que tiene 16 digitos
                            i = linea.indexOf("<title>") + 16;
                            j = linea.indexOf("</title>") - 3; //-3 para que me quite los careceres del final
                            salida += linea.substring(i, j);
                            salida += "\n----------\n";
                        }
                        linea = lector.readLine();
                    }
                    lector.close();
                } else {

                    salida = "Pagina no encontrada";


                }

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return salida;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            noticias.setText(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
