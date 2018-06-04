package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;

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

public class ListaEmpleadoRolActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EmpleadosRoles = IP_ADDRES + "empleado_rol2/listempleadosRoles/"+Ajuste.token;
    //http://192.168.43.201:8080/Ventas/api/empleado_rol2/listempleadosRoles/6c197a825b46206091fe24305e55075e

    List<EmpleadosRoles> empleadosRolesList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;

    int id_empleado;
    int id_rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empleado_rol);
        empleadosRolesList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerViewEmpleadosRoles);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargaEmpleadosRoles();
        adapter = new EmpleadosRolesAdapter(empleadosRolesList,this);
        recyclerView.setAdapter(adapter);
    }

    private void m_cargaEmpleadosRoles() {

        StringRequest request = new StringRequest(Request.Method.GET,URL_EmpleadosRoles,this,this){
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

        EmpleadosRoles objEmpleadoRol;
        if (!response.isEmpty()){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("empleado_rol");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objEmpleadoRol = new EmpleadosRoles();
                objEmpleadoRol.setNombre_empleado(jsonObject1.getString("nombre_empleado"));
                objEmpleadoRol.setApellido_paterno(jsonObject1.getString("apellido_paterno"));
                objEmpleadoRol.setApellido_materno(jsonObject1.getString("apellido_materno"));
                objEmpleadoRol.setNombre_rol(jsonObject1.getString("nombre_rol"));
                objEmpleadoRol.setId_rol(jsonObject1.getInt("id_rol"));
                objEmpleadoRol.setNickname(jsonObject1.getString("nickname"));
                objEmpleadoRol.setId_usuario(jsonObject1.getInt("id_usuario"));
                objEmpleadoRol.setId_empleado(jsonObject1.getInt("id_empleado"));

                empleadosRolesList.add(objEmpleadoRol);
            }
            adapter = new EmpleadosRolesAdapter(empleadosRolesList,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){

            e.printStackTrace();
        }
        }
        else{
                KToast.customColorToast(ListaEmpleadoRolActivity.this, "SESION EXPIRADA", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_LONG, R.color.warning, R.drawable.ic_warning_black_24dp);
                Intent intent= new Intent(ListaEmpleadoRolActivity.this,LoginActivity.class);
                ListaEmpleadoRolActivity.this.startActivity(intent);
                this.finish();
            }

    }
}
