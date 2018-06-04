package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RutaActivity extends AppCompatActivity {


    @BindView(R.id.cardAltaRutas)
    CardView cardAltaRutas;
    @BindView(R.id.cardListaRutas)
    CardView cardListaRutas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        ButterKnife.bind(this);
    }

 @OnClick({R.id.cardAltaRutas})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(RutaActivity.this,AltaRutaActivity.class);
        RutaActivity.this.startActivity(intent);
    }


    @OnClick({R.id.cardListaRutas})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(RutaActivity.this,ListaRutasActivity.class);
        RutaActivity.this.startActivity(intent);
    }
}
