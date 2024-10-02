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

public class RegistrarSubCategoriaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreSubCategoria;
    TextView selecCategoria, idCategoria;
    MediaPlayer sonido;
    Spinner categoriaSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<String> listaCategoria;
    ArrayList<Usuarios> categoriaLista;
    Usuarios categoria=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_sub_categoria);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreSubCategoria=findViewById(R.id.txt_subcategoria);
        categoriaSpinner=findViewById(R.id.btnSpinner);
        idCategoria=findViewById(R.id.txt_nombrecat);
        selecCategoria=findViewById(R.id.select_categoria);
        sonido=MediaPlayer.create(this, R.raw.click);
        llenarCategorias();

        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCategoria);
        categoriaSpinner.setAdapter(adapter);
        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selecCategoria.setText(parent.getItemAtPosition(position).toString());
                consultarIdCategoria();
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
        values.put(Utilidades.CAMPO_NOMBRE_SUBCATEGORIA, nombreSubCategoria.getText().toString());
        values.put(Utilidades.CAMPO_ID_CATEGORIA, idCategoria.getText().toString());
        Long idResultado=bd.insert(Utilidades.TABLA_SUBCATEGORIA,Utilidades.CAMPO_ID_SUBCATEGORIA, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreSubCategoria.setText("");
        bd.close();
    }

    public void registrar(View view) {
        sonido.start();
        nombreSubCategoria.setError(null);
        String valor = nombreSubCategoria.getText().toString();

        if(TextUtils.isEmpty(valor)){
            nombreSubCategoria.setError(getString(R.string.error_ingrese_nombre));
            nombreSubCategoria.requestFocus();
            Toast.makeText(RegistrarSubCategoriaActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
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
    private void llenarCategorias(){
        SQLiteDatabase db = conn.getReadableDatabase();
        categoriaLista=new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("select * from categoria", null);
        while (cursor.moveToNext()) {
            categoria = new Usuarios();
            categoria.setId(cursor.getString(0));
            categoria.setNombre(cursor.getString(1));
            categoriaLista.add(categoria);
        }
        obtenerLista();
        db.close();

    }
    private void obtenerLista(){
        listaCategoria= new ArrayList<String>();
        for(int i=0;i<categoriaLista.size();i++){
            //listaCategoria.add(categoriaLista.get(i).getId()+" - "+categoriaLista.get(i).getNombre());
            listaCategoria.add(categoriaLista.get(i).getNombre());
        }
    }
    private void consultarIdCategoria() {
        String nombre=selecCategoria.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from categoria where nombre_categoria='" + nombre +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                idCategoria.setText(id);

                }


        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();

    }
}