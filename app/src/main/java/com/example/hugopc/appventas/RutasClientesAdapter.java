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


public class RutasClientesAdapter extends RecyclerView.Adapter<RutasClientesAdapter.RutasClientessHolder>  implements Response.Listener<String>, Response.ErrorListener {

    private List<RutasClientes> RutasClientessList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"rutasclientes2/deleteRutasClientes/";
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

    public class RutasClientessHolder extends RecyclerView.ViewHolder  {
        ImageView ivImage;
        TextView tvIdRuta;
        TextView tvDescRuta;
        TextView tvIDCliente;
        TextView tvNombreCliente;
        TextView tvLatitud;
        TextView tvLongitud;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;
        FloatingActionButton btnPosicion;

        public RutasClientessHolder(View itemView) {
            super(itemView);
            tvIdRuta = itemView.findViewById(R.id.txtIdRuta);
            tvDescRuta = itemView.findViewById(R.id.txtDescRuta);
            tvIDCliente = itemView.findViewById(R.id.txtIDCliente);
            tvNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
            tvLatitud =  itemView.findViewById(R.id.txtLatitud);
            tvLongitud = itemView.findViewById(R.id.txtLongitud);
            btnDelete = itemView.findViewById(R.id.btnDeleteRutasClientes);
            btnUpdate = itemView.findViewById(R.id.btnUpdateRutaClients);
            btnPosicion = itemView.findViewById(R.id.btnMarcaPosicionCliente);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public RutasClientesAdapter(List<RutasClientes> List, Context con) {
        RutasClientessList = List;
        context = con;
    }

    @Override
    public RutasClientessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rutas_clientes, parent, false);
        RutasClientesAdapter.RutasClientessHolder RutasClientessHolder = new RutasClientesAdapter.RutasClientessHolder(view);
        return RutasClientessHolder;
    }

    @Override
    public void onBindViewHolder(RutasClientessHolder holder, int position) {
        final RutasClientes RutasClientes = RutasClientessList.get(position);

        holder.tvIdRuta.setText(RutasClientes.getId_ruta() + "");
        holder.tvDescRuta.setText(RutasClientes.getDescripcion());
        holder.tvIDCliente.setText(RutasClientes.getId_cliente() +"");
        holder.tvNombreCliente.setText(RutasClientes.getNombre_cliente());
        holder.tvLatitud.setText(RutasClientes.getLatitud());
        holder.tvLongitud.setText(RutasClientes.getLongitud());
        //holder.tv_IDRutasClientes.setText(RutasClientes.getId_RutasClientes()+"");
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
                                Intent intent= new Intent(context,UpdateRutasClientesActivity.class);

                                intent.putExtra("id_ruta",RutasClientes.getId_ruta()+"");
                                intent.putExtra("id_cliente",RutasClientes.getId_cliente()+"");
                                intent.putExtra("nombre_cliente",RutasClientes.getNombre_cliente());
                                intent.putExtra("descripcion", RutasClientes.getDescripcion());
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
                                //System.out.println("ID RutasClientes" + RutasClientes.getId_RutasClientes());

                                if(m_delete(RutasClientes.getId_ruta(),RutasClientes.getId_cliente())){
                                    Toast toast1 = Toast.makeText(context, "RutasClientes Eliminado" , Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                    ((Activity)context).finish();
                                }else{
                                    Toast toast1 = Toast.makeText(context, "No se pudo Elimanr el RutasClientes" , Toast.LENGTH_SHORT);
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

            private boolean m_delete(int id_ruta,int id_cliente) {
                String URL_DELE_PROD = IP_END_POINT_DELETE+id_ruta+"/"+ id_cliente +"/"+ Ajuste.token;
                try{
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_PROD, RutasClientesAdapter.this, RutasClientesAdapter.this)
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

        return RutasClientessList.size();
    }
}

