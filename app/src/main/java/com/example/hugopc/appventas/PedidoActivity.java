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

public class PedidoActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener,Response.Listener<String>, Response.ErrorListener{


    @BindView(R.id.cardAltaPedidos)
    CardView cardAltaPedidos;
    @BindView(R.id.cardListaPedidos)
    CardView cardListaPedidos;
    @BindView(R.id.cardListaPedidosPorCliente)
    CardView cardListaPedidosporCliente;

    private Spinner spinner,spinner2;
    private ArrayList<String> clientes;
    private JSONArray result;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();
    private static String URL_clientes = IP_ADDRES + "cliente/listclientes/"+Ajuste.token;

    private RequestQueue requestQueue;
    int id_cliente=0;
    int id_cliente2=0;

    String texto2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        clientes = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spinnerClientesMostrarPed);
        spinner2 = findViewById(R.id.spinnerClientesLevPed);
        spinner.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        m_cargaClientes();
    }

    private void m_cargaClientes() {

        StringRequest request = new StringRequest(Request.Method.GET,URL_clientes,this,this){
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


    @OnClick({R.id.cardAltaPedidos})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(PedidoActivity.this,AltaPedidoActivity.class);
        intent.putExtra("id_cliente",id_cliente+"");
        PedidoActivity.this.startActivity(intent);
    }

    @OnClick({R.id.cardListaPedidos})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(PedidoActivity.this,ListaPedidosActivity.class);
        PedidoActivity.this.startActivity(intent);
    }

    @OnClick({R.id.cardListaPedidosPorCliente})
    public void OnClick3(View v){
        if (id_cliente==0){
            KToast.customColorToast(PedidoActivity.this, "NO se proporciono ningun cliente", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
        }else{
            Intent intent= new Intent(PedidoActivity.this,ListaPedidosPorClienteActivity.class);
            intent.putExtra("texto",texto2);
            PedidoActivity.this.startActivity(intent);
        }

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        adapterView.getItemAtPosition(i);


        switch (adapterView.getId())
        {
            case R.id.spinnerClientesLevPed:

                String CADENA= spinner2.getSelectedItem().toString();
                m_obtenerIDCliente(CADENA);

                break;

            case R.id.spinnerClientesMostrarPed:

                texto2= spinner.getSelectedItem().toString();
                m_obtenerIDCliente2(texto2);

                break;
        }



    }

    private void m_obtenerIDCliente(String p_cadena) {
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_cliente = Integer.valueOf(id_);

    }

    private void m_obtenerIDCliente2(String p_cadena) {
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_cliente2 = Integer.valueOf(id_);
        Ajuste.id_client =id_cliente2;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

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
        spinner2.setAdapter(new ArrayAdapter<String>(PedidoActivity.this, android.R.layout.simple_spinner_dropdown_item, clientes));

        spinner.setAdapter(new ArrayAdapter<String>(PedidoActivity.this, android.R.layout.simple_spinner_dropdown_item, clientes));
    }
}
