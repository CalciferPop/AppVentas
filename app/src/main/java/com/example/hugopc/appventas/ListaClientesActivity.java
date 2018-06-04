package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;

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
import java.util.List;
import java.util.Map;

public class ListaClientesActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_cli = IP_ADDRES + "cliente/listclientes/"+Ajuste.token;
    private static String URL_del_cli = IP_ADDRES + "cliente/deletecliente/";

    List<Cliente> clienteList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        clienteList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerViewClientes);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargaClientes();
        adapter = new ClienteAdapter(clienteList,this);
        recyclerView.setAdapter(adapter);
    }





    private void m_cargaClientes() {
        StringRequest request = new StringRequest(Request.Method.GET,URL_cli,this,this){
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

        Cliente objCliente;
        if (!response.isEmpty()){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("cliente");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objCliente = new Cliente();
                objCliente.setNombre_cliente(jsonObject1.getString("nombre_cliente"));
                objCliente.setApellido_paterno(jsonObject1.getString("apellido_paterno"));
                objCliente.setApellido_materno(jsonObject1.getString("apellido_materno"));
                objCliente.setTelefono(jsonObject1.getString("telefono"));
                objCliente.setEmail(jsonObject1.getString("email"));
                objCliente.setId_cliente(jsonObject1.getInt("id_cliente"));

                clienteList.add(objCliente);
            }
            adapter = new ClienteAdapter(clienteList,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){

            e.printStackTrace();
        }
        }else{
            KToast.customColorToast(ListaClientesActivity.this, "SESION EXPIRADA", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_LONG, R.color.warning, R.drawable.ic_warning_black_24dp);
            Intent intent= new Intent(ListaClientesActivity.this,LoginActivity.class);
            ListaClientesActivity.this.startActivity(intent);
            this.finish();
        }

    }


    }

