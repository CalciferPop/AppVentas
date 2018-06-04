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
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
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

public class cliente_alta extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;
    @BindView(R.id.edtNombreClie)
    EditText edtNombCli;
    @BindView(R.id.edtApellidoPatCli)
    EditText edtApepatCli;
    @BindView(R.id.edtApellidoMatCli)
    EditText edtApematCli;
    @BindView(R.id.edtFechaNacCli)
    EditText edtFecNacCli;
    @BindView(R.id.edtDomicilioCli)
    EditText edtDomCli;
    @BindView(R.id.edttelefonoCli)
    EditText edtTelCli;
    @BindView(R.id.edtemailCli)
    EditText edtEmailCli;
    @BindView(R.id.edtLatitud)
    EditText edtLatitud;
    @BindView(R.id.edtLongitud)
    EditText edtLongitud;
    @BindView(R.id.btnAltaCli)
    Button btnAltaCLI;
    @BindView(R.id.btnGetPosicion)
    Button btnAGetPosicion;

    DatePickerDialog datePickerDialog;

    LocationManager locationManager;
    double longitudGPS, latitudGPS;

    Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_alta);
        ButterKnife.bind(this);


        requestQueue = Volley.newRequestQueue(this);
    }


    @OnClick({R.id.btnGetPosicion})
    public void OnClick3(View v){
        m_getPos();
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
        Localizacion Local = new Localizacion();
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



    @OnClick({R.id.btnAltaCli})
    public void OnClick(View v){

        //edtDomCli.setText("Antonio Garcia Cubas S/n");
        m_alta();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void m_alta() {

        if(m_validaDatos()){
            if (m_validaEmail(edtEmailCli.getText().toString())){
                String nombre_cliente = edtNombCli.getText().toString();
                String apellido_paterno = edtApepatCli.getText().toString();
                String apellido_materno = edtApematCli.getText().toString();
                String fecha_nacimiento = edtFecNacCli.getText().toString();
                String domicilio = edtDomCli.getText().toString();
                String telefono = edtTelCli.getText().toString();
                String email_cliente = edtEmailCli.getText().toString();
                String latitud = edtLatitud.getText().toString();
                String longitud = edtLongitud.getText().toString();


                String URL = IP_ADDRES+"cliente/insertcliente/";
                URL+=Ajuste.token;
                Date fec_nac = Date.valueOf(fecha_nacimiento);


                jsonObject = new JSONObject();

                try{

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
                KToast.customColorToast(cliente_alta.this, "Cliente Insertado", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_warning_black_24dp);
                Intent intent= new Intent(this,ClienteActivity.class);
                this.startActivity(intent);
                finish();
            }else{
                KToast.customColorToast(cliente_alta.this, "El email proporcionado no es valido", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            }

        }else{
            KToast.customColorToast(cliente_alta.this, "NO se pudo insertar", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);

        }

    }

    private boolean m_validaDatos() {

        if(edtLongitud.length()==0 && edtLatitud.length()==0 && edtEmailCli.length()==0
                && edtDomCli.length()==0 && edtTelCli.length()==0 && edtFecNacCli.length()==0
                && edtApepatCli.length()==0 && edtApematCli.length() == 0 &&edtNombCli.length()==0) {
            KToast.customColorToast(cliente_alta.this, "*NO has proporcionado ningun dato", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        else if (edtNombCli.length() == 0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Nombre Cliente", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApepatCli.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Apellido Paterno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtApematCli.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Apellido Materno", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtFecNacCli.length() == 0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Fecha Nacimiento", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtDomCli.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Domicilio", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtTelCli.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        } else  if(edtTelCli.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Telefono", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        } else if(edtEmailCli.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Email", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }  else if(edtLatitud.length()==0){
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Latitud", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }else if(edtLongitud.length()==0) {
            KToast.customColorToast(cliente_alta.this, "*Dato Requerido: Longitud", Gravity.CENTER_HORIZONTAL, KToast.LENGTH_AUTO, R.color.warning, R.drawable.ic_warning_black_24dp);
            return false;
        }
        else{
            return true;
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



    @OnClick({R.id.edtFechaNacCli})
    public void OnClick1(View v){
        String fecha="";

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        datePickerDialog = new DatePickerDialog(cliente_alta.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        // edtFecNac.setText(
                        //       dayOfMonth + "/"
                        //     + (monthOfYear + 1) + "/" + year);
                        edtFecNacCli.setText(year + "-" + monthOfYear +"-" +dayOfMonth );

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.btnGetPosicion})
    public void OnClick2(View v){

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




    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }
    public class Localizacion implements LocationListener {

        cliente_alta mainActivity;

        public cliente_alta getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(cliente_alta mainActivity) {
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
            edtLatitud.setText(loc.getLatitude()+ "");
            edtLongitud.setText(loc.getLongitude()+"");
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
