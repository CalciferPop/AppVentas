package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RolActivity extends AppCompatActivity {

    @BindView(R.id.cardAltaRoles)
    CardView cardAltaRoles;
    @BindView(R.id.cardListaRoles)
    CardView cardListaRoles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cardAltaRoles})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(RolActivity.this,AltaRolActivity.class);
        RolActivity.this.startActivity(intent);
    }



    @OnClick({R.id.cardListaRoles})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(RolActivity.this,ListaRolesActivity.class);
        RolActivity.this.startActivity(intent);
    }


}
