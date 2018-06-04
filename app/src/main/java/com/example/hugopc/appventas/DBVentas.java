package com.example.hugopc.appventas;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.view.Gravity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onurkaganaldemir.ktoastlib.KToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DBVentas extends SQLiteOpenHelper {


    private RequestQueue requestQueue;
    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_pedido = IP_ADDRES + "pedido2/listpedidos2/"+Ajuste.token;
    private static String URL_Usuarios = IP_ADDRES + "usuario/listusuarios/"+Ajuste.token;
    private static String URL_generos = IP_ADDRES + "genero/listgeneros/"+Ajuste.token;
    private static String URL_empl = IP_ADDRES + "empleado/listempleados/"+Ajuste.token;
    private static String URL_clientes = IP_ADDRES + "cliente/listclientes/"+Ajuste.token;
    private static String URL_productos = IP_ADDRES + "producto/listproductos/"+Ajuste.token;
    private static String URL_roles = IP_ADDRES + "rol/listroles/"+Ajuste.token;
    private static String URL_rutas = IP_ADDRES + "ruta/listrutas/"+Ajuste.token;


    String sql_usuario = "CREATE TABLE usuario(" +
            "  id_usuario INT , " +
            "  nickname VARCHAR(50) NOT NULL , " +
            "  contrasena VARCHAR(32) NOT NULL , " +
            "  token VARCHAR(100));";

    String sql_genero =  "CREATE TABLE genero(" +
            "  id_genero INT  , " +
            "  descripcion VARCHAR(250) " +
            ");";

    String sql_empleado = "CREATE TABLE empleado(" +
            "  id_empleado INT  , " +
            "  nombre_empleado VARCHAR(150) NOT NULL , " +
            "  apellido_paterno VARCHAR(150), " +
            "  apellido_materno VARCHAR(150), " +
            "  fecha_nacimiento DATE, " +
            "  email_empleado VARCHAR(80), " +
            "  telefono VARCHAR(60), " +
            "  RFC VARCHAR(13), " +
            "  id_genero INT  , " +
            "  id_usuario INT , " +
            "FOREIGN KEY (id_genero) REFERENCES genero (id_genero) , "+
            "FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)"+
            ")";


    String sql_cliente ="CREATE TABLE cliente(" +
            "  id_cliente INT  , " +
            "  nombre_cliente VARCHAR(150) NOT NULL, " +
            "  apellido_paterno VARCHAR(150), " +
            "  apellido_materno VARCHAR(150), " +
            "  fecha_nacimiento DATE, " +
            "  domicilio VARCHAR(150), " +
            "  telefono VARCHAR(100), " +
            "  email VARCHAR(100), " +
            "  latitud VARCHAR(250), " +
            "  longitud VARCHAR(250) " +
            ");";

    String sql_producto = "CREATE TABLE producto(" +
            "  id_producto INT , " +
            "  nombre_producto VARCHAR(150), " +
            "  descripcion VARCHAR(250), " +
            "  precio NUMERIC(10,2), " +
            "  num_existencia INT " +
            ");";



    String sql_pedido = "CREATE TABLE pedido(" +
            "      id_pedido INT  , " +
            "      id_cliente INTEGER, " +
            "      cantidad INTEGER, " +
            "      fecha_compra DATE, " +
            "      importe NUMERIC(10,2), " +
            "      id_producto INTEGER, " +
            "      FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)," +
            "      FOREIGN KEY (id_producto) REFERENCES producto (id_producto)" +
            "    );";

    String sql_empleado_pedido ="CREATE TABLE empleado_pedido(" +
            "  id_empleado INT, " +
            "  id_pedido INT, " +
            "  FOREIGN KEY (id_empleado) REFERENCES empleado (id_empleado)," +
            "  FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido)" +
            ");";

    String sql_rol ="CREATE TABLE rol (" +
            "  id_rol INT  , " +
            "  nombre_rol VARCHAR(150) " +
            ");";

    String sql_ruta = "CREATE TABLE ruta(" +
            "  id_ruta INT  , " +
            "  descripcion VARCHAR(200)" +
            ");";

    String sql_ruta_cliente ="CREATE TABLE rutas_clientes(" +
            "  id_ruta INT, " +
            "  id_cliente INT, " +
            "  FOREIGN KEY (id_ruta) REFERENCES ruta (id_ruta)," +
            "  FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)" +
            ");";

    String sql_empleado_rol ="CREATE TABLE empleado_rol(" +
            "  id_empleado INT, " +
            "  id_rol INT, " +
            "  FOREIGN KEY (id_empleado) REFERENCES empleado (id_empleado)," +
            "  FOREIGN KEY (id_rol) REFERENCES pedido (id_rol)" +
            ");";

    String sql_bitacora ="CREATE table bitacora(" +
            "  id_bitacora INT , " +
            "  nickname VARCHAR(150), " +
            "  token VARCHAR(200) , " +
            "  fecha_inicio TIMESTAMP, " +
            "  fecha_fin TIMESTAMP " +
            ");";


    public DBVentas(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        requestQueue = Volley.newRequestQueue(context);
    }



    @Override
    public void onCreate(final SQLiteDatabase db) {

        db.execSQL(sql_usuario);
        db.execSQL(sql_genero);
        db.execSQL(sql_empleado);
        db.execSQL(sql_cliente);
        db.execSQL(sql_producto);
        db.execSQL(sql_pedido);
        db.execSQL(sql_empleado_pedido);
        db.execSQL(sql_rol);
        db.execSQL(sql_ruta);
        db.execSQL(sql_ruta_cliente);
        db.execSQL(sql_empleado_rol);
        db.execSQL(sql_bitacora);


        /*
        StringRequest request2 = new StringRequest(Request.Method.GET, URL_generos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("genero");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id_genero = jsonObject1.getInt("id_genero");
                        String descripcion = jsonObject1.getString("descripcion");

                        String sql3 = "INSERT INTO genero (id_genero,descripcion) " +
                                "VALUES("+id_genero+",'"+descripcion+"')";
                        System.out.println(sql3);
                        //db.execSQL(sql3);
                    }


                }catch (JSONException e){

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request2);



        StringRequest request3 = new StringRequest(Request.Method.GET, URL_empl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("empleado");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            int id_empleado = jsonObject1.getInt("id_empleado");
                            String nombre_empleado = jsonObject1.getString("nombre_empleado");
                            String ape_pat =jsonObject1.getString("apellido_paterno");
                            String ape_mat = jsonObject1.getString("apellido_materno");
                            String fec_nac = jsonObject1.getString("fecha_nacimiento");
                            String email = jsonObject1.getString("email_empleado");
                            String telefono = jsonObject1.getString("telefono");
                            String rfc = jsonObject1.getString("rfc");
                            int id_genero = jsonObject1.getInt("id_genero");
                            int id_usuario = jsonObject1.getInt("id_usuario");


                            String sql4 ="INSERT INTO empleado (id_empleado,nombre_empleado, apellido_paterno, apellido_materno, fecha_nacimiento, email_empleado, telefono, RFC, id_genero, id_usuario) " +
                                 "VALUES("+id_empleado+",'"+nombre_empleado+",'"+ape_pat+"','"+ape_mat+"','"+fec_nac+"','"+email+"','"+telefono+"','"+rfc+"',"+id_genero+","+id_usuario+")";
                            System.out.println(sql4);
                            //db.execSQL(sql4);

                        }

                    }catch (JSONException e){

                        e.printStackTrace();
                    }
        }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request3);


        StringRequest request4 = new StringRequest(Request.Method.GET, URL_clientes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("cliente");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id_cliente = jsonObject1.getInt("id_cliente");
                        String nombre_cliente =jsonObject1.getString("nombre_cliente");
                        String ape_pat = jsonObject1.getString("apellido_paterno");
                        String ape_mat = jsonObject1.getString("apellido_materno");
                        String telefono = jsonObject1.getString("telefono");
                        String email = jsonObject1.getString("email");
                        String latitud = jsonObject1.getString("latitud");
                        String longitud = jsonObject1.getString("longitud");

                        String sql5 ="INSERT INTO cliente (id_cliente,nombre_cliente, apellido_paterno, apellido_materno, telefono, email, latitud, longitud) " +
                                "VALUES("+id_cliente+",'"+nombre_cliente+",'"+ape_pat+"','"+ape_mat+"','"+telefono+"','"+email+"','"+telefono+"','"+latitud+"','"+longitud+"')";
                        System.out.println(sql5);
                        //db.execSQL(sql5);

                    }


                }catch (JSONException e){

                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request4);





        //
        StringRequest request5 = new StringRequest(Request.Method.GET, URL_productos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("producto");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id_prod = jsonObject1.getInt("id_producto");
                        String nombre_prod = jsonObject1.getString("nombre_producto");
                        String descripcion = jsonObject1.getString("descripcion");
                        double precio = jsonObject1.getDouble("precio");
                        int num_existencia = jsonObject1.getInt("num_existencia");

                        String sql6 ="INSERT INTO producto (id_producto,nombre_producto, descripcion, precio, num_existencia) " +
                                "VALUES("+id_prod+"',"+nombre_prod+",'"+descripcion+"',"+precio+","+num_existencia+")";

                        System.out.println(sql6);
                       // db.execSQL(sql6);

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request5);


        StringRequest request6 = new StringRequest(Request.Method.GET, URL_pedido, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("pedido2");
                    //System.out.println(jsonObject.getJSONArray("pedido2"));
                    System.out.println("TAMAÑO DEL ARRAY"+  jsonArray.length());
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String fecha_compra = jsonObject1.getString("fecha_compra");
                        int id_cliente = jsonObject1.getInt("id_cliente");
                        int id_pedido = jsonObject1.getInt("id_pedido");
                        int id_producto = jsonObject1.getInt("id_producto");
                        double importe = jsonObject1.getDouble("importe");


                        String sql7 = "INSERT INTO pedido (id_pedido,id_cliente,cantidad,fecha_compra,importe,id_producto) " +
                                "VALUES("+id_pedido+","+id_cliente+",'"+fecha_compra+"',"+importe+","+id_producto+")";

                        System.out.println(sql7);

                        //db.execSQL(sql7);
                    }

                }catch (JSONException e){

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request6);



        StringRequest request7 = new StringRequest(Request.Method.GET, URL_roles, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("rol");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id_rol = jsonObject1.getInt("id_rol");
                        String nombre_rol = jsonObject1.getString("nombre_rol");

                        String sql8 = "INSERT INTO rol (id_rol,nombre_rol) " +
                                "VALUES("+id_rol+",'"+nombre_rol+"')";

                        System.out.println(sql8);

                        //db.execSQL(sql8);
                    }


                }catch (JSONException e){

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request7);

        //
        StringRequest request8 = new StringRequest(Request.Method.GET, URL_rutas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("ruta");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id_ruta = jsonObject1.getInt("id_ruta");
                        String desc = jsonObject1.getString("descripcion");

                        String sql9 = "INSERT INTO ruta (id_ruta,descripcion) " +
                                "VALUES("+id_ruta+",'"+desc+"')";

                        System.out.println(sql9);

                        //db.execSQL(sql9);




                    }


                }catch (JSONException e){

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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
        requestQueue.add(request8);
         */


        StringRequest request = new StringRequest(Request.Method.GET, URL_Usuarios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id_usuario = jsonObject1.getInt("id_usuario");
                        String nickname = jsonObject1.getString("nickname");
                        String contrasena = jsonObject1.getString("contrasena");

                        String sql2 = "INSERT INTO usuario (id_usuario,nickname,contrasena) " +
                                "VALUES("+id_usuario+",'"+nickname+"','"+contrasena+"')";
                        System.out.println("SQL INSERT USUARIO:    " + sql2);
                        db.execSQL(sql2);
                    }
                }catch (JSONException e){

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
