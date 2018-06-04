package com.example.hugopc.appventas;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FCMTokenService extends FirebaseInstanceIdService implements
        Response.Listener<String>,Response.ErrorListener {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("TOKEN PAPU" + token);
        reg_token(token);
    }

    private void reg_token(String token) {
        RequestQueue qSol = new Volley().newRequestQueue(getApplicationContext());
        String URL = "http://192.168.43.201/notificacionesAppVentas/registro_token.php?token=" + token;
        StringRequest insToken = new StringRequest(Request.Method.GET, URL, this, this);
        qSol.add(insToken);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("error", error.toString());
    }

    @Override
    public void onResponse(String response) {
    }
}
