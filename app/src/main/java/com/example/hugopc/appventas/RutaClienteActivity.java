package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onurkaganaldemir.ktoastlib.KToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RutaClienteActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener,Response.Listener<String>, Response.ErrorListener {


    @BindView(R.id.cardAltaRutasClientes)
    CardView cardAltaRutasCLientes;
    @BindView(R.id.cardListaClientesRutas)
    CardView cardListaRutasClientes;
    @BindView(R.id.cardMapasRutas)
    CardView cardMapas;

    private Spinner spinner;
    private ArrayList<String> rutas;
    private JSONArray result;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();
    private static String URL_rutas = IP_ADDRES + "ruta/listrutas/"+Ajuste.token;

    private RequestQueue requestQueue;
    int id_ruta=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_cliente);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        rutas = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spinnerRutas);
        spinner.setOnItemSelectedListener(this);
        m_cargaRutas();


    }

    private void m_cargaRutas() {
        StringRequest request = new StringRequest(Request.Method.GET,URL_rutas,this,this){
            @Override
            public Map<String, String> getHeaders ()
            {
                Map<String, String> params = new HashMap<>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                return params;
            }
        };
        requestQueue.add(request);
    }


    @OnClick({R.id.cardAltaRutasClientes})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(RutaClienteActivity.this,AltaRutaClienteActivity.class);
        RutaClienteActivity.this.startActivity(intent);
    }



    @OnClick({R.id.cardListaClientesRutas})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(RutaClienteActivity.this,ListaRutasClientesActivity.class);
        RutaClienteActivity.this.startActivity(intent);
    }


    @OnClick({R.id.cardMapasRutas})
    public void OnClick3(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        if (id_ruta==0){
            KToast.customColorToast(RutaClienteActivity.this, "NO se proporciono ninguna ruta", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
        }else{
            Intent intent= new Intent(RutaClienteActivity.this,MapasRutasClientesActivity.class);
            intent.putExtra("id_ruta",id_ruta+"");
            RutaClienteActivity.this.startActivity(intent);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String texto= spinner.getSelectedItem().toString();
        m_obtenerIDRuta(texto);

    }

    public void m_obtenerIDRuta(String p_cadena){
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_ruta = Integer.valueOf(id_);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

        Ruta objRuta;
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("ruta");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objRuta = new Ruta();
                objRuta.setId_ruta(jsonObject1.getInt("id_ruta"));
                objRuta.setDesc_ruta(jsonObject1.getString("descripcion"));

                String ruta = jsonObject1.getInt("id_ruta")+":   "+  jsonObject1.getString("descripcion");
                rutas.add(ruta);
            }


        }catch (JSONException e){

            e.printStackTrace();
        }

        spinner.setAdapter(new ArrayAdapter<String>(RutaClienteActivity.this, android.R.layout.simple_spinner_dropdown_item, rutas));
    }
}
