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

public class AltaEmpleadoRolActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener,Spinner.OnItemSelectedListener{


    private Spinner spinnerEmpleados;
    private Spinner spinnerRoles;

    private ArrayList<String> empleados;
    private ArrayList<String> roles;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();
    private static String URL_empleados = IP_ADDRES + "empleado/listempleados/"+Ajuste.token;
    private static String URL_roles = IP_ADDRES + "rol/listroles/"+Ajuste.token;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_empleado=0;
    int id_rol=0;

    @BindView(R.id.btnAltaEmpleadoRol)
    Button btnAltaEmpleadoRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_empleado_rol);
        ButterKnife.bind(this);
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


    private void m_cargaEmpleados() {

        StringRequest request = new StringRequest(Request.Method.GET, URL_empleados, new Response.Listener<String>() {
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

                spinnerEmpleados.setAdapter(new ArrayAdapter<String>(AltaEmpleadoRolActivity.this, android.R.layout.simple_spinner_dropdown_item, empleados));
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


    private void m_cargaRoles() {
        StringRequest request = new StringRequest(Request.Method.GET,URL_roles,this,this){
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
        id_rol = Integer.valueOf(id_);
        System.out.println("ID Rol:" +id_rol);

    }

    private void m_obtenerIDEmpleado(String p_cadena) {

        StringTokenizer st = new StringTokenizer(p_cadena,":");
        String id_ = st.nextToken();
        id_empleado = Integer.valueOf(id_);
        System.out.println("ID Empleado:" +id_empleado);

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

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
        spinnerRoles.setAdapter(new ArrayAdapter<String>(AltaEmpleadoRolActivity.this, android.R.layout.simple_spinner_dropdown_item, roles));
    }


    @OnClick({R.id.btnAltaEmpleadoRol})
    public void OnClick3(View v){

        m_altaEmpleadoRol(id_empleado,id_rol);
    }

    private void m_altaEmpleadoRol(int p_id_empleado, int p_id_rol) {

        String URL = IP_ADDRES+"empleado_rol/insertempleado_rol/"+Ajuste.token;

        jsonObject = new JSONObject();

        try{

            jsonObject.put("id_empleado",p_id_empleado);
            jsonObject.put("id_rol",p_id_rol);


        }catch (Exception e){
            e.getMessage();
        }
        System.out.println(jsonObject);
        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
        KToast.customColorToast(AltaEmpleadoRolActivity.this, "Registro Empleado - Rol Insertado", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_warning_black_24dp);
        Intent intent= new Intent(this,EmpleadosRolesActivity.class);
        this.startActivity(intent);
        this.finish();
    }

}
