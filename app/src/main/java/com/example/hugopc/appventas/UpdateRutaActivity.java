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

public class UpdateRutaActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{


    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EndPoint = IP_ADDRES + "ruta/listrutas/";
    private static String URL_UPD_RUTA = IP_ADDRES + "ruta/updateruta/";

    @BindView(R.id.edtUPDIDRuta)
    EditText edtIDRuta;
    @BindView(R.id.edtUPDDescRuta)
    EditText edtDEscRuta;

    @BindView(R.id.btnUPDRuta)
    Button btn_UPDRuta;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_ruta = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ruta);
        ButterKnife.bind(this);
        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_ruta");
        id_ruta = Integer.valueOf(id);
        edtIDRuta.setText(id);
        requestQueue = Volley.newRequestQueue(this);
        m_obtenDatos(id_ruta);
    }


    private void m_obtenDatos(int id_ruta) {

        String URL_CARGA = URL_EndPoint + id_ruta + "/" + Ajuste.token;
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

        if(edtDEscRuta.length()==0 ){
            KToast.customColorToast(UpdateRutaActivity.this, "*NO se proporciono ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        return true;
    }


    @OnClick({R.id.btnUPDRuta})
    public void OnClick4(View v){

        if(m_valida()){
            m_actualiza();
            KToast.customColorToast(UpdateRutaActivity.this, "Ruta Actualizada", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent= new Intent(this,ListaRutasActivity.class);
            this.startActivity(intent);
            finish();
        }else{
            KToast.customColorToast(UpdateRutaActivity.this, "NO se pudo actualizar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
        }
    }

    private void m_actualiza() {

        String desc_Ruta = edtDEscRuta.getText().toString();


        String URL_ = URL_UPD_RUTA+Ajuste.token;
        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_ruta",id_ruta);
            jsonObject.put("descripcion",desc_Ruta);

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
            System.out.println(response.get("ruta"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject1 = (JSONObject) response.get("ruta");
            edtDEscRuta.setText(jsonObject1.getString("descripcion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
