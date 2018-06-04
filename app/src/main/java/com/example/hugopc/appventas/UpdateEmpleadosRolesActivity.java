package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateEmpleadosRolesActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener,Spinner.OnItemSelectedListener {


    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_empleados = IP_ADDRES + "empleado/listempleados/"+Ajuste.token;
    private static String URL_roles = IP_ADDRES + "rol/listroles/"+Ajuste.token;

    private Spinner spinnerEmpleados,spinnerRoles;
    private ArrayList<String> empleados;
    private ArrayList<String> roles;
    private JSONArray result;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_empleado=0;
    int id_rol=0;
    int id_empleadoNuevo=0;
    int id_rolNuevo=0;

    @BindView(R.id.edtIdEmpleadoAnterior)
    EditText edtIDEmpleadoAnterior;
    @BindView(R.id.edtIdRolAnterior)
    EditText edtIDRolAnterior;

    @BindView(R.id.btnUPDEmpleadosRoles)
    Button btnActuaEmpleadoRol;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_empleados_roles);
        ButterKnife.bind(this);
        Intent intent1 = getIntent();
        final String id_empleadoP = intent1.getStringExtra("id_empleado");
        final String nombre_empleado = intent1.getStringExtra("nombre_empleado");
        final String id_rolP = intent1.getStringExtra("id_rol");
        final String nombre_rol = intent1.getStringExtra("nombre_rol");
        edtIDEmpleadoAnterior.setText(id_empleadoP + ":" + nombre_empleado);
        edtIDRolAnterior.setText(id_rolP+":" + nombre_rol);
        id_empleado = Integer.valueOf(id_empleadoP);
        id_rol = Integer.valueOf(id_rolP);
        requestQueue = Volley.newRequestQueue(this);
        empleados = new ArrayList<String>();
        roles = new ArrayList<String>();
        spinnerEmpleados = (Spinner) findViewById(R.id.spinnerEmpleados);
        spinnerRoles = (Spinner) findViewById(R.id.spinnerRoles);
        spinnerEmpleados.setOnItemSelectedListener(this);
        spinnerRoles.setOnItemSelectedListener(this);
        m_cargaEmpleados();
        m_cargaRoles();
    }

    private void m_cargaRoles() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_roles, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Rol objRol;
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("rol");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        objRol = new Rol();
                        objRol.setId_rol(jsonObject1.getInt("id_rol"));
                        objRol.setNombre_rol(jsonObject1.getString("nombre_rol"));

                        String rol = jsonObject1.getInt("id_rol")+":   "+  jsonObject1.getString("nombre_rol");
                        roles.add(rol);
                    }


                }catch (JSONException e){

                    e.printStackTrace();
                }
                spinnerRoles.setAdapter(new ArrayAdapter<String>(UpdateEmpleadosRolesActivity.this, android.R.layout.simple_spinner_dropdown_item, roles));
            }
        }, this){
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

    private void m_cargaEmpleados() {
        StringRequest request = new StringRequest(Request.Method.GET,URL_empleados,this,this){
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.getItemAtPosition(i);


        switch (adapterView.getId())
        {
            case R.id.spinnerEmpleados:

                String texto= spinnerEmpleados.getSelectedItem().toString();
                m_obtenerIDEmpleado(texto);

                break;

            case R.id.spinnerRoles:

                String texto2= spinnerRoles.getSelectedItem().toString();
                m_obtenerIDRol(texto2);
                break;
        }
    }

    private void m_obtenerIDRol(String p_cadena) {
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_rolNuevo = Integer.valueOf(id_);
        System.out.println("ID Rol anterior:" +id_rol);
        System.out.println("ID Rol Nuevo:" +id_rolNuevo);
    }

    private void m_obtenerIDEmpleado(String p_cadena) {
        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_empleadoNuevo = Integer.valueOf(id_);
        System.out.println("ID Empleado anterior:" +id_empleado);
        System.out.println("ID Empleado Nuevo:" +id_empleadoNuevo);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

        Empleado objEmpleado;
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("empleado");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objEmpleado = new Empleado();
                objEmpleado.setId_empleado(jsonObject1.getInt("id_empleado"));
                objEmpleado.setNombre_empleado(jsonObject1.getString("nombre_empleado"));

                String empleado = jsonObject1.getInt("id_empleado")+":   "+  jsonObject1.getString("nombre_empleado");
                empleados.add(empleado);
            }


        }catch (JSONException e){

            e.printStackTrace();
        }

        spinnerEmpleados.setAdapter(new ArrayAdapter<String>(UpdateEmpleadosRolesActivity.this, android.R.layout.simple_spinner_dropdown_item, empleados));
    }

    @OnClick({R.id.btnUPDEmpleadosRoles})
    public void OnClick4(View v){
        m_actualiza(id_empleado,id_empleadoNuevo,id_rol,id_rolNuevo);
        KToast.customColorToast(UpdateEmpleadosRolesActivity.this, "Cambios Realizados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(this,ListaEmpleadoRolActivity.class);
        this.startActivity(intent);
        finish();

    }

    private void m_actualiza(int id_empleado, int id_empleadoNuevo, int id_rol, int id_rolNuevo) {

        String URL_UPD_RC = IP_ADDRES + "empleado_rol/updateempleado_rol/"+Ajuste.token;

        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_empleado",id_empleado);
            jsonObject.put("id_empleadoN",id_empleadoNuevo);
            jsonObject.put("id_rol",id_rol);
            jsonObject.put("id_rolN",id_rolNuevo);


        }catch (Exception e){
            e.getMessage();
            System.out.println("ERROR" + e.getMessage());
        }

        stringRequest = new StringRequest(Request.Method.PUT, URL_UPD_RC, new Response.Listener<String>() {
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

}
