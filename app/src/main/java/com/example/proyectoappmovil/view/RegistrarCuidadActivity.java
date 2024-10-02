package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.util.Utilidades;

public class RegistrarCuidadActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreCuidad;
    MediaPlayer sonido;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuidad);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreCuidad=findViewById(R.id.txt_ciudad);
        sonido=MediaPlayer.create(this, R.raw.click);
    }
    public void registrarCiudad(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_CIUDAD, nombreCuidad.getText().toString());
        Long idResultado=bd.insert(Utilidades.TABLA_CIUDAD,Utilidades.CAMPO_ID_CIUDAD, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreCuidad.setText("");
        bd.close();
    }
    public void registrar(View view) {
        sonido.start();
        nombreCuidad.setError(null);
        String valor = nombreCuidad.getText().toString();

        if(TextUtils.isEmpty(valor)){
            nombreCuidad.setError(getString(R.string.error_ingrese_nombre));
            nombreCuidad.requestFocus();
            Toast.makeText(RegistrarCuidadActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;

        }


        registrarCiudad();

    }

    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
}