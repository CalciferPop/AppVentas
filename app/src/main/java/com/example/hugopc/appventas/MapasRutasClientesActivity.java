package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapasRutasClientesActivity extends FragmentActivity implements OnMapReadyCallback,Response.Listener<String>, Response.ErrorListener {

    private GoogleMap mMap;
    private double latitud,longitud;
    Marker marca;
    private Marker[] marcas;
    RequestQueue requestQueue;
    int id_ruta = 0;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_rutasClientes = IP_ADDRES + "rutas_clientes2/listrutasclientes2/";



    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas_rutas_clientes);
        requestQueue= Volley.newRequestQueue(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_ruta");
        id_ruta = Integer.valueOf(id);
        m_cargaRutasCliente(id_ruta);

    }

    private void m_cargaRutasCliente(int p_IDRuta) {

        String url_rc = URL_rutasClientes+p_IDRuta+"/"+Ajuste.token;
        StringRequest request = new StringRequest(Request.Method.GET,url_rc,this,this){
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
        requestQueue.add(request);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.itmNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

            case R.id.itmSatelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.itmHibrida:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.itmRelieve:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

        RutasClientes objRutaCliente;
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("rutas_clientes2");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                objRutaCliente = new RutasClientes();
                objRutaCliente.setNombre_cliente(jsonObject1.getString("nombre_cliente"));
                objRutaCliente.setDescripcion(jsonObject1.getString("descripcion"));
                objRutaCliente.setLatitud(jsonObject1.getString("latitud"));
                objRutaCliente.setLongitud(jsonObject1.getString("longitud"));

                String titulo = objRutaCliente.getNombre_cliente().toString();
                String desc = objRutaCliente.getDescripcion().toString();
                latitud = Double.parseDouble(objRutaCliente.getLatitud().toString());
                longitud = Double.parseDouble(objRutaCliente.getLongitud().toString());


                LatLng ubicacion_cliente = new LatLng(latitud,longitud);
                // Add a marker in Sydney and move the camera
                //LatLng sydney = new LatLng(20.5108635, -100.8333272);
                mMap.addMarker(new MarkerOptions()
                        .position(ubicacion_cliente)
                        .title(titulo)
                        .snippet(desc)

                );

                mMap.setMaxZoomPreference(50);

            }

            LatLng celaya = new LatLng(20.520404,-100.8204688);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(celaya));


        }catch (JSONException e){

            e.printStackTrace();
        }

    }
}
