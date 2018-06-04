package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmpleadosRolesActivity extends AppCompatActivity {


    @BindView(R.id.cardAltaEmpleadosRoles)
    CardView cardAltaEmpleRoles;
    @BindView(R.id.cardListaEmpleadosRoles)
    CardView cardListaEmpleRoles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados_roles);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.cardAltaEmpleadosRoles})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(EmpleadosRolesActivity.this,AltaEmpleadoRolActivity.class);
        EmpleadosRolesActivity.this.startActivity(intent);
    }

    @OnClick({R.id.cardListaEmpleadosRoles})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(EmpleadosRolesActivity.this,ListaEmpleadoRolActivity.class);
        EmpleadosRolesActivity.this.startActivity(intent);
    }



}
