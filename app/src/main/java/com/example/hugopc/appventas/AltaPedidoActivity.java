package com.example.hugopc.appventas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltaPedidoActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_prod = IP_ADDRES + "producto/listproductos/"+Ajuste.token;


    List<Producto> productoList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedido);

        productoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerViewAltaPedidos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargaProductos();
        adapter = new AltaPedidoAdpater(productoList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


    private void m_cargaProductos() {

        StringRequest request = new StringRequest(Request.Method.GET,URL_prod,this,this){
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
    public void onResponse(String response) {

        Producto objProducto;
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("producto");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objProducto = new Producto();
                objProducto.setNombre_producto(jsonObject1.getString("nombre_producto"));
                objProducto.setDescripcion(jsonObject1.getString("descripcion"));
                objProducto.setPrecio(jsonObject1.getDouble("precio"));
                objProducto.setNum_existencia(jsonObject1.getInt("num_existencia"));
                objProducto.setId_producto(jsonObject1.getInt("id_producto"));

                productoList.add(objProducto);
            }
            adapter = new AltaPedidoAdpater(productoList,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){

            e.printStackTrace();
        }

    }
}
