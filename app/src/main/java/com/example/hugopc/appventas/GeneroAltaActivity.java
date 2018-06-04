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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onurkaganaldemir.ktoastlib.KToast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneroAltaActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    @BindView(R.id.edtNombreGen)
    EditText edtNombGenero;

    @BindView(R.id.btnAltaGenero)
    Button btnAltaGenero;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero_alta);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    @OnClick({R.id.btnAltaGenero})
    public void OnClick(View v){
        if(m_valida()){
            m_alta();
            KToast.customColorToast(GeneroAltaActivity.this, "Genero Agregado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent= new Intent(this,ListaGenerosActivity.class);
            this.startActivity(intent);
            finish();
        }else{
            KToast.customColorToast(GeneroAltaActivity.this, "*Requerido: No se pudo insertar ", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);

        }
    }

    private boolean m_valida() {
        if (edtNombGenero.length()== 0 ){
            KToast.customColorToast(GeneroAltaActivity.this, "*Requerido: Nombre Genero ", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        return true;
    }

    private void m_alta() {
        String desc = edtNombGenero.getText().toString();
        String URL = IP_ADDRES+"genero/insertgenero/";
        URL+=Ajuste.token;

        jsonObject = new JSONObject();

        try{
            jsonObject.put("descripcion",desc);
        }catch (Exception e){
            e.getMessage();
        }
        System.out.println(jsonObject);
        stringRequest = new StringRequest(Request.Method.POST, URL, this, this)
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
    public void onResponse(String response) {

    }
}
