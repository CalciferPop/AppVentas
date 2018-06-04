package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AltaRutaClienteActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener,Spinner.OnItemSelectedListener {


    private Spinner spinnerRutas;
    private Spinner spinnerClientes;

    private ArrayList<String> rutas;
    private ArrayList<String> clientes;

    private JSONArray result;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();
    private static String URL_rutas = IP_ADDRES + "ruta/listrutas/"+Ajuste.token;
    private static String URL_clientes = IP_ADDRES + "cliente/listclientes/"+Ajuste.token;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_ruta=0;
    int id_cliente=0;

    @BindView(R.id.btnAltaRutaCliente)
    Button btnAltaRutaCliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_ruta_cliente);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        rutas = new ArrayList<String>();
        clientes = new ArrayList<String>();
        spinnerRutas = (Spinner) findViewById(R.id.spinnerRutas2);
        spinnerClientes = (Spinner) findViewById(R.id.spinnerClientes);
        spinnerClientes.setOnItemSelectedListener(this);
        spinnerRutas.setOnItemSelectedListener(this);
        m_cargaRutas();
        m_cargaClientes();
    }

    private void m_cargaClientes() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_clientes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Cliente objCliente;
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("cliente");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        objCliente = new Cliente();
                        objCliente.setId_cliente(jsonObject1.getInt("id_cliente"));
                        objCliente.setNombre_cliente(jsonObject1.getString("nombre_cliente"));

                        String cliente = jsonObject1.getInt("id_cliente")+":   "+  jsonObject1.getString("nombre_cliente");
                        clientes.add(cliente);
                    }


                }catch (JSONException e){

                    e.printStackTrace();
                }

                spinnerClientes.setAdapter(new ArrayAdapter<String>(AltaRutaClienteActivity.this, android.R.layout.simple_spinner_dropdown_item, clientes));



            }
        }, this){
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

        spinnerRutas.setAdapter(new ArrayAdapter<String>(AltaRutaClienteActivity.this, android.R.layout.simple_spinner_dropdown_item, rutas));


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.getItemAtPosition(i);


        switch (adapterView.getId())
        {
            case R.id.spinnerRutas2:

                String texto= spinnerRutas.getSelectedItem().toString();
                m_obtenerIDRuta(texto);

                break;

            case R.id.spinnerClientes:

                String texto2= spinnerClientes.getSelectedItem().toString();
                m_obtenerIDCliente(texto2);
                break;
        }
    }

    public void m_obtenerIDRuta(String p_cadena){
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_ruta = Integer.valueOf(id_);
        System.out.println("ID RUta:" +id_ruta);
    }

    public void m_obtenerIDCliente(String p_cadena){
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_cliente = Integer.valueOf(id_);
        System.out.println("ID Cliente:" +id_cliente);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick({R.id.btnAltaRutaCliente})
    public void OnClick3(View v){
       m_altaRutaCliente(id_ruta,id_cliente);
    }

    private void m_altaRutaCliente(int id_ruta, int id_cliente) {

        String URL = IP_ADDRES+"rutas_clientes/insertrutas_clientes/"+Ajuste.token;

        jsonObject = new JSONObject();

        try{

            jsonObject.put("id_ruta",id_ruta);
            jsonObject.put("id_cliente",id_cliente);


        }catch (Exception e){
            e.getMessage();
        }
        System.out.println(jsonObject);
        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError
            {
                try
                {
                    return jsonObject.toString().getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee)
                {
                    Log.e("Error", uee.toString());
                    return null;
                }
            }
            @Override
            public Map<String, String> getHeaders ()
            {
                Map<String, String> params = new HashMap<>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        requestQueue.add(stringRequest);
        KToast.customColorToast(AltaRutaClienteActivity.this, "Registro Ruta - Cliente Insertado", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_warning_black_24dp);
        Intent intent= new Intent(this,RutaClienteActivity.class);
        this.startActivity(intent);
        finish();
    }


}
