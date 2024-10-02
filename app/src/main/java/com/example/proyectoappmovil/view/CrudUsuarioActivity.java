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

public class CrudUsuarioActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios usuario=null;
    TextView idUser, selectUser, selecComuna, idComuna, selecEmpresa, idEmpresa;
    EditText nombreUser, clave, apellido, rut, correo, telefono, direccion;
    Spinner tipoUsuario, comunaSpinner, empresaSpinner;
    ArrayAdapter<CharSequence> adaptercomuna;
    ArrayAdapter<CharSequence> adapterEmpresa;
    ArrayAdapter<CharSequence> adapter;
    MediaPlayer sonido;
    ArrayList<Usuarios> comunaLista;
    ArrayList<String> listaComuna;
    ArrayList<String> listaEmpresa;
    Usuarios comuna=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_usuario);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        idUser=findViewById(R.id.txt_id);
        nombreUser=findViewById(R.id.txt_user);
        apellido=findViewById(R.id.txt_apellido);
        comunaSpinner=findViewById(R.id.spinner_comuna);
        empresaSpinner=findViewById(R.id.spinner_empresa);
        rut=findViewById(R.id.txt_rut);
        correo=findViewById(R.id.txt_correo);
        selecEmpresa=findViewById(R.id.select_empresa);
        telefono=findViewById(R.id.txt_telefono);
        idComuna=findViewById(R.id.txt_idComuna);
        selecComuna=findViewById(R.id.select_comuna);
        idEmpresa=findViewById(R.id.txt_idEmpresa);
        direccion=findViewById(R.id.txt_direccion);
        clave=findViewById(R.id.txt_clave);
        tipoUsuario=findViewById(R.id.btnSpinner);
        selectUser=findViewById(R.id.select_user);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_usuario");
            idUser.setText(info);

        }
        consultaUsuario();
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
    private void consultaUsuario(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=idUser.getText().toString();
        Cursor cursor=db.rawQuery("select * from usuario where id_usuario='"+id+ "'",null);
        while (cursor.moveToNext()){
            usuario=new Usuarios();
            usuario.setNombre(cursor.getString(3));
            usuario.setApellido(cursor.getString(4));
            usuario.setRut(cursor.getString(5));
            usuario.setCorreo(cursor.getString(6));
            usuario.setTelefono(cursor.getString(7));
            usuario.setDirrecion(cursor.getString(8));
            usuario.setPassword(cursor.getString(9));
            nombreUser.setText(usuario.getNombre());
            apellido.setText(usuario.getApellido());
            rut.setText(usuario.getRut());
            correo.setText(usuario.getCorreo());
            telefono.setText(usuario.getTelefono());
            direccion.setText(usuario.getDirrecion());
            clave.setText(usuario.getPassword());
        }
        db.close();
    }
    private void modificarUsuario(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametro = {idUser.getText().toString()};
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
        db.update(Utilidades.TABLA_USUARIO, values,Utilidades.CAMPO_ID_USUARIO+"=?",parametro);
        db.close();

    }
    private void eliminarUsuario(){
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametro = {idUser.getText().toString()};
        db.delete(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_ID_USUARIO+"=?",parametro);
        db.close();
        idUser.setText("");
        nombreUser.setText("");
        apellido.setText("");
        rut.setText("");
        correo.setText("");
        telefono.setText("");
        direccion.setText("");
        clave.setText("");
    }
    public void registrar(View view) {
        finish();
        sonido.start();
        Intent intent=new Intent(this, RegistroUsuarioActivity.class);
        startActivity(intent);
    }

    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaUsuariosActivity.class);
        startActivity(intent);
    }

    public void eliminar(View view) {
        sonido.start();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(CrudUsuarioActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Seguro que desea eliminar el usuario "+idUser.getText().toString()+"?");
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
        eliminarUsuario();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(CrudUsuarioActivity.this);
        dialogo1.setCancelable(false);
        final AlertDialog.Builder confirmar = dialogo1.setPositiveButton("Usuario eliminado", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }

    public void actualizar(View view) {
        sonido.start();
        String id = idUser.getText().toString();
        String nombre = nombreUser.getText().toString();
        String apellidoUser = apellido.getText().toString();
        String rutUser = rut.getText().toString();
        String correoUser = correo.getText().toString();
        String fono = telefono.getText().toString();
        String direccionUser = direccion.getText().toString();
        String claveUser = clave.getText().toString();
        if(TextUtils.isEmpty(id)){
            nombreUser.setError(getString(R.string.error_ingrese_id));
            nombreUser.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(nombre)){
            nombreUser.setError(getString(R.string.error_ingrese_nombre));
            nombreUser.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(apellidoUser)){
            apellido.setError(getString(R.string.error_ingrese_apellido));
            apellido.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese un apellido",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(rutUser)){
            rut.setError(getString(R.string.error_ingrese_rut));
            rut.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese un rut",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(correoUser)){
            correo.setError(getString(R.string.error_ingrese_correo));
            correo.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese un correo",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(fono)){
            telefono.setError(getString(R.string.error_ingrese_telefono));
            telefono.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese un teléfono",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(direccionUser)){
            direccion.setError(getString(R.string.error_ingrese_direccion));
            direccion.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese una dirección",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(claveUser)){
            clave.setError(getString(R.string.error_ingrese_clave));
            clave.requestFocus();
            Toast.makeText(CrudUsuarioActivity.this,"Ingrese una clave",Toast.LENGTH_SHORT).show();
            return;
        }
        modificarUsuario();
        Toast.makeText(CrudUsuarioActivity.this,"Usuario modificado",Toast.LENGTH_SHORT).show();


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