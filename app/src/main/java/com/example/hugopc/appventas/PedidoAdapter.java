package com.example.hugopc.appventas;

import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidosHolder>  implements Response.Listener<String>, Response.ErrorListener {

    private List<Pedido> PedidosList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"pedido/deletepedido/";
    public String IP_END_POINT_UPD = Cons.getIP_ADDRESS().toString()+"producto/updateproductoplus/";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    @Override
    public void onErrorResponse(VolleyError error) {
        error.getMessage();
    }

    @Override
    public void onResponse(String response) {



    }

    public class PedidosHolder extends RecyclerView.ViewHolder  {
        ImageView ivImage;
        TextView tvIdPedido;
        TextView tvNombre_cliente;
        TextView tvDomicilio;
        TextView tvNombreProducto;
        TextView tv_Cantidad;
        TextView tv_Precio;
        TextView tv_IMporte;
        FloatingActionButton btnDelete;


        public PedidosHolder(View itemView) {
            super(itemView);
            tvIdPedido = itemView.findViewById(R.id.txtIdPedido);
            tvNombre_cliente = itemView.findViewById(R.id.txtNombCliente);
            tvDomicilio = itemView.findViewById(R.id.txtDomicilio);
            tvNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            tv_Cantidad = itemView.findViewById(R.id.txtCantidad);
            tv_IMporte = itemView.findViewById(R.id.txtImporte);
            tv_Precio = itemView.findViewById(R.id.txtPrecio);
            btnDelete = itemView.findViewById(R.id.btnDeletePedido);

            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public PedidoAdapter(List<Pedido> List, Context con) {
        PedidosList = List;
        context = con;
    }

    @Override
    public PedidosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pedidos, parent, false);
        PedidoAdapter.PedidosHolder PedidosHolder = new PedidoAdapter.PedidosHolder(view);
        return PedidosHolder;
    }

    @Override
    public void onBindViewHolder(PedidosHolder holder, int position) {
        final Pedido Pedido = PedidosList.get(position);

        holder.tvIdPedido.setText(Pedido.getId_pedido()+"");
        holder.tvNombre_cliente.setText(Pedido.getNombre_cliente());
        holder.tvDomicilio.setText(Pedido.getDomicilio());
        holder.tvNombreProducto.setText(Pedido.getNombre_producto());
        holder.tv_Cantidad.setText(Pedido.getCantidad()+"");
        holder.tv_Precio.setText(Pedido.getPrecio()+"");
        holder.tv_IMporte.setText(Pedido.getImporte()+"");
        //holder.tv_Cantidad.setText(Pedido.getId_Pedido()+"");
/*
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Quieres editar este registro..?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click Si para editar!")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                //PARTE DONDE SE lanza el activity para ACTUALIZAR7
                                Intent intent= new Intent(context,UpdatePedidoActivity.class);

                                intent.putExtra("id_pedido",Pedido.getId_pedido()+"");
                                intent.putExtra("cantidad",Pedido.getCantidad()+"");
                                context.startActivity(intent);
                                ((Activity)context).finish();


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
        */
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Quieres eliminar este registro..?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click Si para eliminar!")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                System.out.println("ID pedido" + Pedido.getId_pedido());

                                m_actuaNumExistencia(Pedido.getCantidad(),Pedido.getId_producto());


                                if(m_delete(Pedido.getId_pedido())){
                                    Toast toast1 = Toast.makeText(context, "Pedido Eliminado" , Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                    ((Activity)context).recreate();
                                }else{
                                    Toast toast1 = Toast.makeText(context, "No se pudo Elimanr el Pedido" , Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
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

            public void m_actuaNumExistencia(int p_cantidad,int p_id_producto) {
                String URL_F = IP_END_POINT_UPD+p_cantidad+"/"+Ajuste.token;

                int id_prod = p_id_producto;
                jsonObject = new JSONObject();

                try{
                    jsonObject.put("id_producto",id_prod);


                }catch (Exception e){
                    e.getMessage();
                    System.out.println("ERROR" + e.getMessage());
                }

                stringRequest = new StringRequest(Request.Method.PUT, URL_F, new Response.Listener<String>() {
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

            private boolean m_delete(int id_pedido) {
                String URL_DELE_PROD = IP_END_POINT_DELETE+id_pedido+"/"+ Ajuste.token;
                try{
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_PROD, PedidoAdapter.this, PedidoAdapter.this)
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
                    return true;
                }catch (Exception e){
                    e.getMessage();
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        return PedidosList.size();
    }
}

