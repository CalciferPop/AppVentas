package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsuarioActivity extends AppCompatActivity {


    @BindView(R.id.cardAltaUsuarios)
    CardView cardAltaUsuarios;
    @BindView(R.id.cardListaUsuarios)
    CardView cardListaUsuarios;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        ButterKnife.bind(this);
    }

    /*
    @OnClick({R.id.cardAltaUsuarios})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(UsuarioActivity.this,AltaUsuarioActivity.class);
        UsuarioActivity.this.startActivity(intent);
    }
    */
    @OnClick({R.id.cardListaUsuarios})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(UsuarioActivity.this,ListaUsuariosActivity.class);
        UsuarioActivity.this.startActivity(intent);
    }
    
}
