package com.example.hugopc.appventas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onurkaganaldemir.ktoastlib.KToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistroActivity extends AppCompatActivity {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    @BindView(R.id.edtRegNombreEmpl)
    EditText edtNombEmpl;
    @BindView(R.id.edtRegApePatEmpl)
    EditText edtApePatEmpl;
    @BindView(R.id.edtRegApeMatEmpl)
    EditText edtApeMatEmpl;
    @BindView(R.id.edtRegFechaNacEmpl)
    EditText edtFecNac;
    @BindView(R.id.edtRegEmailEmpl)
    EditText edtEmailEmpl;
    @BindView(R.id.edtRegTelefonoEmpl)
    EditText edtTelEmpl;
    @BindView(R.id.edtRegRFCEmpl)
    EditText edtRFCEmpl;
    @BindView(R.id.edtRegusuario)
    EditText edtUsuario;
    @BindView(R.id.edtRegpassword)
    EditText edtpassword;
    @BindView(R.id.btnRegistrar)
    Button btnRegistrarEmpl;
    @BindView(R.id.rb_Regfemenino)
    RadioButton rbFemenino;
    @BindView(R.id.rb_Regmasculino)
    RadioButton rbMasculino;


    Spinner spineerGenero;

    Date fec_nac;

    private RequestQueue requestQueue,requestQueue2;
    private StringRequest stringRequest;
    JsonObjectRequest JsonRequest;
    private JSONObject jsonObject;

    DatePickerDialog datePickerDialog;

    String nickname="";
    String password="";
    int id_usuario;
    int id_rol = 2;

    ObtenID obtenID;

    Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);


    }


    private boolean m_valida() {
        if(edtNombEmpl.length()==0 && edtApePatEmpl.length()==0 && edtApeMatEmpl.length()==0
                && edtFecNac.length() == 0 && edtEmailEmpl.length()==0 && edtTelEmpl.length()==0 && edtRFCEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*NO se proporciono ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        else if (edtNombEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Nombre empleado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApePatEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Apellido Paterno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApeMatEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Apellido Materno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtFecNac.length() == 0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Fecha Nacimiento", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtEmailEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Email", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtTelEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtRFCEmpl.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: RFC", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtUsuario.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Usuario", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtpassword.length()==0){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Contraseña", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(!rbFemenino.isChecked() && !rbMasculino.isChecked()){
            KToast.customColorToast(RegistroActivity.this, "*Valor Requerido: Gener", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if (!m_validaEmail(edtEmailEmpl.getText().toString())){
            KToast.customColorToast(RegistroActivity.this, "El email proporcionado no es valido", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            this.recreate();
            return false;
        }
        return  true;
    }

    public boolean m_validaEmail(String p_email ){
        Matcher mather = pattern.matcher(p_email);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }


    @OnClick({R.id.edtRegFechaNacEmpl})
    public void OnClick1(View v){
        String fecha="";

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        datePickerDialog = new DatePickerDialog(RegistroActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        // edtFecNac.setText(
                        //       dayOfMonth + "/"
                        //     + (monthOfYear + 1) + "/" + year);
                        edtFecNac.setText(year + "-" + monthOfYear +"-" +dayOfMonth );

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void m_altaUsuario(){
        //System.out.println("ENTRE A HACER EL ALTA DEL USUARIO");
        nickname = edtUsuario.getText().toString();
        password = m_encriptar(edtpassword.getText().toString());
        String URL = IP_ADDRES+"usuario/insertusuario2";
        jsonObject = new JSONObject();
        try{

            jsonObject.put("nickname",nickname);
            jsonObject.put("contrasena",password);

        }catch (Exception e){
            ///System.out.println("ALTA DE USUARIO");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e.getCause());
        }
        //System.out.println(jsonObject);
        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                System.out.println(error.getStackTrace());
                System.out.println(error.getCause());
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
        m_traeID();


    }


    private void m_altaEmpleado(int p_idUsuario) {
        //System.out.println("ENTRO A DAR DE ALTA AL EMPLEADO");
        //System.out.println("SU ID USUARIO ES:" + p_idUsuario);
        int id_genero=0;

        String nombre_empleado = edtNombEmpl.getText().toString();
        String apellido_paterno = edtApePatEmpl.getText().toString();
        String apellido_materno = edtApeMatEmpl.getText().toString();
        String fecha_nacimiento = edtFecNac.getText().toString();

        //System.out.println("FECHA NACI" + fecha_nacimiento.toString());
        String email_empleado = edtEmailEmpl.getText().toString();
        String telefono = edtTelEmpl.getText().toString();
        String rfc = edtRFCEmpl.getText().toString();
        if (rbMasculino.isChecked()){
            id_genero=1;
        }else if (rbFemenino.isChecked()){
            id_genero=2;
        }

        String URL = IP_ADDRES+"empleado/insertempleado";
        System.out.println(URL);
        try{

            fec_nac = Date.valueOf(fecha_nacimiento);
        }catch (IllegalArgumentException e){

        }

            jsonObject = new JSONObject();

            try{

                jsonObject.put("nombre_empleado",nombre_empleado);
                jsonObject.put("apellido_paterno",apellido_paterno);
                jsonObject.put("apellido_materno",apellido_materno);
                jsonObject.put("fecha_nacimiento",fec_nac);
                jsonObject.put("email_empleado",email_empleado);
                jsonObject.put("telefono",telefono);
                jsonObject.put("RFC",rfc);
                jsonObject.put("id_genero",id_genero);
                jsonObject.put("id_usuario",p_idUsuario);

            }catch (Exception e){
                //System.out.println("ALTA EMPLEADO");
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                System.out.println(e.getCause());
            }
            //System.out.println(jsonObject);
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




        public void m_altaEmpleadoRol(int id_empleado,int id_rol){


            String URL = IP_ADDRES+"empleado_rol/insertempleado_rol";
            System.out.println(URL);

            jsonObject = new JSONObject();

            try{

                jsonObject.put("id_empleado",id_empleado);
                jsonObject.put("id_rol",id_rol);


            }catch (Exception e){
                //System.out.println("ALTA EMPLEADO");
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                System.out.println(e.getCause());
            }
            //System.out.println(jsonObject);
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
            requestQueue2.add(stringRequest);

        }





    @OnClick({R.id.btnRegistrar})
    public void OnClick2(View v){
        if(m_valida()) {
            m_altaUsuario();
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {

                        m_altaEmpleado(id_usuario);
                        m_obtenIdEmpleado(id_usuario);


                    }
                }
            };
            timer.start();









            KToast.customColorToast(RegistroActivity.this, "*Empleado Registrado, Login para continuar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
            this.startActivity(intent);
            finish();
            btnRegistrarEmpl.setEnabled(false);

        }else{
            KToast.customColorToast(RegistroActivity.this, "*No se pudo registrar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
        }
    }




    private void m_traeID() {
        obtenID = new ObtenID(this);
        obtenID.m_obtenID(nickname,password);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    id_usuario = Ajuste.id_usua;
                    //System.out.println("ID USUARIO" +id_usuario);
                }
            }
        };
        timer.start();
    }


    private void  m_obtenIdEmpleado(int id_usua){
        String URL_rol = IP_ADDRES+"empleado/getidempleado/"+id_usua;
        System.out.println(URL_rol);
        JsonRequest = new JsonObjectRequest(Request.Method.GET, URL_rol, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Entro al response de traer el id del empleado");
                System.out.println(response.toString());

                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    //System.out.println("OBJETO :" + response);
                    //System.out.println("TAMAÑO" + response.length());
                    JSONObject jsonObject1 = response.getJSONObject("empleado");
                    //System.out.println(jsonObject1.getInt("id_usuario"));
                    //System.out.println(jsonObject1.getString("nickname"));
                    int id_emp = jsonObject1.getInt("id_empleado");

                    Ajuste.id_empl = id_emp;
                    System.out.println("ID EMPLEADO" + Ajuste.id_empl);
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
        requestQueue2.add(JsonRequest);



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





}
