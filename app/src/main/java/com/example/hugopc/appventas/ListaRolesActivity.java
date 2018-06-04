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

public class ListaRolesActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {


    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_roles = IP_ADDRES + "rol/listroles/"+Ajuste.token;


    List<Rol> rolList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_roles);
        rolList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerViewRoles);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        m_cargaRoles();
        adapter = new RolAdapter(rolList,this);
        recyclerView.setAdapter(adapter);
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
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

        Rol objRol;
        if (!response.isEmpty()){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("rol");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objRol = new Rol();
                objRol.setNombre_rol(jsonObject1.getString("nombre_rol"));
                objRol.setId_rol(jsonObject1.getInt("id_rol"));

                rolList.add(objRol);
            }
            adapter = new RolAdapter(rolList,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){

            e.printStackTrace();
        }
        }else{
            KToast.customColorToast(ListaRolesActivity.this, "SESION EXPIRADA", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_LONG, R.color.warning, R.drawable.ic_warning_black_24dp);
            Intent intent= new Intent(ListaRolesActivity.this,LoginActivity.class);
            ListaRolesActivity.this.startActivity(intent);
            this.finish();
        }

    }
}
