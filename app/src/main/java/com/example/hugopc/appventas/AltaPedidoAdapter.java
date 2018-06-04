package com.example.hugopc.appventas;

import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


 class AltaPedidoAdpater extends RecyclerView.Adapter<AltaPedidoAdpater.AltaPedidosHolder>  implements Response.Listener<String>, Response.ErrorListener {

    private List<Producto> ProductosList;
    Context context;
    public   Constantes Cons = new Constantes();

    public String IP_END_POINT_ADD = Cons.getIP_ADDRESS().toString()+"pedido/insertpedido/"+Ajuste.token;
    public String IP_END_POINT_UPD = Cons.getIP_ADDRESS().toString()+"producto/updateproductominus/";
    private RequestQueue requestQueue;
    private StringRequest stringRequest,stringRequest2;
    private JSONObject jsonObject,jsonObject2;
     int cant ;




     @Override
    public void onErrorResponse(VolleyError error) {
        error.getMessage();
    }

    @Override
    public void onResponse(String response) {



    }

    public class AltaPedidosHolder extends RecyclerView.ViewHolder  {
        ImageView ivImage;
        TextView tvNombreproducto;
        TextView tvDescProd;
        TextView tvPrecioProd;
        TextView tvNumExistProd;
        EditText edtCantidad;
        FloatingActionButton btnAlta;



        public AltaPedidosHolder(View itemView) {
            super(itemView);
            tvNombreproducto = itemView.findViewById(R.id.txtNombProdAltaPed);
            tvDescProd = itemView.findViewById(R.id.txtDescProdAltaPed);
            tvPrecioProd = itemView.findViewById(R.id.txtPrecioProdAltaPed);
            tvNumExistProd = itemView.findViewById(R.id.txtNumExistenciaAltaPed);
            btnAlta = itemView.findViewById(R.id.btnAltaPedido);
            edtCantidad = itemView.findViewById(R.id.edtCantidadAltaPed);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public AltaPedidoAdpater(List<Producto> List, Context con) {
        ProductosList = List;
        context = con;
    }

    @Override
    public AltaPedidosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alta_pedido, parent, false);
        AltaPedidoAdpater.AltaPedidosHolder AltaPedidosHolder = new AltaPedidoAdpater.AltaPedidosHolder(view);

        return AltaPedidosHolder;
    }

    @Override
    public void onBindViewHolder(final AltaPedidosHolder holder, int position) {
        final Producto Producto = ProductosList.get(position);
        holder.tvNombreproducto.setText(Producto.getNombre_producto());
        holder.tvDescProd.setText(Producto.getDescripcion());
        holder.tvPrecioProd.setText(Producto.getPrecio() +"");
        holder.tvNumExistProd.setText(Producto.getNum_existencia() + "");
        holder.btnAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Quieres agregar este registro..?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click Si para agregar!")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                //PARTE DONDE SE lanza el activity para ACTUALIZAR7
                                if(holder.edtCantidad.getText().length()<=0) {


                                    m_seguir2();

                                }else {
                                    cant = Integer.valueOf(holder.edtCantidad.getText().toString());
                                    int exist = Producto.getNum_existencia();
                                    if (cant == 0) {
                                        Toast toast1 =
                                                Toast.makeText(context,
                                                        "NO hay productos en existencia", Toast.LENGTH_SHORT);
                                        toast1.show();
                                    } else if (cant > exist) {
                                        Toast toast1 =
                                                Toast.makeText(context,
                                                        "NO puedes comprar mas productos de los que hay en existencia", Toast.LENGTH_SHORT);
                                        toast1.show();
                                    } else {
                                        m_insertaPedido();
                                        m_actuaNumExistencia(Integer.valueOf(holder.edtCantidad.getText().toString()));
                                        m_seguir();

                                    }

                                }
                            }


                            public void m_actuaNumExistencia(Integer p_cantidad) {
                                String URL_F = IP_END_POINT_UPD+p_cantidad+"/"+Ajuste.token;

                                int id_prod = Producto.getId_producto();
                                jsonObject2 = new JSONObject();

                                try{
                                    jsonObject2.put("id_producto",id_prod);


                                }catch (Exception e){
                                    e.getMessage();
                                    System.out.println("ERROR" + e.getMessage());
                                }

                                stringRequest2 = new StringRequest(Request.Method.PUT, URL_F, new Response.Listener<String>() {
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
                                            return jsonObject2.toString().getBytes("utf-8");
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

                                requestQueue.add(stringRequest2);
                            }


                            private void m_seguir2() {

                                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                                        context);

                                alertDialogBuilder2.setTitle("Debes propocionar la cantidad de productos a comprar");

                                alertDialogBuilder2
                                        .setMessage("Click Si regresar a comprar, NO para salir")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //Intent intent = new Intent(context, AltaPedidoActivity.class);
                                                //context.startActivity(intent);
                                                dialogInterface.cancel();



                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(context, PedidoActivity.class);
                                                context.startActivity(intent);
                                                ((Activity)context).finish();
                                            }
                                        });

                                AlertDialog alertDialog3 = alertDialogBuilder2.create();

                                // show it
                                alertDialog3.show();

                            }

                            private void m_seguir() {

                                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                                        context);

                                alertDialogBuilder2.setTitle("Quieres Seguir comprando..?");

                                alertDialogBuilder2
                                        .setMessage("Click Si para seguir comprando, NO para salir")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //Intent intent = new Intent(context, AltaPedidoActivity.class);
                                                //context.startActivity(intent);
                                                dialogInterface.cancel();

                                                ((Activity)context).recreate();

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(context, PedidoActivity.class);
                                                context.startActivity(intent);
                                                ((Activity)context).finish();
                                            }
                                        });

                                AlertDialog alertDialog3 = alertDialogBuilder2.create();

                                // show it
                                alertDialog3.show();

                            }



                            public  boolean m_compruebaConexion(Context context) {

                                boolean connected = false;

                                ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                                // Recupera todas las redes (tanto móviles como wifi)
                                NetworkInfo[] redes = connec.getAllNetworkInfo();

                                for (int i = 0; i < redes.length; i++) {
                                    // Si alguna red tiene conexión, se devuelve true
                                    if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                                        connected = true;
                                    }
                                }
                                return connected;
                            }



                            private void m_insertaPedido() {

                                if(!m_compruebaConexion(context)){
                                    Toast toast1 =
                                            Toast.makeText(context,
                                                    "NO hay conexion a internet, su pedido se almacenara de manera offline", Toast.LENGTH_SHORT);
                                    toast1.show();
                                }else{
                                    String URL_G = Cons.getIP_ADDRESS().toString()+"pedido/insertpedido/"+Ajuste.token;
                                    Intent intent = ((Activity) context).getIntent();
                                    String id_client = intent.getStringExtra(("id_cliente"));
                                    int id_cli = Integer.valueOf(id_client);

                                    int cantidad = Integer.valueOf(holder.edtCantidad.getText().toString());
                                    double importe = Producto.getPrecio() * cantidad;
                                    int id_prod = Producto.getId_producto();



                                    System.out.println("ID CLIENTE: " + Ajuste.id_client + " O " + id_cli);
                                    System.out.println("Cantidad: " + cantidad);
                                    System.out.println("IMPORTE: " + importe);
                                    System.out.println("ID PRODUCTO" + id_prod);

                                    jsonObject = new JSONObject();

                                    try{

                                        jsonObject.put("id_cliente",id_cli);
                                        jsonObject.put("cantidad",cantidad);
                                        jsonObject.put("importe",importe);
                                        jsonObject.put("id_producto",id_prod);


                                        System.out.println(jsonObject);

                                    }catch (Exception e){
                                        e.getMessage();
                                    }
                                    stringRequest = new StringRequest(Request.Method.POST, URL_G, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            System.out.println(error.getCause());
                                            System.out.println(error.getMessage());
                                            System.out.println(error.getStackTrace());
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


                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
            }



    @Override
    public int getItemCount() {

        return ProductosList.size();
    }
}

