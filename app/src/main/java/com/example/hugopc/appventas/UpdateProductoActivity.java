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
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateProductoActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EndPoint = IP_ADDRES + "producto/listproductos/";
    private static String URL_UPD_Producto = IP_ADDRES + "producto/updateproducto/";


    @BindView(R.id.edtUPDidProd)
    EditText edtIDProd;
    @BindView(R.id.edtUPDNombreProd)
    EditText edtNombreProdu;
    @BindView(R.id.edtUPDDescProd)
    EditText edtDescripProd;
    @BindView(R.id.edtUPDPrecio)
    EditText edtPrecioProd;
    @BindView(R.id.edtUPDNumExistencia)
    EditText edtNumExisteProdUPD;
    @BindView(R.id.btnUPDProd)
    Button btn_UPDProd;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    int id_producto = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_producto);
        ButterKnife.bind(this);
        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_producto");
        id_producto = Integer.valueOf(id);
        edtIDProd.setText(id);
        System.out.println("ID PRODUCTO PAPA" + " " + id);
        requestQueue = Volley.newRequestQueue(this);
        m_obtenDatos(id_producto);
    }

    private void m_obtenDatos(int id_producto) {

        String URL_CARGA = URL_EndPoint + id_producto + "/" + Ajuste.token;
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


    @OnClick({R.id.btnUPDProd})
    public void OnClick4(View v){

        if(m_valida()){
            m_actualiza();
            KToast.customColorToast(UpdateProductoActivity.this, "Producto Actualizado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent= new Intent(this,ListaProductosActivity.class);
            this.startActivity(intent);
            finish();
        }else{
            KToast.customColorToast(UpdateProductoActivity.this, "NO se puedo actualizar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);

        }
    }

    private boolean m_valida() {

        if(edtNombreProdu.length()==0 && edtDescripProd.length() == 0 && edtDescripProd.length() == 0 && edtPrecioProd.length() == 0){
            KToast.customColorToast(UpdateProductoActivity.this, "*NO se proporciono ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        } else if(edtNombreProdu.length()==0){
            KToast.customColorToast(UpdateProductoActivity.this, "Requerido: Nombre Producto", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtDescripProd.length() == 0){
            KToast.customColorToast(UpdateProductoActivity.this, "*Requerido: Descripcion Producto", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtPrecioProd.length() == 0){
            KToast.customColorToast(UpdateProductoActivity.this, "*Requerido: Precio Producto", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtNumExisteProdUPD.length() == 0){
            KToast.customColorToast(UpdateProductoActivity.this, "*Requerido: Existencia Prod", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        return true;
    }

    private void m_actualiza() {

        String nombre_producto = edtNombreProdu.getText().toString();
        String desc_prod = edtDescripProd.getText().toString();
        double precio_prod = Double.valueOf(edtPrecioProd.getText().toString());
        int num_exist_prod = Integer.valueOf(edtNumExisteProdUPD.getText().toString());

        String URL_ = URL_UPD_Producto+Ajuste.token;
        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_producto",id_producto);
            jsonObject.put("nombre_producto",nombre_producto);
            jsonObject.put("descripcion",desc_prod);
            jsonObject.put("precio",precio_prod);
            jsonObject.put("num_existencia",num_exist_prod);

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
            System.out.println(response.get("producto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject1 = (JSONObject) response.get("producto");

            edtNombreProdu.setText(jsonObject1.getString("nombre_producto"));
            edtDescripProd.setText(jsonObject1.getString("descripcion"));
            edtPrecioProd.setText(jsonObject1.getDouble("precio")+"");
            edtNumExisteProdUPD.setText(jsonObject1.getInt("num_existencia") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
