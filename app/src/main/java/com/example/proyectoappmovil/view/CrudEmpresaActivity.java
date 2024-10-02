package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

public class CrudEmpresaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreEmpresa, rut, direccion, correo, telefono;
    TextView selecComuna, idComuna, idEmpresa;
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
        setContentView(R.layout.activity_crud_empresa);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreEmpresa=findViewById(R.id.txt_user);
        rut=findViewById(R.id.txt_rut);
        idEmpresa=findViewById(R.id.txt_id_empresa);
        correo=findViewById(R.id.txt_correo);
        telefono=findViewById(R.id.txt_telefono);
        direccion=findViewById(R.id.txt_direccion);
        comunaSpinner=findViewById(R.id.btnSpinner);
        idComuna=findViewById(R.id.txt_idComuna);
        selecComuna=findViewById(R.id.select_comuna);
        sonido=MediaPlayer.create(this, R.raw.click);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_empresa");
            idEmpresa.setText(info);

        }
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
        consultaEmpresa();
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
    private void consultaEmpresa(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=idEmpresa.getText().toString();
        Cursor cursor=db.rawQuery("select * from empresa where id_empresa='"+id+ "'",null);
        while (cursor.moveToNext()){
            comuna=new Usuarios();
            comuna.setNombre(cursor.getString(2));
            comuna.setRut(cursor.getString(3));
            comuna.setCorreo(cursor.getString(4));
            comuna.setTelefono(cursor.getString(5));
            comuna.setDirrecion(cursor.getString(6));

            nombreEmpresa.setText(comuna.getNombre());
            rut.setText(comuna.getRut());
            correo.setText(comuna.getCorreo());
            telefono.setText(comuna.getTelefono());
            direccion.setText(comuna.getDirrecion());
        }
        db.close();

    }
    private void modificarEmpresa(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametro = {idEmpresa.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_EMPRESA, nombreEmpresa.getText().toString());
        values.put(Utilidades.CAMPO_RUT_EMPRESA, rut.getText().toString());
        values.put(Utilidades.CAMPO_CORREO_EMPRESA, correo.getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO_EMPRESA, telefono.getText().toString());
        values.put(Utilidades.CAMPO_DIRECCION_EMPRESA, direccion.getText().toString());
        values.put(Utilidades.CAMPO_ID_COMUNA, idComuna.getText().toString());
        db.update(Utilidades.TABLA_EMPRESA, values,Utilidades.CAMPO_ID_EMPRESA+"=?",parametro);
        db.close();

    }
    private void eliminarEmpresa(){
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametro = {idEmpresa.getText().toString()};
        db.delete(Utilidades.TABLA_EMPRESA,Utilidades.CAMPO_ID_EMPRESA+"=?",parametro);
        db.close();
        idEmpresa.setText("");
        nombreEmpresa.setText("");
        rut.setText("");
        correo.setText("");
        telefono.setText("");
        direccion.setText("");
        db.close();
    }

    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaEmpresasActivity.class);
        startActivity(intent);
    }

    public void actualizar(View view) {
        sonido.start();
        String nombre = nombreEmpresa.getText().toString();
        String rutEmpresa = rut.getText().toString();
        String correoEmpresa = correo.getText().toString();
        String fono = telefono.getText().toString();
        String direccionEmpresa = direccion.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            nombreEmpresa.setError(getString(R.string.error_ingrese_nombre));
            nombreEmpresa.requestFocus();
            Toast.makeText(CrudEmpresaActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(rutEmpresa)){
            rut.setError(getString(R.string.error_ingrese_rut));
            rut.requestFocus();
            Toast.makeText(CrudEmpresaActivity.this,"Ingrese un rut",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(correoEmpresa)){
            correo.setError(getString(R.string.error_ingrese_correo));
            correo.requestFocus();
            Toast.makeText(CrudEmpresaActivity.this,"Ingrese un correo",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(fono)){
            telefono.setError(getString(R.string.error_ingrese_telefono));
            telefono.requestFocus();
            Toast.makeText(CrudEmpresaActivity.this,"Ingrese un teléfono",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(direccionEmpresa)){
            direccion.setError(getString(R.string.error_ingrese_direccion));
            direccion.requestFocus();
            Toast.makeText(CrudEmpresaActivity.this,"Ingrese una dirección",Toast.LENGTH_SHORT).show();
            return;
        }

        modificarEmpresa();
        Toast.makeText(CrudEmpresaActivity.this,"Usuario modificado",Toast.LENGTH_SHORT).show();

    }

    public void eliminar(View view) {
        sonido.start();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(CrudEmpresaActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Seguro que desea eliminar la empresa "+nombreEmpresa.getText().toString()+"?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
    public void aceptar() {
        eliminarEmpresa();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(CrudEmpresaActivity.this);
        dialogo1.setCancelable(false);
        final AlertDialog.Builder confirmar = dialogo1.setPositiveButton("Empresa eliminada", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }


    public void registrar(View view) {
        finish();
        sonido.start();
        Intent intent=new Intent(this, RegistrarEmpresaActivity.class);
        startActivity(intent);
    }
}