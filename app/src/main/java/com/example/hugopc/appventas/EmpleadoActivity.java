package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmpleadoActivity extends AppCompatActivity {


    @BindView(R.id.cardAltaEmpleados)
    CardView cardAltaEmpleados;
    @BindView(R.id.cardListaEmpleados)
    CardView cardListaEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cardAltaEmpleados})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(EmpleadoActivity.this,empleado_alta.class);
        EmpleadoActivity.this.startActivity(intent);
    }

    @OnClick({R.id.cardListaEmpleados})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
        Intent intent= new Intent(EmpleadoActivity.this,listEmpleado.class);
        EmpleadoActivity.this.startActivity(intent);
    }

}
