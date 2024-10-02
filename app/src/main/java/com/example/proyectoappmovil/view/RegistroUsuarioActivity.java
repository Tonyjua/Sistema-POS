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


public class RegistroUsuarioActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreUser, clave, apellido, rut, correo, telefono, direccion;
    TextView selectUser, selecComuna, idComuna, selecEmpresa, idEmpresa;
    Spinner tipoUsuario, comunaSpinner, empresaSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adaptercomuna;
    ArrayAdapter<CharSequence> adapterEmpresa;
    ArrayList<String> listaComuna;
    ArrayList<String> listaEmpresa;
    ArrayList<Usuarios> comunaLista;
    MediaPlayer sonido;
    Usuarios comuna=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreUser=findViewById(R.id.txt_user);
        apellido=findViewById(R.id.txt_apellido);
        rut=findViewById(R.id.txt_rut);
        correo=findViewById(R.id.txt_correo);
        telefono=findViewById(R.id.txt_telefono);
        direccion=findViewById(R.id.txt_direccion);
        clave=findViewById(R.id.txt_clave);
        comunaSpinner=findViewById(R.id.spinner_comuna);
        empresaSpinner=findViewById(R.id.spinner_empresa);
        tipoUsuario=findViewById(R.id.btnSpinner);
        selectUser=findViewById(R.id.select_user);
        idComuna=findViewById(R.id.txt_idComuna);
        selecComuna=findViewById(R.id.select_comuna);
        idEmpresa=findViewById(R.id.txt_idEmpresa);
        selecEmpresa=findViewById(R.id.select_empresa);
        sonido=MediaPlayer.create(this, R.raw.click);
        adapter= ArrayAdapter.createFromResource(this, R.array.Tipo_usuarios, android.R.layout.simple_spinner_item);
        tipoUsuario.setAdapter(adapter);
        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectUser.setText(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                
            }
        });
        llenarComuna();
        llenarEmpresa();
        adaptercomuna=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaComuna);
        comunaSpinner.setAdapter(adaptercomuna);
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
        adapterEmpresa=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaEmpresa);
        empresaSpinner.setAdapter(adapterEmpresa);
        empresaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selecEmpresa.setText(parent.getItemAtPosition(position).toString());
                consultaridEmpresa();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }
    public void registrarUsuario(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID_EMPRESA, idEmpresa.getText().toString());
        values.put(Utilidades.CAMPO_ID_COMUNA, idComuna.getText().toString());
        values.put(Utilidades.CAMPO_NOMBRE_USUARIO, nombreUser.getText().toString());
        values.put(Utilidades.CAMPO_APELLIDO_USUARIO, apellido.getText().toString());
        values.put(Utilidades.CAMPO_RUT_USUARIO, rut.getText().toString());
        values.put(Utilidades.CAMPO_CORREO_USUARIO, correo.getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO_USUARIO, telefono.getText().toString());
        values.put(Utilidades.CAMPO_DIRECCION_USUARIO, direccion.getText().toString());
        values.put(Utilidades.CAMPO_CLAVE, clave.getText().toString());
        values.put(Utilidades.CAMPO_TIPO_USUARIO, selectUser.getText().toString());
        Long idResultado=bd.insert(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_ID_USUARIO, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreUser.setText("");
        apellido.setText("");
        rut.setText("");
        correo.setText("");
        telefono.setText("");
        direccion.setText("");
        clave.setText("");
        bd.close();
    }

    public void registrar(View view) {
        sonido.start();
        String nombre = nombreUser.getText().toString();
        String apellidoUser = apellido.getText().toString();
        String rutUser = rut.getText().toString();
        String correoUser = correo.getText().toString();
        String fono = telefono.getText().toString();
        String direccionUser = direccion.getText().toString();
        String claveUser = clave.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            nombreUser.setError(getString(R.string.error_ingrese_nombre));
            nombreUser.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(apellidoUser)){
            apellido.setError(getString(R.string.error_ingrese_apellido));
            apellido.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese un apellido",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(rutUser)){
            rut.setError(getString(R.string.error_ingrese_rut));
            rut.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese un rut",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(correoUser)){
            correo.setError(getString(R.string.error_ingrese_correo));
            correo.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese un correo",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(fono)){
            telefono.setError(getString(R.string.error_ingrese_telefono));
            telefono.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese un teléfono",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(direccionUser)){
            direccion.setError(getString(R.string.error_ingrese_direccion));
            direccion.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese una dirección",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(claveUser)){
            clave.setError(getString(R.string.error_ingrese_clave));
            clave.requestFocus();
            Toast.makeText(RegistroUsuarioActivity.this,"Ingrese una clave",Toast.LENGTH_SHORT).show();
            return;
        }
        registrarUsuario();

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

    private void llenarEmpresa(){
        SQLiteDatabase db = conn.getReadableDatabase();
        comunaLista=new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("select * from empresa", null);
        while (cursor.moveToNext()) {
            comuna = new Usuarios();
            comuna.setId(cursor.getString(0));
            comuna.setNombre(cursor.getString(2));
            comunaLista.add(comuna);
        }
        obtenerListaEmpresa();
        db.close();

    }
    private void obtenerListaEmpresa(){
        listaEmpresa= new ArrayList<String>();
        for(int i=0;i<comunaLista.size();i++){
            //listaCategoria.add(categoriaLista.get(i).getId()+" - "+categoriaLista.get(i).getNombre());
            listaEmpresa.add(comunaLista.get(i).getNombre());
        }
    }
    private void consultaridEmpresa() {
        String nombre=selecEmpresa.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from empresa where nombre_empresa='" + nombre +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                idEmpresa.setText(id);

            }


        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();

    }

}