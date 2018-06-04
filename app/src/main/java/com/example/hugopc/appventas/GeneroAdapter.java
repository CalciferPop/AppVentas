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


public class GeneroAdapter extends RecyclerView.Adapter<GeneroAdapter.GenerosHolder> implements Response.Listener<String>, Response.ErrorListener {

    private List<Genero> GenerosList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"genero/deletegenero/";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }

    public  class GenerosHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvDescGenero;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;

        public GenerosHolder(View itemView) {
            super(itemView);
            tvDescGenero = itemView.findViewById(R.id.txtDescGenero);
            btnDelete = itemView.findViewById(R.id.btnDeleteGenero);
            btnUpdate  = itemView.findViewById(R.id.btnUpdateGenero);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public GeneroAdapter(List<Genero> List, Context con) {
        GenerosList = List;
        context = con;
    }

    @Override
    public GenerosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_genero, parent, false);
        GeneroAdapter.GenerosHolder GenerosHolder = new GeneroAdapter.GenerosHolder(view);
        return GenerosHolder;
    }

    @Override
    public void onBindViewHolder(GenerosHolder holder, int position) {
        final Genero Genero = GenerosList.get(position);
        holder.tvDescGenero.setText(Genero.getdesc_genero());
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
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                //PARTE DONDE SE lanza el activity para ACTUALIZAR7
                                Intent intent = new Intent(context, UpdateGeneroActivity.class);

                                intent.putExtra("id_genero", Genero.getid_genero() + "");
                                context.startActivity(intent);
                                ((Activity)context).finish();


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                System.out.println("ID Genero" + Genero.getid_genero());

                                if (m_delete(Genero.getid_genero())) {
                                    Toast toast1 = Toast.makeText(context, "Genero Eliminado", Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                    ((Activity)context).recreate();
                                } else {
                                    Toast toast1 = Toast.makeText(context, "No se pudo Eliminar el genero", Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                }


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

            private boolean m_delete(int id_genero) {
                String URL_DELE_GENERO = IP_END_POINT_DELETE + id_genero + "/" + Ajuste.token;
                try {
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_GENERO,   GeneroAdapter.this, GeneroAdapter.this) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return jsonObject.toString().getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                Log.e("Error", uee.toString());
                                return null;
                            }
                        }

                        @Override
                        public Map<String, String> getHeaders() {
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
                } catch (Exception e) {
                    e.getMessage();
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        return GenerosList.size();
    }
}