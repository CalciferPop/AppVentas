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
import android.widget.EditText;
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

public class UpdateRutasClientesActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener,Spinner.OnItemSelectedListener{
    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_rutas = IP_ADDRES + "ruta/listrutas/"+Ajuste.token;
    private static String URL_clientes = IP_ADDRES + "cliente/listclientes/"+Ajuste.token;

    private Spinner spinnerRutas,spinnerClientes;
    private ArrayList<String> rutas;
    private ArrayList<String> clientes;
    private JSONArray result;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_ruta=0;
    int id_cliente=0;
    int id_rutaNueva=0;
    int id_clienteNuevo=0;

    @BindView(R.id.edtIdRutaAnterior)
    EditText edtIDRutaAnterior;
    @BindView(R.id.edtIdClienteAnterior)
    EditText edtIDClienteAnterior;

    @BindView(R.id.btnUPDRutaClientes)
    Button btnActuaRutaCliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rutas_clientes);
        ButterKnife.bind(this);
        Intent intent1 = getIntent();
        final String id_rutaP = intent1.getStringExtra("id_ruta");
        final String descripcion = intent1.getStringExtra("descripcion");
        final String id_clienteP = intent1.getStringExtra("id_cliente");
        final String nombre_cliente = intent1.getStringExtra("nombre_cliente");
        edtIDRutaAnterior.setText(id_rutaP + ":" + descripcion);
        edtIDClienteAnterior.setText(id_clienteP+":" + nombre_cliente);
        id_ruta = Integer.valueOf(id_rutaP);
        id_cliente = Integer.valueOf(id_clienteP);
        requestQueue = Volley.newRequestQueue(this);
        rutas = new ArrayList<String>();
        clientes = new ArrayList<String>();
        spinnerRutas = (Spinner) findViewById(R.id.spinnerRutas3);
        spinnerClientes = (Spinner) findViewById(R.id.spinnerClientes2);
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

                spinnerClientes.setAdapter(new ArrayAdapter<String>(UpdateRutasClientesActivity.this, android.R.layout.simple_spinner_dropdown_item, clientes));



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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.getItemAtPosition(i);


        switch (adapterView.getId())
        {
            case R.id.spinnerRutas3:

                String texto= spinnerRutas.getSelectedItem().toString();
                m_obtenerIDRuta(texto);

                break;

            case R.id.spinnerClientes2:

                String texto2= spinnerClientes.getSelectedItem().toString();
                m_obtenerIDCliente(texto2);
                break;
        }
    }


    public void m_obtenerIDRuta(String p_cadena){
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_rutaNueva = Integer.valueOf(id_);
        System.out.println("ID RUta anterior:" +id_ruta);
        System.out.println("ID RUta Nueva:" +id_rutaNueva);
    }

    public void m_obtenerIDCliente(String p_cadena){
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_clienteNuevo = Integer.valueOf(id_);
        System.out.println("ID Cliente anterior:" +id_cliente);
        System.out.println("ID Cliente Nueva:" +id_clienteNuevo);
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

        spinnerRutas.setAdapter(new ArrayAdapter<String>(UpdateRutasClientesActivity.this, android.R.layout.simple_spinner_dropdown_item, rutas));
    }


    @OnClick({R.id.btnUPDRutaClientes})
    public void OnClick4(View v){
        m_actualiza(id_ruta,id_rutaNueva,id_cliente,id_clienteNuevo);
        KToast.customColorToast(UpdateRutasClientesActivity.this, "Cambios Realizados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(this,ListaRutasClientesActivity.class);
        this.startActivity(intent);
        finish();

    }

    private void m_actualiza(int p_idRutaAnt, int p_idRutaNueva,int p_idClienteAnt,int p_idClienteNuevo) {

        String URL_UPD_RC = IP_ADDRES + "rutas_clientes/updaterutas_clientes/"+Ajuste.token;

        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_ruta",p_idRutaAnt);
            jsonObject.put("id_rutaN",p_idRutaNueva);
            jsonObject.put("id_cliente",p_idClienteAnt);
            jsonObject.put("id_clienteN",p_idClienteNuevo);


        }catch (Exception e){
            e.getMessage();
            System.out.println("ERROR" + e.getMessage());
        }

        stringRequest = new StringRequest(Request.Method.PUT, URL_UPD_RC, new Response.Listener<String>() {
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



    }

}
