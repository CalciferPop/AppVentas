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
import android.widget.Button;

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
import com.onurkaganaldemir.ktoastlib.KToast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmpleadosRolesAdapter extends RecyclerView.Adapter<EmpleadosRolesAdapter.EmpleadosRolesHolder>  implements Response.Listener<String>, Response.ErrorListener {

    private List<EmpleadosRoles> EmpleadosRolesList;
    Context context;
    public   Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"empleado_rol/deleteempleado_rol/";
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

    public class EmpleadosRolesHolder extends RecyclerView.ViewHolder  {
        ImageView ivImage;
        TextView tvNombreEmpleado;
        TextView tvApellidoPaterno;
        TextView tvApellidoMaterno;
        TextView tvNickname;
        TextView tvRol;
        TextView tvLongitud;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;


        public EmpleadosRolesHolder(View itemView) {
            super(itemView);
            tvNombreEmpleado = itemView.findViewById(R.id.txtNombEmplEMPROL);
            tvApellidoPaterno = itemView.findViewById(R.id.txtApePatEMPROL);
            tvApellidoMaterno = itemView.findViewById(R.id.txtApeMatEMPROL);
            tvNickname = itemView.findViewById(R.id.txtNicknameEMPROL);
            tvRol =  itemView.findViewById(R.id.txtRolEMPROL);
            btnDelete = itemView.findViewById(R.id.btnDeleteEmpleadoRol);
            btnUpdate = itemView.findViewById(R.id.btnUpdateEmpleadoRol);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public EmpleadosRolesAdapter(List<EmpleadosRoles> List, Context con) {
        EmpleadosRolesList = List;
        context = con;
    }

    @Override
    public EmpleadosRolesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empleado_rol, parent, false);
        EmpleadosRolesAdapter.EmpleadosRolesHolder EmpleadosRolesHolder = new EmpleadosRolesAdapter.EmpleadosRolesHolder(view);
        return EmpleadosRolesHolder;
    }

    @Override
    public void onBindViewHolder(EmpleadosRolesHolder holder, int position) {
        final EmpleadosRoles EmpleadosRoles = EmpleadosRolesList.get(position);

        holder.tvNombreEmpleado.setText(EmpleadosRoles.getNombre_empleado());
        holder.tvApellidoPaterno.setText(EmpleadosRoles.getApellido_paterno());
        holder.tvApellidoMaterno.setText(EmpleadosRoles.getApellido_materno());
        holder.tvNickname.setText(EmpleadosRoles.getNickname());
        holder.tvRol.setText(EmpleadosRoles.getNombre_rol());
        //holder.tv_IDEmpleadosRoles.setText(EmpleadosRoles.getId_EmpleadosRoles()+"");
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
                                Intent intent= new Intent(context,UpdateEmpleadosRolesActivity.class);

                                intent.putExtra("id_empleado",EmpleadosRoles.getId_empleado()+"");
                                intent.putExtra("nombre_empleado",EmpleadosRoles.getNombre_empleado());
                                intent.putExtra("id_rol",EmpleadosRoles.getId_rol()+"");
                                intent.putExtra("nombre_rol",EmpleadosRoles.getNombre_rol());
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
                                //System.out.println("ID EmpleadosRoles" + EmpleadosRoles.getId_EmpleadosRoles());

                                if(m_delete(EmpleadosRoles.getId_empleado(),EmpleadosRoles.getId_rol())){
                                    Toast toast1 = Toast.makeText(context, "EmpleadosRoles Eliminado" , Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();
                                    ((Activity)context).recreate();
                                }else{
                                    Toast toast1 = Toast.makeText(context, "No se pudo Elimanr el EmpleadosRoles" , Toast.LENGTH_SHORT);
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

            private boolean m_delete(int id_empleado,int id_rol) {
                String URL_DELE_PROD = IP_END_POINT_DELETE+id_empleado+"/"+ id_rol +"/"+Ajuste.token;
                try{
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_PROD, EmpleadosRolesAdapter.this, EmpleadosRolesAdapter.this)
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

        return EmpleadosRolesList.size();
    }
}

