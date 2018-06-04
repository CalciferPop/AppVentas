package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Productoactivity extends AppCompatActivity {

    @BindView(R.id.cardAltaProductos)
    CardView cardAltaProdu;
    @BindView(R.id.cardListaProductos)
    CardView cardListaProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productoactivity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cardAltaProductos})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(Productoactivity.this,producto_alta.class);
        Productoactivity.this.startActivity(intent);
    }


    @OnClick({R.id.cardListaProductos})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(Productoactivity.this,ListaProductosActivity.class);
        Productoactivity.this.startActivity(intent);
    }


}
