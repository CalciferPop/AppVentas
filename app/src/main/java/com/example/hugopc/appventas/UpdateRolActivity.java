package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onurkaganaldemir.ktoastlib.KToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateRolActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EndPoint = IP_ADDRES + "rol/listroles/";
    private static String URL_UPD_Rol = IP_ADDRES + "rol/updaterol/";

    @BindView(R.id.edtUPDIDRol)
    EditText edtIDRol;
    @BindView(R.id.edtUPDNombreRol)
    EditText edtNombreRol;

    @BindView(R.id.btnUpdateRol)
    Button btn_UPDRol;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_rol = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rol);
        ButterKnife.bind(this);
        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_rol");
        id_rol = Integer.valueOf(id);
        edtIDRol.setText(id);
        System.out.println("ID ROL PAPA" + " " + id);
        requestQueue = Volley.newRequestQueue(this);
        m_obtenDatos(id_rol);
    }


    private void m_obtenDatos(int id_rol) {

        String URL_CARGA = URL_EndPoint + id_rol + "/" + Ajuste.token;
        System.out.println("END POINT" + URL_CARGA);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,URL_CARGA,null,this,null){
            @Override
            public Map<String, String> getHeaders() {
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


    private boolean m_valida() {

        if(edtNombreRol.length()==0 ){
            KToast.customColorToast(UpdateRolActivity.this, "*NO se proporciono ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        return true;
    }


    @OnClick({R.id.btnUpdateRol})
    public void OnClick4(View v){

        if(m_valida()){
            m_actualiza();
            KToast.customColorToast(UpdateRolActivity.this, "Rol Actualizado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent= new Intent(this,ListaRolesActivity.class);
            this.startActivity(intent);
            finish();
        }else{
            KToast.customColorToast(UpdateRolActivity.this, "NO se pudo actualizar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
        }
    }

    private void m_actualiza() {

        String nombre_ROL = edtNombreRol.getText().toString();


        String URL_ = URL_UPD_Rol+Ajuste.token;
        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_rol",id_rol);
            jsonObject.put("nombre_rol",nombre_ROL);

        }catch (Exception e){
            e.getMessage();
            System.out.println("ERROR" + e.getMessage());
        }

        stringRequest = new StringRequest(Request.Method.PUT, URL_, new Response.Listener<String>() {
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



    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            System.out.println(response.get("rol"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject1 = (JSONObject) response.get("rol");
            edtNombreRol.setText(jsonObject1.getString("nombre_rol"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
