package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;
import com.example.proyectoappmovil.util.Utilidades;

import java.util.ArrayList;

public class RegistrarComunaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreComuna;
    TextView selecCiudad, idCiudad;
    MediaPlayer sonido;
    Spinner ciudadSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<String> listaCiudad;
    ArrayList<Usuarios> ciudadLista;
    Usuarios ciudad=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_comuna);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreComuna=findViewById(R.id.txt_comuna);
        ciudadSpinner=findViewById(R.id.btnSpinner);
        idCiudad=findViewById(R.id.txt_nombreCiudad);
        selecCiudad=findViewById(R.id.select_ciudad);
        sonido=MediaPlayer.create(this, R.raw.click);
        llenarCiudad();

        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCiudad);
        ciudadSpinner.setAdapter(adapter);
        ciudadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selecCiudad.setText(parent.getItemAtPosition(position).toString());
                consultarNombreCiudad();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }
    public void registrarSubCategoria(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_COMUNA, nombreComuna.getText().toString());
        values.put(Utilidades.CAMPO_ID_CIUDAD, idCiudad.getText().toString());
        Long idResultado=bd.insert(Utilidades.TABLA_COMUNA,Utilidades.CAMPO_ID_COMUNA, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreComuna.setText("");
        bd.close();
    }

    public void registrar(View view) {
        sonido.start();
        nombreComuna.setError(null);
        String valor = nombreComuna.getText().toString();

        if(TextUtils.isEmpty(valor)){
            nombreComuna.setError(getString(R.string.error_ingrese_nombre));
            nombreComuna.requestFocus();
            Toast.makeText(RegistrarComunaActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;

        }
        registrarSubCategoria();

    }
    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
    private void llenarCiudad(){
        SQLiteDatabase db = conn.getReadableDatabase();
        ciudadLista=new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("select * from ciudad", null);
        while (cursor.moveToNext()) {
            ciudad = new Usuarios();
            ciudad.setId(cursor.getString(0));
            ciudad.setNombre(cursor.getString(1));
            ciudadLista.add(ciudad);
        }
        obtenerLista();
        db.close();
    }
    private void obtenerLista(){
        listaCiudad= new ArrayList<String>();
        for(int i=0;i<ciudadLista.size();i++){
            //listaCategoria.add(categoriaLista.get(i).getId()+" - "+categoriaLista.get(i).getNombre());
            listaCiudad.add(ciudadLista.get(i).getNombre());
        }
    }
    private void consultarNombreCiudad() {
        String nombre=selecCiudad.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from ciudad where nombre_ciudad='" + nombre +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                idCiudad.setText(id);

            }


        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();
    }
}