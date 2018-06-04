package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneroActivity extends AppCompatActivity {

    @BindView(R.id.cardAltaGeneros)
    CardView cardAltaGeneros;
    @BindView(R.id.cardListaGeneros)
    CardView cardListaGeneros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cardAltaGeneros})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(GeneroActivity.this,GeneroAltaActivity.class);
        GeneroActivity.this.startActivity(intent);
    }


    @OnClick({R.id.cardListaGeneros})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(GeneroActivity.this,ListaGenerosActivity.class);
        GeneroActivity.this.startActivity(intent);
    }
}
