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

import android.text.util.Linkify;


public class EmpleadoAdapter extends RecyclerView.Adapter<EmpleadoAdapter.EmpleadosHolder> implements Response.Listener<String>, Response.ErrorListener{

    private List<Empleado> EmpleadosList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"empleado/deleteempleado/";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }

    public  class EmpleadosHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvEmpleadoName;
        TextView tvapePatEmpl;
        TextView tvapeMatEmpl;
        TextView tvfechaNacEmp;
        TextView tvTelefono;
        TextView tvEmail;
        TextView tvRFC;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;

        public EmpleadosHolder(View itemView) {
            super(itemView);
            tvEmpleadoName = itemView.findViewById(R.id.txtNombEmpl);
            tvapePatEmpl = itemView.findViewById(R.id.txtApePat);
            tvapeMatEmpl = itemView.findViewById(R.id.txtApeMat);
            tvEmail = itemView.findViewById(R.id.txtEmailEmpl);
            tvTelefono = itemView.findViewById(R.id.txtTelEmpl);
            btnDelete = itemView.findViewById(R.id.btnDeleteEmpleado);
            btnUpdate  = itemView.findViewById(R.id.btnUpdateEmpleado);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public EmpleadoAdapter(List<Empleado> List, Context con) {
        EmpleadosList = List;
        context = con;
    }

    @Override
    public EmpleadosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empleados, parent, false);
        EmpleadoAdapter.EmpleadosHolder EmpleadosHolder = new EmpleadoAdapter.EmpleadosHolder(view);
        return EmpleadosHolder;
    }

    @Override
    public void onBindViewHolder(EmpleadosHolder holder, int position) {
        final Empleado Empleado = EmpleadosList.get(position);

        holder.tvEmpleadoName.setText(Empleado.getNombre_empleado());
        holder.tvapePatEmpl.setText(Empleado.getApellido_paterno());
        holder.tvapeMatEmpl.setText(Empleado.getApellido_materno());
        holder.tvEmail.setText(Empleado.getEmail_empleado());
        holder.tvTelefono.setText(Empleado.getTelefono());
        Linkify.addLinks(holder.tvEmail,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(holder.tvTelefono,Linkify.PHONE_NUMBERS);
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
                                Intent intent= new Intent(context,UpdateEmpleadoActivity.class);

                                intent.putExtra("id_empleado",Empleado.getId_empleado()+"");
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
                                System.out.println("ID EMPLEADO" + Empleado.getId_empleado());

                                if(m_delete(Empleado.getId_empleado())){
                                    Toast toast1 = Toast.makeText(context, "Empleado Eliminado" , Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER, 0, 0);
                                    toast1.show();

                                    ((Activity)context).recreate();
                                }else{
                                    Toast toast1 = Toast.makeText(context, "No se pudo Elimanr el empleado" , Toast.LENGTH_SHORT);
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

            private boolean m_delete(int id_empleado) {
                String URL_DELE_Empl = IP_END_POINT_DELETE+id_empleado+"/"+ Ajuste.token;
                try{
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_Empl, EmpleadoAdapter.this, EmpleadoAdapter.this)
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
                    System.out.println("ERROR MERRY");
                    System.out.println(e.getMessage());
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return EmpleadosList.size();
    }
}