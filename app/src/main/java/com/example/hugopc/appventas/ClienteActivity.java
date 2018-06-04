package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClienteActivity extends AppCompatActivity {

    @BindView(R.id.cardAltaClientes)
    CardView cardAltaClientes;
    @BindView(R.id.cardListaClientes)
    CardView cardListaClientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.cardAltaClientes})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(ClienteActivity.this,cliente_alta.class);
        ClienteActivity.this.startActivity(intent);
    }



    @OnClick({R.id.cardListaClientes})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(ClienteActivity.this,ListaClientesActivity.class);
        ClienteActivity.this.startActivity(intent);
    }


}
