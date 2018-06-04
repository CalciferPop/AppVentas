package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.widget.TextView;

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

public class ListaPedidosPorClienteActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {



    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    //private static String URL_pedido = IP_ADDRES + "pedido2/listpedidos2/";

    List<Pedido> pedidoList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    TextView txtCliente;

    private RequestQueue requestQueue;

    int id_cliente=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos_por_cliente);
        txtCliente = findViewById(R.id.txtIDClientePedido);

        pedidoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        Intent intent1 = getIntent();
        final String texto = intent1.getStringExtra("texto");

        txtCliente.setText(texto);
        recyclerView = findViewById(R.id.recyclerViewPedidosPorCliente);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargapedidos(Ajuste.id_client);
        adapter = new PedidoAdapter(pedidoList,this);
        recyclerView.setAdapter(adapter);

    }

    private void m_cargapedidos(int p_idCliente) {

         String URL_pedido = IP_ADDRES + "pedido2/listpedidos2/"+p_idCliente+"/"+Ajuste.token;

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
            System.out.println("OBJETO :" + jsonObject);

            System.out.println("TAMAÑO" +  response.length());

            if(response.length()<420){
                JSONObject jsonObject1 = jsonObject.getJSONObject("pedido2");

                objpedido = new Pedido();
                objpedido.setPrecio( jsonObject1.getDouble("precio"));
                objpedido.setCantidad( jsonObject1.getInt("cantidad"));
                objpedido.setDomicilio(jsonObject1.getString("domicilio"));
                objpedido.setFecha_compra( jsonObject1.getString("fecha_compra"));
                objpedido.setId_cliente( jsonObject1.getInt("id_cliente"));
                objpedido.setId_pedido( jsonObject1.getInt("id_pedido"));
                objpedido.setId_producto( jsonObject1.getInt("id_producto"));
                objpedido.setImporte( jsonObject1.getDouble("importe"));
                objpedido.setNombre_cliente(jsonObject1.getString("nombre_cliente"));
                objpedido.setNombre_producto(jsonObject1.getString("nombre_producto"));
                    System.out.println(jsonObject1.get("precio"));
                System.out.println(jsonObject1);
                pedidoList.add(objpedido);
                adapter = new PedidoAdapter(pedidoList,this);
                recyclerView.setAdapter(adapter);
            }else{
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
            }
        }catch (JSONException e){

            e.printStackTrace();
        }
        }else{
            KToast.customColorToast(ListaPedidosPorClienteActivity.this, "SESION EXPIRADA", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_LONG, R.color.warning, R.drawable.ic_warning_black_24dp);
            Intent intent= new Intent(ListaPedidosPorClienteActivity.this,LoginActivity.class);
            ListaPedidosPorClienteActivity.this.startActivity(intent);
            this.finish();
        }

    }
}
