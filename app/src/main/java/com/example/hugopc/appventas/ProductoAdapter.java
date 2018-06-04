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


public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductosHolder>  implements Response.Listener<String>, Response.ErrorListener {

    private List<Producto> ProductosList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"producto/deleteproducto/";
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

    public class ProductosHolder extends RecyclerView.ViewHolder  {
        ImageView ivImage;
        TextView tvNombreproducto;
        TextView tvDescProd;
        TextView tvPrecioProd;
        TextView tvNumExistProd;
        TextView tv_IDproducto;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;

        public ProductosHolder(View itemView) {
            super(itemView);
            tvNombreproducto = itemView.findViewById(R.id.txtNombProd);
            tvDescProd = itemView.findViewById(R.id.txtDescProd);
            tvPrecioProd = itemView.findViewById(R.id.txtPrecioProd);
            tvNumExistProd = itemView.findViewById(R.id.txtNumExistencia);
            btnDelete = itemView.findViewById(R.id.btnDeleteProducto);
            btnUpdate = itemView.findViewById(R.id.btnUpdateProducto);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public ProductoAdapter(List<Producto> List, Context con) {
        ProductosList = List;
        context = con;
    }

    @Override
    public ProductosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_productos, parent, false);
        ProductoAdapter.ProductosHolder ProductosHolder = new ProductoAdapter.ProductosHolder(view);
        return ProductosHolder;
    }

    @Override
    public void onBindViewHolder(ProductosHolder holder, int position) {
        final Producto Producto = ProductosList.get(position);

        holder.tvNombreproducto.setText(Producto.getNombre_producto());
        holder.tvDescProd.setText(Producto.getDescripcion());
        holder.tvPrecioProd.setText(Producto.getPrecio() +"");
        holder.tvNumExistProd.setText(Producto.getNum_existencia() + "");
        //holder.tv_IDproducto.setText(Producto.getId_producto()+"");
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
                                Intent intent= new Intent(context,UpdateProductoActivity.class);

                                intent.putExtra("id_producto",Producto.getId_producto()+"");
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
                                System.out.println("ID Producto" + Producto.getId_producto());

                                if(m_delete(Producto.getId_producto())){
                                    Toast toast1 = Toast.makeText(context, "Producto Eliminado" , Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                    ((Activity)context).recreate();
                                }else{
                                    Toast toast1 = Toast.makeText(context, "No se pudo Elimanr el Producto" , Toast.LENGTH_SHORT);
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

            private boolean m_delete(int id_producto) {
                String URL_DELE_PROD = IP_END_POINT_DELETE+id_producto+"/"+ Ajuste.token;
                try{
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_PROD, ProductoAdapter.this, ProductoAdapter.this)
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

        return ProductosList.size();
    }
}

