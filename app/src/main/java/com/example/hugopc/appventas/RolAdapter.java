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


public class RolAdapter extends RecyclerView.Adapter<RolAdapter.RolsHolder> implements Response.Listener<String>, Response.ErrorListener{

    private List<Rol> RolsList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"rol/deleterol/";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }

    public class RolsHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvNombreRol;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;

        public RolsHolder(View itemView) {
            super(itemView);
            tvNombreRol = itemView.findViewById(R.id.txtNombreRol);
            btnDelete = itemView.findViewById(R.id.btnDeleteRol);
            btnUpdate  = itemView.findViewById(R.id.btnUpdateRol);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public RolAdapter(List<Rol> List, Context con) {
        RolsList = List;
        context = con;
    }

    @Override
    public RolsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_roles, parent, false);
        RolAdapter.RolsHolder RolsHolder = new RolAdapter.RolsHolder(view);
        return RolsHolder;
    }

    @Override
    public void onBindViewHolder(RolsHolder holder, int position) {
        final Rol Rol = RolsList.get(position);
        holder.tvNombreRol.setText(Rol.getNombre_rol());
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
                                Intent intent = new Intent(context, UpdateRolActivity.class);

                                intent.putExtra("id_rol", Rol.getId_rol() + "");
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
                                System.out.println("ID ROL" + Rol.getId_rol());

                                if (m_delete(Rol.getId_rol())) {
                                    Toast toast1 = Toast.makeText(context, "Rol Eliminado", Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                    ((Activity)context).recreate();
                                } else {
                                    Toast toast1 = Toast.makeText(context, "No se pudo Elimanr el rol", Toast.LENGTH_SHORT);
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

            private boolean m_delete(int id_rol) {
                String URL_DELE_ROL = IP_END_POINT_DELETE + id_rol + "/" + Ajuste.token;
                try {
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_ROL, RolAdapter.this, RolAdapter.this) {
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

        return RolsList.size();
    }
}