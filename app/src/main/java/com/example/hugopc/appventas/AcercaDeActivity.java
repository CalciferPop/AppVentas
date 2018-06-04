package com.example.hugopc.appventas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcercaDeActivity extends AppCompatActivity {

    @BindView(R.id.txtWebTec)
    TextView txtWebTec;
    @BindView(R.id.txtEmailHugo)
    TextView txtEmail;
    @BindView(R.id.txtPhoneHugo)
    TextView txtPhone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        ButterKnife.bind(this);
    }



    @OnClick({R.id.txtWebTec})
    public void OnClick(View v){

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(txtWebTec.getText().toString()));
        startActivity(intent);
    }


    @OnClick({R.id.txtEmailHugo})
    public void OnClick2(View v){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Informes app");
        intent.putExtra(Intent.EXTRA_TEXT, "texto del correo");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {txtEmail.getText().toString()});
        startActivity(intent);
    }


    @SuppressLint("MissingPermission")
    @OnClick({R.id.txtPhoneHugo})
    public void OnClick3(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + txtPhone.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }


}
