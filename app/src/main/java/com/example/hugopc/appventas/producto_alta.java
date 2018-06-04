package com.example.hugopc.appventas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

public class producto_alta extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    @BindView(R.id.edtNombreProd)
    EditText edtNombProd;
    @BindView(R.id.edtDescProd)
    EditText edtDescProd;
    @BindView(R.id.edtPrecio)
    EditText edtPrecio;
    @BindView(R.id.edtNumExistencia)
    EditText edtNumExistencia;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_alta);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    @OnClick({R.id.btnAltaProd})
    public void OnClick(View v){
        m_alta();
    }

    private void m_alta() {

        String nombre_PROD = edtNombProd.getText().toString();
        String descripcion = edtDescProd.getText().toString();
        Double precio = Double.valueOf(edtPrecio.getText().toString());
        int num_existencia = Integer.valueOf(edtNumExistencia.getText().toString());

        String URL = IP_ADDRES+"producto/insertproducto/";
        URL+=Ajuste.token;

        jsonObject = new JSONObject();

        try{

            jsonObject.put("nombre_producto",nombre_PROD);
            jsonObject.put("descripcion",descripcion);
            jsonObject.put("precio",precio);
            jsonObject.put("num_existencia",num_existencia);

        }catch (Exception e){
            e.getMessage();
        }
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
        KToast.customColorToast(producto_alta.this, "Producto Insertado", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        android.content.Intent intent= new android.content.Intent(this,ListaProductosActivity.class);
            this.startActivity(intent);
            finish();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("volley_error", error.toString());
    }

    @Override
    public void onResponse(String response) {

    }
}
