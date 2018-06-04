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

public class ListaPedidosActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_pedido = IP_ADDRES + "pedido2/listpedidos2/"+Ajuste.token;


    List<Pedido> pedidoList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);


        pedidoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerViewPedidos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargapedidos();
        adapter = new PedidoAdapter(pedidoList,this);
        recyclerView.setAdapter(adapter);

    }



    private void m_cargapedidos() {

        StringRequest request = new StringRequest(Request.Method.GET,URL_pedido,this,this){
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

        Pedido objpedido;
        if (!response.isEmpty()){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("pedido2");
            //System.out.println(jsonObject.getJSONArray("pedido2"));
            System.out.println("TAMAÑO DEL ARRAY"+  jsonArray.length());
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objpedido = new Pedido();
                objpedido.setPrecio(jsonObject1.getDouble("precio"));
                objpedido.setCantidad(jsonObject1.getInt("cantidad"));
                objpedido.setDomicilio(jsonObject1.getString("domicilio"));
                objpedido.setFecha_compra(jsonObject1.getString("fecha_compra"));
                objpedido.setId_cliente(jsonObject1.getInt("id_cliente"));
                objpedido.setId_pedido(jsonObject1.getInt("id_pedido"));
                objpedido.setId_producto(jsonObject1.getInt("id_producto"));
                objpedido.setImporte(jsonObject1.getDouble("importe"));
                objpedido.setNombre_cliente(jsonObject1.getString("nombre_cliente"));
                objpedido.setNombre_producto(jsonObject1.getString("nombre_producto"));
                System.out.println(jsonObject1.getDouble("precio"));
                System.out.println(jsonObject1);
                pedidoList.add(objpedido);
            }
            adapter = new PedidoAdapter(pedidoList,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){

            e.printStackTrace();
        }
        }else{
            KToast.customColorToast(ListaPedidosActivity.this, "SESION EXPIRADA", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_LONG, R.color.warning, R.drawable.ic_warning_black_24dp);
            Intent intent= new Intent(ListaPedidosActivity.this,LoginActivity.class);
            ListaPedidosActivity.this.startActivity(intent);
            this.finish();
        }

    }
}
