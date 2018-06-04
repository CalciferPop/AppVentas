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


public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClientesHolder>  implements Response.Listener<String>, Response.ErrorListener {

    private List<Cliente> ClientesList;
    Context context;
    public Constantes Cons = new Constantes();
    public String IP_END_POINT_DELETE = Cons.getIP_ADDRESS().toString()+"cliente/deletecliente/";
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

    public class ClientesHolder extends RecyclerView.ViewHolder  {
        ImageView ivImage;
        TextView tvNombreCliente;
        TextView tvapePatCli;
        TextView tvapeMatCli;
        TextView tvtelefonoCli;
        TextView tvEmailCli;
        TextView tv_IDCliente;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;
        FloatingActionButton btnCorreo;

        public ClientesHolder(View itemView) {
            super(itemView);
            tvNombreCliente = itemView.findViewById(R.id.txtNombCliente);
            tvapePatCli = itemView.findViewById(R.id.txtApePatCli);
            tvapeMatCli = itemView.findViewById(R.id.txtApeMatCli);
            tvtelefonoCli = itemView.findViewById(R.id.txtTelefonoCli);
            tvEmailCli = itemView.findViewById(R.id.txtEmailCli);
            tv_IDCliente =  itemView.findViewById(R.id.txtid_cliente);
            btnDelete = itemView.findViewById(R.id.btnDeleteCliente);
            btnUpdate = itemView.findViewById(R.id.btnUpdateCliente);
            btnCorreo = itemView.findViewById(R.id.btnEnviarCorreoCliente);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public ClienteAdapter(List<Cliente> List, Context con) {
        ClientesList = List;
        context = con;
    }

    @Override
    public ClientesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_clientes, parent, false);
        ClienteAdapter.ClientesHolder ClientesHolder = new ClienteAdapter.ClientesHolder(view);
        return ClientesHolder;
    }

    @Override
    public void onBindViewHolder(ClientesHolder holder, int position) {
        final Cliente Cliente = ClientesList.get(position);

        holder.tvNombreCliente.setText(Cliente.getNombre_cliente());
        holder.tvapePatCli.setText(Cliente.getApellido_paterno());
        holder.tvapeMatCli.setText(Cliente.getApellido_materno());
        holder.tvtelefonoCli.setText(Cliente.getTelefono());
        holder.tvEmailCli.setText(Cliente.getEmail());
        holder.tv_IDCliente.setText(Cliente.getId_cliente()+"");
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
                                Intent intent= new Intent(context,UpdateClienteActivity.class);

                                intent.putExtra("id_cliente",Cliente.getId_cliente()+"");
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
                                System.out.println("ID CLIENTE" + Cliente.getId_cliente());

                               if(m_delete(Cliente.getId_cliente())){
                                   Toast toast1 = Toast.makeText(context, "Cliente Eliminado" , Toast.LENGTH_SHORT);
                                   toast1.setGravity(Gravity.CENTER, 0, 0);
                                   toast1.show();
                                   ((Activity)context).recreate();
                               }else{
                                   Toast toast1 = Toast.makeText(context, "No se pudo Eliminar el cliente" , Toast.LENGTH_SHORT);
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

            private boolean m_delete(int id_cliente) {
                String URL_DELE_CLI = IP_END_POINT_DELETE+id_cliente+"/"+ Ajuste.token;
                try{
                    stringRequest = new StringRequest(Request.Method.DELETE, URL_DELE_CLI, ClienteAdapter.this, ClienteAdapter.this)
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
        holder.btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Cliente.getEmail().toString();
                String cliente = Cliente.getNombre_cliente().toString();
                m_enviaEmail(correo,cliente);
            }

            private void m_enviaEmail(String p_correo,String p_cliente) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Tienda de Mascotas Star");
                intent.putExtra(Intent.EXTRA_TEXT, "Apreciable " + p_cliente + " nos comunicamos para ");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {p_correo});
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return ClientesList.size();
    }
}

