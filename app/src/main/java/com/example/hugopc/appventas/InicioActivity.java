package com.example.hugopc.appventas;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import com.onurkaganaldemir.ktoastlib.KToast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InicioActivity extends AppCompatActivity {

    //String token;
    @BindView(R.id.cardEmpleados)
    CardView cardEmpleados;
    @BindView(R.id.cardClientes)
    CardView cardClientes;
    @BindView(R.id.cardProductos)
    CardView cardProd;
    @BindView(R.id.cardPedidos)
    CardView cardPedidos;
    @BindView(R.id.cardRoles)
    CardView cardRoles;
    @BindView(R.id.cardRutas)
    CardView cardRutas;
    @BindView(R.id.cardGeneros)
    CardView cardGeneros;
    @BindView(R.id.cardUsuarios)
    CardView cardUsuarios;
    @BindView(R.id.cardRutas_Clientes)
    CardView cardRUCLI;

    @BindView(R.id.cardEmpleado_rol)
    CardView cardEmpleadosrOLES;
    @BindView(R.id.cardAcercaDE)
    CardView cardAcercaDE;


    TextView email_creador,nombre_creador;
    Button comunicate;


    ImageView cerrar;

    Dialog acerca_de;
    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inicio);
            ButterKnife.bind(this);
            acerca_de = new Dialog(this);

                



       // KToast.customColorToast(InicioActivity.this, "Rol del usuario: " + Ajuste.rol, Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
    }



    @OnClick({R.id.cardEmpleados})
    public void OnClick(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

            Intent intent= new Intent(InicioActivity.this,EmpleadoActivity.class);
            InicioActivity.this.startActivity(intent);



    }

    @OnClick({R.id.cardAcercaDE})
    public void OnClick18(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

        m_muestraAcerdaDE();



    }

    private void m_muestraAcerdaDE() {
        acerca_de.setContentView(R.layout.pop_up_acerca_de);

        cerrar = (ImageView) acerca_de.findViewById(R.id.img_close);
        comunicate = (Button)acerca_de.findViewById(R.id.btnComunicate);
        email_creador = (TextView) acerca_de.findViewById(R.id.txt_email_creador);
        nombre_creador = (TextView)acerca_de.findViewById(R.id.nombre_creador);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acerca_de.dismiss();
            }
        });



        comunicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = email_creador.getText().toString();
                String cliente =  nombre_creador.getText().toString();

                m_enviaEmail(correo,cliente);
            }
        });

        acerca_de.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        acerca_de.show();




    }

    private void m_enviaEmail(String p_correo,String p_cliente) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de informes");
        intent.putExtra(Intent.EXTRA_TEXT,  p_cliente + " informes de servicios ");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {p_correo});
        this.startActivity(intent);
    }



    @OnClick({R.id.cardProductos})
    public void OnClick2(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

            Intent intent= new Intent(InicioActivity.this,Productoactivity.class);
            InicioActivity.this.startActivity(intent);




    }

    @OnClick({R.id.cardClientes})
    public void OnClick3(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

            Intent intent = new Intent(InicioActivity.this, ClienteActivity.class);
            InicioActivity.this.startActivity(intent);

    }

    @OnClick({R.id.cardGeneros})
    public void OnClick4(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

            Intent intent = new Intent(InicioActivity.this, GeneroActivity.class);
            InicioActivity.this.startActivity(intent);

    }

    @OnClick({R.id.cardUsuarios})
    public void OnClick5(View v) {
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);


            Intent intent = new Intent(InicioActivity.this, UsuarioActivity.class);
            InicioActivity.this.startActivity(intent);

    }

    @OnClick({R.id.cardPedidos})
    public void OnClick6(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

            Intent intent = new Intent(InicioActivity.this, PedidoActivity.class);
            InicioActivity.this.startActivity(intent);

    }

    @OnClick({R.id.cardRoles})
    public void OnClick7(View v){
        //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);

            Intent intent = new Intent(InicioActivity.this, RolActivity.class);
            InicioActivity.this.startActivity(intent);

    }

    @OnClick({R.id.cardRutas})
    public void OnClick8(View v){

            //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent = new Intent(InicioActivity.this, RutaActivity.class);
            InicioActivity.this.startActivity(intent);

    }

    @OnClick({R.id.cardRutas_Clientes})
    public void OnClick9(View v){

            //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent = new Intent(InicioActivity.this, RutaClienteActivity.class);
            InicioActivity.this.startActivity(intent);

    }


    @OnClick({R.id.cardEmpleado_rol})
    public void OnClick10(View v){

            //KToast.customColorToast(InicioActivity.this, "Click Empleados", Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.success, R.drawable.ic_info_outline_black_24dp);
            Intent intent = new Intent(InicioActivity.this, EmpleadosRolesActivity.class);
            InicioActivity.this.startActivity(intent);

    }
}
