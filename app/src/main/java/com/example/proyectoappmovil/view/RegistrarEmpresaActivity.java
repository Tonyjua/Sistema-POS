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

public class RegistrarEmpresaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreEmpresa, rut, direccion, correo, telefono;
    TextView selecComuna, idComuna;
    MediaPlayer sonido;
    Spinner comunaSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<String> listaComuna;
    ArrayList<Usuarios> comunaLista;
    Usuarios comuna=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_empresa);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreEmpresa=findViewById(R.id.txt_user);
        rut=findViewById(R.id.txt_rut);
        correo=findViewById(R.id.txt_correo);
        telefono=findViewById(R.id.txt_telefono);
        direccion=findViewById(R.id.txt_direccion);
        comunaSpinner=findViewById(R.id.btnSpinner);
        idComuna=findViewById(R.id.txt_idComuna);
        selecComuna=findViewById(R.id.select_comuna);
        sonido=MediaPlayer.create(this, R.raw.click);
        llenarComuna();

        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaComuna);
        comunaSpinner.setAdapter(adapter);
        comunaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selecComuna.setText(parent.getItemAtPosition(position).toString());
                consultarNombreComuna();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }
    public void registrarEmpresa(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_EMPRESA, nombreEmpresa.getText().toString());
        values.put(Utilidades.CAMPO_RUT_EMPRESA, rut.getText().toString());
        values.put(Utilidades.CAMPO_CORREO_EMPRESA, correo.getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO_EMPRESA, telefono.getText().toString());
        values.put(Utilidades.CAMPO_DIRECCION_EMPRESA, direccion.getText().toString());
        values.put(Utilidades.CAMPO_ID_COMUNA, idComuna.getText().toString());
        Long idResultado=bd.insert(Utilidades.TABLA_EMPRESA,Utilidades.CAMPO_ID_EMPRESA, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreEmpresa.setText("");
        rut.setText("");
        correo.setText("");
        telefono.setText("");
        direccion.setText("");
        bd.close();
    }

    public void registrar(View view) {
        sonido.start();
        nombreEmpresa.setError(null);
        String valor = nombreEmpresa.getText().toString();
        String valorRut = rut.getText().toString();
        if(TextUtils.isEmpty(valor)){
            nombreEmpresa.setError(getString(R.string.error_ingrese_nombre));
            nombreEmpresa.requestFocus();
            Toast.makeText(RegistrarEmpresaActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(valorRut)){
            rut.setError(getString(R.string.error_ingrese_nombre));
            rut.requestFocus();
            Toast.makeText(RegistrarEmpresaActivity.this,"Ingrese rut",Toast.LENGTH_SHORT).show();
            return;

        }
        registrarEmpresa();

    }
    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
    private void llenarComuna(){
        SQLiteDatabase db = conn.getReadableDatabase();
        comunaLista=new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("select * from comuna", null);
        while (cursor.moveToNext()) {
            comuna = new Usuarios();
            comuna.setId(cursor.getString(0));
            comuna.setNombre(cursor.getString(2));
            comunaLista.add(comuna);
        }
        obtenerLista();
        db.close();
    }
    private void obtenerLista(){
        listaComuna= new ArrayList<String>();
        for(int i=0;i<comunaLista.size();i++){
            //listaCategoria.add(categoriaLista.get(i).getId()+" - "+categoriaLista.get(i).getNombre());
            listaComuna.add(comunaLista.get(i).getNombre());
        }
    }
    private void consultarNombreComuna() {
        String nombre=selecComuna.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from comuna where nombre_comuna='" + nombre +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                idComuna.setText(id);

            }


        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();
    }
}