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

public class ListaRutasActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {


    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_rutas = IP_ADDRES + "ruta/listrutas/"+Ajuste.token;


    List<Ruta> rutaList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rutas);
        rutaList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerViewRutas);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargaRutas();
        adapter = new RutaAdapter(rutaList,this);
        recyclerView.setAdapter(adapter);
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
        if (!response.isEmpty()){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("ruta");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objRuta = new Ruta();
                objRuta.setId_ruta(jsonObject1.getInt("id_ruta"));
                objRuta.setDesc_ruta(jsonObject1.getString("descripcion"));

                rutaList.add(objRuta);
            }
            adapter = new RutaAdapter(rutaList,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){

            e.printStackTrace();
        }
        }else{
            KToast.customColorToast(ListaRutasActivity.this, "SESION EXPIRADA", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_LONG, R.color.warning, R.drawable.ic_warning_black_24dp);
            Intent intent= new Intent(ListaRutasActivity.this,LoginActivity.class);
            ListaRutasActivity.this.startActivity(intent);
            this.finish();
        }

    }
}
