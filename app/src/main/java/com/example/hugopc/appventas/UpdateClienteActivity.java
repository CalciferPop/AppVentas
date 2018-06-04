package com.example.hugopc.appventas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.volley.Request.*;

public class UpdateClienteActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EndPoint = IP_ADDRES + "cliente/listclientes/";
    private static String URL_UPD_Clie = IP_ADDRES + "cliente/updatecliente/";

    LocationManager locationManager;

    @BindView(R.id.edtUPD_IDCliente)
    EditText edtIDCli;
    @BindView(R.id.edtUPDNombreClie)
    EditText edtNombCli;
    @BindView(R.id.edtUPDApellidoPatCli)
    EditText edtApePatCli;
    @BindView(R.id.edtUPDApellidoMatCli)
    EditText edtApeMatCli;
    @BindView(R.id.edtUPDFechaNacCli)
    EditText edtFecNacCli;
    @BindView(R.id.edtUPDDomicilioCli)
    EditText edtDomCli;
    @BindView(R.id.edtUPDtelefonoCli)
    EditText edtTelCli;
    @BindView(R.id.edtUPDemailCli)
    EditText edtEmailCli;
    @BindView(R.id.edtUPDLatitud)
    EditText edtLatCli;
    @BindView(R.id.edtUPDLongitud)
    EditText edtLongCli;
    @BindView(R.id.btnUPDGetPosicion)
    Button btnGetPosicion;
    @BindView(R.id.btnUpdateCli)
    Button btnUpdateCli;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    DatePickerDialog datePickerDialog;

    int id_cliente = 0;

    Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cliente);
        ButterKnife.bind(this);

        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_cliente");
        id_cliente = Integer.valueOf(id);
        edtIDCli.setText(id);

        requestQueue = Volley.newRequestQueue(this);
        m_obtenDatos(id_cliente);


    }

    public boolean m_validaEmail(String p_email ){
        Matcher mather = pattern.matcher(p_email);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }


    private void m_obtenDatos(int id_cliente) {

        String URL_CARGA = URL_EndPoint + id_cliente + "/" + Ajuste.token;
        System.out.println("END POINT" + URL_CARGA);


        JsonObjectRequest request = new JsonObjectRequest(Method.GET,URL_CARGA,null,this,null){
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

    @OnClick({R.id.btnUPDGetPosicion})
    public void OnClick3(View v){
        m_getPos();
    }

    @OnClick({R.id.edtUPDFechaNacCli})
    public void OnClick(View v) {
        String fecha = "";

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        datePickerDialog = new DatePickerDialog(UpdateClienteActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        // edtFecNac.setText(
                        //       dayOfMonth + "/"
                        //     + (monthOfYear + 1) + "/" + year);
                        edtFecNacCli.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.btnUpdateCli})
    public void OnClick4(View v){

        if(m_validaDatos()){
            if(m_validaEmail(edtEmailCli.getText().toString())){
                m_actualiza();
            }else {
                KToast.customColorToast(UpdateClienteActivity.this, "El email proporcionado no es valido", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            }
        }else{
            KToast.customColorToast(UpdateClienteActivity.this, "NO se pudo actualizar", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
        }



    }

    private boolean m_validaDatos() {

        if(edtLongCli.length()==0 && edtLatCli.length()==0 && edtEmailCli.length()==0
                && edtDomCli.length()==0 && edtTelCli.length()==0 && edtFecNacCli.length()==0
                && edtApePatCli.length()==0 && edtApeMatCli.length() == 0 &&edtNombCli.length()==0) {
            KToast.customColorToast(UpdateClienteActivity.this, "*NO has proporcionado ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        else if (edtNombCli.length() == 0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Nombre Cliente", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApePatCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Apellido Paterno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApeMatCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Apellido Materno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtFecNacCli.length() == 0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Fecha Nacimiento", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtDomCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Domicilio", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtTelCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        } else  if(edtTelCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        } else if(edtEmailCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Email", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }  else if(edtLatCli.length()==0){
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Latitud", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtLongCli.length()==0) {
            KToast.customColorToast(UpdateClienteActivity.this, "*Dato Requerido: Longitud", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        else{
            return true;
        }
    }

    private void m_actualiza() {
        String nombre_cliente = edtNombCli.getText().toString();
        String apellido_paterno = edtApePatCli.getText().toString();
        String apellido_materno = edtApeMatCli.getText().toString();
        String fecha_nacimiento = edtFecNacCli.getText().toString();
        String domicilio = edtDomCli.getText().toString();
        String telefono = edtTelCli.getText().toString();
        String email_cliente = edtEmailCli.getText().toString();
        String latitud = edtLatCli.getText().toString();
        String longitud = edtLongCli.getText().toString();

        String URL_ = URL_UPD_Clie+Ajuste.token;
        Date fec_nac = Date.valueOf(fecha_nacimiento);

        jsonObject = new JSONObject();

        try{
            jsonObject.put("id_cliente",id_cliente);
            jsonObject.put("nombre_cliente",nombre_cliente);
            jsonObject.put("apellido_paterno",apellido_paterno);
            jsonObject.put("apellido_materno",apellido_materno);
            jsonObject.put("fecha_nacimiento",fec_nac);
            jsonObject.put("domicilio",domicilio);
            jsonObject.put("telefono",telefono);
            jsonObject.put("email",email_cliente);
            jsonObject.put("latitud",latitud);
            jsonObject.put("longitud",longitud);

        }catch (Exception e){
            e.getMessage();
        }

        stringRequest = new StringRequest(Method.PUT, URL_, new Response.Listener<String>() {
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
        KToast.customColorToast(UpdateClienteActivity.this, "Cliente Actualizado", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(this,ListaClientesActivity.class);
        this.startActivity(intent);
        finish();

    }



    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        ////System.out.println(response);
        try {
            System.out.println(response.get("cliente"));
            JSONObject jsonObject1 = (JSONObject) response.get("cliente");

            edtNombCli.setText(jsonObject1.getString("nombre_cliente"));
            edtApePatCli.setText(jsonObject1.getString("apellido_paterno"));
            edtApeMatCli.setText(jsonObject1.getString("apellido_materno"));

            String Fecha_largar = jsonObject1.getString("fecha_nacimiento");
            String fecha_corta = Fecha_largar.substring(0,9);
            edtFecNacCli.setText(fecha_corta);
            edtDomCli.setText(jsonObject1.getString("domicilio"));
            edtTelCli.setText(jsonObject1.getString("telefono"));
            edtEmailCli.setText(jsonObject1.getString("email"));
            edtLatCli.setText(jsonObject1.getString("latitud"));
            edtLongCli.setText(jsonObject1.getString("longitud"));




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void m_getPos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        UpdateClienteActivity.Localizacion Local = new UpdateClienteActivity.Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        //coordenadas.setText("Localización agregada");
        //direccion.setText("");
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    edtDomCli.setText(DirCalle.getAddressLine(0).toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private boolean isLocationEnabled() {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }

        private void showAlert() {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Enable Location")
                    .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                            "usa esta app")
                    .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        }

        private boolean checkLocation() {
            if (!isLocationEnabled())
                showAlert();
            return isLocationEnabled();
        }

        public class Localizacion implements LocationListener {

            UpdateClienteActivity mainActivity;

            public UpdateClienteActivity getMainActivity() {
                return mainActivity;
            }

            public void setMainActivity(UpdateClienteActivity mainActivity) {
                this.mainActivity = mainActivity;
            }

            @Override
            public void onLocationChanged(Location loc) {
                // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
                // debido a la deteccion de un cambio de ubicacion

                loc.getLatitude();
                loc.getLongitude();

                String Text = "Mi ubicacion actual es: " + "\n Lat = "
                        + loc.getLatitude() + "\n Long = " + loc.getLongitude();
                edtLatCli.setText(loc.getLatitude()+ "");
                edtLongCli.setText(loc.getLongitude()+"");
                // coordenadas.setText(Text);
                this.mainActivity.setLocation(loc);
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Este metodo se ejecuta cuando el GPS es desactivado
                // coordenadas.setText("GPS Desactivado");
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Este metodo se ejecuta cuando el GPS es activado
                //coordenadas.setText("GPS Activado");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    case LocationProvider.AVAILABLE:
                        Log.d("debug", "LocationProvider.AVAILABLE");
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                        break;
                }
            }
        }


    }


