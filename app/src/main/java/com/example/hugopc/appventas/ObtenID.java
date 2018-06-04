package com.example.hugopc.appventas;

import android.content.Context;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ObtenID  {



    private RequestQueue requestQueue2;
    private StringRequest stringRequest;
    JsonObjectRequest JsonRequest;
    private JSONObject jsonObject;

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    int id_user;
    String nick;


    public ObtenID(Context context) {

        requestQueue2 = Volley.newRequestQueue(context);

    }

    public void m_obtenID(String p_nick,String p_pass){

        //System.out.println("ENTRO A OBTENER EL ID USUARIO");
        String URL_p = IP_ADDRES+"usuario/getidusuario/"+p_nick+"/"+p_pass;
        System.out.println(URL_p);

        JsonRequest = new JsonObjectRequest(Request.Method.GET, URL_p, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Entro al response de traer el ID usuario");
                System.out.println(response.toString());

                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    //System.out.println("OBJETO :" + response);
                    //System.out.println("TAMAÑO" + response.length());
                    JSONObject jsonObject1 = response.getJSONObject("usuario");
                    //System.out.println(jsonObject1.getInt("id_usuario"));
                    //System.out.println(jsonObject1.getString("nickname"));
                    id_user = jsonObject1.getInt("id_usuario");
                    Ajuste.id_usua = id_user;
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
