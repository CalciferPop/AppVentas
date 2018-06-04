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
import android.widget.Toast;

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
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class empleado_alta extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{



    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    @BindView(R.id.edtNombreEmpl)
    EditText edtNombEmpl;
    @BindView(R.id.edtApellidoPat)
    EditText edtApePatEmpl;
    @BindView(R.id.edtApellidoMat)
    EditText edtApeMatEmpl;
    @BindView(R.id.edtFecha)
    EditText edtFecNac;
    @BindView(R.id.edtemailEmp)
    EditText edtEmailEmpl;
    @BindView(R.id.edttelefonoEmp)
    EditText edtTelEmpl;
    @BindView(R.id.edtRFCEmpleado)
    EditText edtRFCEmpl;
    @BindView(R.id.rb_masculino)
    RadioButton rbMasculino;
    @BindView(R.id.rb_femenino)
    RadioButton rbFemenino;
    @BindView(R.id.btnAltaEmp)
    Button btnAltaEmpl;


    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    DatePickerDialog datePickerDialog;

    Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_alta);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
    }


    @OnClick({R.id.btnAltaEmp})
    public void OnClick(View v){
      if(m_valida()){
          if(m_validaEmail(edtEmailEmpl.getText().toString())){
              m_alta();
              KToast.customColorToast(empleado_alta.this, "Empleado Insertado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
              Intent intent= new Intent(this,EmpleadoActivity.class);
              this.startActivity(intent);
              finish();
          }else{
              KToast.customColorToast(empleado_alta.this, "El email proporcionado no es valido", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
          }

      }else{
          KToast.customColorToast(empleado_alta.this, "*NO se puede insertar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
      }
    }

    private boolean m_valida() {
        if(edtNombEmpl.length()==0 && edtApePatEmpl.length()==0 && edtApeMatEmpl.length()==0
                && edtFecNac.length() == 0 && edtEmailEmpl.length()==0 && edtTelEmpl.length()==0 && edtRFCEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*NO se proporciono ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        else if (edtNombEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Nombre empleado", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApePatEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Apellido Paterno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApeMatEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Apellido Materno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtFecNac.length() == 0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Fecha Nacimiento", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtEmailEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Email", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtTelEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtRFCEmpl.length()==0){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: RFC", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(!rbFemenino.isChecked() && !rbMasculino.isChecked()){
            KToast.customColorToast(empleado_alta.this, "*Valor Requerido: Gener", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        return  true;
    }

    @OnClick({R.id.edtFecha})
    public void OnClick1(View v){
        String fecha="";

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        datePickerDialog = new DatePickerDialog(empleado_alta.this,
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


    private void m_alta() {
        int id_genero=0;

        String nombre_empleado = edtNombEmpl.getText().toString();
        String apellido_paterno = edtApePatEmpl.getText().toString();
        String apellido_materno = edtApeMatEmpl.getText().toString();
        String fecha_nacimiento = edtFecNac.getText().toString();

        System.out.println("FECHA NACI" + fecha_nacimiento.toString());
        String email_empleado = edtEmailEmpl.getText().toString();
        String telefono = edtTelEmpl.getText().toString();
        String rfc = edtRFCEmpl.getText().toString();
        if (rbMasculino.isChecked()){
            id_genero=1;
        }else if (rbFemenino.isChecked()){
            id_genero=2;
        }

        String URL = IP_ADDRES+"empleado/insertempleado/";
        URL+=Ajuste.token;
        Date fec_nac = Date.valueOf(fecha_nacimiento);


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

    public boolean m_validaEmail(String p_email ){
        Matcher mather = pattern.matcher(p_email);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("volley_error", error.toString());
    }

    @Override
    public void onResponse(String response) {

       

    }
}
