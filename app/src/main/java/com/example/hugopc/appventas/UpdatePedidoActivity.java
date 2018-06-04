package com.example.hugopc.appventas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;

public class UpdatePedidoActivity extends AppCompatActivity {

    private static Constantes Cons = new Constantes();
    private static String IP_ADDRES = Cons.getIP_ADDRESS().toString();

    private static String URL_EndPoint = IP_ADDRES + "producto/listproductos/";
    private static String URL_UPD_Producto = IP_ADDRES + "producto/updateproducto/";

    @BindView(R.id.edtUPDidPedido)
    EditText edtIDPedido;
    @BindView(R.id.edtUPDCantidadPedido)
    EditText edtCantPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pedido);



        Intent intent1 = getIntent();
        final String id = intent1.getStringExtra("id_pedido");
        final String cantidad = intent1.getStringExtra("cantidad");
        int  id_pedido = Integer.valueOf(id);
        int cant = Integer.valueOf(cantidad);

        Toast toast1 = Toast.makeText(this, "ID Pedido a editar: " + id_pedido + " cantidad: " + cant , Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER, 0, 0);
        toast1.show();
    }
}
