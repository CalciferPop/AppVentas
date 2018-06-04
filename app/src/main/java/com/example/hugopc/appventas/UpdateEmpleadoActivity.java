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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateEmpleadoActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {


    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EndPoint = IP_ADDRES + "empleado/listempleados/";
    private static String URL_UPD_Empleado = IP_ADDRES + "empleado/updateempleado/";

    @BindView(R.id.edtUPDID_empleado)
    EditText edtIDEmple;
    @BindView(R.id.edtUPDNombreEmpl)
    EditText edtNombEmpl;
    @BindView(R.id.edtUPDApellidoPat)
    EditText edtApePat;
    @BindView(R.id.edtUPDApellidoMatEmpl)
    EditText edtApeMat;
    @BindView(R.id.edtUPDFechaEmp)
    EditText edtFecNacEmpl;
    @BindView(R.id.edtUPDemailEmp)
    EditText edtEmailEmpl;
    @BindView(R.id.edtUPDtelefonoEmp)
    EditText edtTelefonoEmpl;
    @BindView(R.id.edtUPDRFCEmpleado)
    EditText edtRFCEmpl;
    @BindView(R.id.rb_UPDmasculino)
    RadioButton rb_UPDmasculino;
    @BindView(R.id.rb_UPDfemenino)
    RadioButton rb_UPDFemenino;
    @BindView(R.id.btnUpdateEmp)
    Button btn_UPDEmpl;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    DatePickerDialog datePickerDialog;
    int id_empleado = 0;

    Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_empleado);
        ButterKnife.bind(this);
        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_empleado");
        id_empleado = Integer.valueOf(id);
        edtIDEmple.setText(id);
        requestQueue = Volley.newRequestQueue(this);
        System.out.println("ID PRODUCTO PAPA" + " " + id_empleado);
        m_obtenDatos(id_empleado);
    }

    private void m_obtenDatos(int id_empleado) {

        String URL_CARGA = URL_EndPoint + id_empleado + "/" + Ajuste.token;
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

    @OnClick({R.id.edtUPDFechaEmp})
    public void OnClick(View v) {
        String fecha = "";

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        datePickerDialog = new DatePickerDialog(UpdateEmpleadoActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        edtFecNacEmpl.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.btnUpdateEmp})
    public void OnClick4(View v){

        if(m_valida()){
            if (m_validaEmail(edtEmailEmpl.getText().toString())){
                m_actualiza();
                KToast.customColorToast(UpdateEmpleadoActivity.this, "Empleado Actualizado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
                Intent intent= new Intent(this,listEmpleado.class);
                this.startActivity(intent);
                finish();
            }else{
                KToast.customColorToast(UpdateEmpleadoActivity.this, "El email proporcionado no es valido", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            }

        }else{
            KToast.customColorToast(UpdateEmpleadoActivity.this, "NO se puedo actualizar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);

        }
    }


    public boolean m_validaEmail(String p_email ){
        Matcher mather = pattern.matcher(p_email);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }



    private boolean m_valida() {
        if(edtNombEmpl.length()==0 && edtNombEmpl.length() == 0 && edtApePat.length() == 0 && edtApeMat.length() == 0 &&
        edtFecNacEmpl.length() == 0 && edtEmailEmpl.length() == 0 && edtTelefonoEmpl.length() == 0 && edtRFCEmpl.length() == 0) {
            KToast.customColorToast(UpdateEmpleadoActivity.this, "NO proporciono ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        } else if(edtNombEmpl.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Nombre Empleado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApePat.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Apellido Paterno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApeMat.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Apellido Materno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtFecNacEmpl.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Fecha Nacimiento", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtEmailEmpl.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Email", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtTelefonoEmpl.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtRFCEmpl.length() == 0){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: RFC", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(!rb_UPDFemenino.isChecked() && !rb_UPDmasculino.isChecked()){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "*Requerido: Genero", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        return true;
    }

    private void m_actualiza() {
        int id_genero=0;
        String nombre_empleado = edtNombEmpl.getText().toString();
        String ape_paterno = edtApePat.getText().toString();
        String ape_materno = edtApeMat.getText().toString();
        String fecha_nacimiento = edtFecNacEmpl.getText().toString();
        String email_emp = edtEmailEmpl.getText().toString();
        String telefono = edtTelefonoEmpl.getText().toString();
        String RFC = edtRFCEmpl.getText().toString();
        String URL_ = URL_UPD_Empleado+Ajuste.token;
        Date fec_nac = Date.valueOf(fecha_nacimiento);
        if (rb_UPDmasculino.isChecked()){
            id_genero=1;
        }else if(rb_UPDFemenino.isChecked()){
            id_genero=2;
        }
        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_empleado",id_empleado);
            jsonObject.put("nombre_empleado",nombre_empleado);
            jsonObject.put("apellido_paterno",ape_paterno);
            jsonObject.put("apellido_materno",ape_materno);
            jsonObject.put("fecha_nacimiento",fec_nac);
            jsonObject.put("email_empleado",email_emp);
            jsonObject.put("telefono",telefono);
            jsonObject.put("rfc",RFC);
            jsonObject.put("id_genero",id_genero);

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
            System.out.println(response.get("empleado"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject1 = (JSONObject) response.get("empleado");

            edtNombEmpl.setText(jsonObject1.getString("nombre_empleado"));
            edtApePat.setText(jsonObject1.getString("apellido_paterno"));
            edtApeMat.setText(jsonObject1.getString("apellido_materno"));

            String Fecha_largar = jsonObject1.getString("fecha_nacimiento");
            String fecha_corta = Fecha_largar.substring(0,9);
            edtFecNacEmpl.setText(fecha_corta);
            edtEmailEmpl.setText(jsonObject1.getString("email_empleado"));
            edtTelefonoEmpl.setText(jsonObject1.getString("telefono"));
            edtRFCEmpl.setText(jsonObject1.getString("rfc"));
            int id_genero = jsonObject1.getInt("id_genero");
            m_cargaGenero(id_genero);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void m_cargaGenero(int id_genero) {

        if (id_genero == 1){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "Genero Masculino" + id_genero, Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);


            rb_UPDmasculino.setActivated(true);
        }else if(id_genero == 2){
            KToast.customColorToast(UpdateEmpleadoActivity.this, "Genero FEMENICO" + id_genero, Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

           rb_UPDFemenino.setActivated(false);
        }
    }
}
