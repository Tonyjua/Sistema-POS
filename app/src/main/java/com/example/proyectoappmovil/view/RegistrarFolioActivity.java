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

public class RegistrarFolioActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreCategoria;
    MediaPlayer sonido;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_folio);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreCategoria=findViewById(R.id.txt_categoria);
        sonido=MediaPlayer.create(this, R.raw.click);
    }
    public void registrarCategoria(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID_FOLIO, nombreCategoria.getText().toString());
        values.put(Utilidades.CAMPO_ID_EMPRESA, "1");
        Long idResultado=bd.insert(Utilidades.TABLA_FOLIO,Utilidades.CAMPO_ID_FOLIO, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreCategoria.setText("");
        bd.close();
    }

    public void registrar(View view) {
        sonido.start();
        nombreCategoria.setError(null);
        String valor = nombreCategoria.getText().toString();

        if(TextUtils.isEmpty(valor)){
            nombreCategoria.setError(getString(R.string.error_ingrese_nombre));
            nombreCategoria.requestFocus();
            Toast.makeText(RegistrarFolioActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;

        }


        registrarCategoria();

    }
    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
}