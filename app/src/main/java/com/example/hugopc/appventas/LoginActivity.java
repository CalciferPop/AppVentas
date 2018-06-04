package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.edtusuario) EditText edtusuario;
    @BindView(R.id.edtpassword) EditText edtpassword;
    @BindView(R.id.chbsesion) CheckBox chbsesion;
    @BindView(R.id.btnlogIn) Button btn_login;
    @BindView(R.id.btnRegistro) Button btnRegistro;


    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    JsonObjectRequest JsonRequest;
    private JSONObject jsonObject;


    String token=null;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        KToast.customColorToast(LoginActivity.this, "NO esta conectado a ninguna red, revise su configuracion de red", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_info_outline_black_24dp);


        //Log.e("volley_error", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("volley_response", response);
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            String token = jsonObject.getString("token");
            Ajuste.token = token;
            //System.out.println("TOKEN: " + token);
            if (!token.equalsIgnoreCase("Acceso Denegado")) {

                m_obtenRol(edtusuario.getText().toString(),m_encriptar(edtpassword.getText().toString()));


                Thread timer2 = new Thread(){
                    public void run(){
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            Intent intent= new Intent(LoginActivity.this,InicioActivity.class);
                            LoginActivity.this.startActivity(intent);

                        }
                    }
                };
                timer2.start();
            }
            else{
                KToast.customColorToast(LoginActivity.this, "Acceso Denegado", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    }


    public void m_obtenRol(String p_nick,String p_pass){

        //System.out.println("ENTRO A OBTENER EL ID USUARIO");
        String URL_rol = IP_ADDRES+"usuario/obtenrol/"+p_nick+"/"+p_pass;
        System.out.println(URL_rol);

        JsonRequest = new JsonObjectRequest(Request.Method.GET, URL_rol, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Entro al response de traer el rol del usuario");
                System.out.println(response.toString());

                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    //System.out.println("OBJETO :" + response);
                    //System.out.println("TAMAÑO" + response.length());
                    JSONObject jsonObject1 = response.getJSONObject("rol");
                    //System.out.println(jsonObject1.getInt("id_usuario"));
                    //System.out.println(jsonObject1.getString("nickname"));
                    String rol = jsonObject1.getString("nombre_rol");
                    int id_ro = jsonObject1.getInt("id_rol");
                    Ajuste.rol = rol;
                    Ajuste.id_rol = id_ro;
                    //System.out.println("ID USUARIO" + id_user);
                    //System.out.println("NICKNAME:" + nick);



                } catch (JSONException e) {
                    e.getCause();
                    e.getMessage();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(JsonRequest);

    }


    @OnClick({R.id.btnlogIn})
    public void OnClick(View v){
            m_validar();
    }

    private void m_validar() {
        //edtusuario.setText("hugopc");
        //edtpassword.setText("hugopc");
        String user = edtusuario.getText().toString();
        String password = edtpassword.getText().toString();

        String URL = IP_ADDRES+"usuario/validar/"+user+"/"+m_encriptar(password);
       // String URL = "http://10.149.163.38:8080/Training/api/usr/validate/";



        stringRequest = new StringRequest(Request.Method.GET, URL, this, this){
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
        requestQueue.add(stringRequest);
    }









    public static String m_encriptar(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @OnClick({R.id.btnRegistro})
    public void OnClick10(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(LoginActivity.this,RegistroActivity.class);
        LoginActivity.this.startActivity(intent);
        edtusuario.setText("");
        edtpassword.setText("");
    }



    }

