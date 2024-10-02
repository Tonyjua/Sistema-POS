package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.util.Utilidades;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public EditText etCorreoUser, etPassword;
    ConexionSQLiteHelper conn;
    MediaPlayer sonido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        etCorreoUser = findViewById(R.id.txtuser);
        etPassword = findViewById(R.id.txtpwd);
        sonido=MediaPlayer.create(this, R.raw.click);

    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void ingresar(View view) {
        sonido.start();
        String valor = etCorreoUser.getText().toString();
        String valorDos = etPassword.getText().toString();
        if (!validarEmail(valor)){
            etCorreoUser.setError("correo no válido");
        }
        if(TextUtils.isEmpty(valor)){
            etCorreoUser.setError(getString(R.string.error_ingrese_correo));
            etCorreoUser.requestFocus();
            Toast.makeText(MainActivity.this,"Ingrese un correo",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(valorDos)){
            etPassword.setError(getString(R.string.error_ingrese_clave));
            etPassword.requestFocus();
            Toast.makeText(MainActivity.this,"Ingrese una clave",Toast.LENGTH_SHORT).show();
            return;

        }
        if(etCorreoUser.getText().toString().equals(Utilidades.CORREO_INGRESO) && etPassword.getText().toString().equals(Utilidades.CLAVE_INGRESO)){
            Intent intent=new Intent(this, MenuPrincipalActivity.class);
            startActivity(intent);
        }else{
            consultarIngreso();
        }

    }
    private void consultarIngreso() {
        String nombre = etCorreoUser.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String tabla=Utilidades.TABLA_USUARIO;
        String tipoUser="Administrador";
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from '" + tabla +"' where correo_usuario='" +
                nombre + "' and clave='" + pass +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                String correo = cursor.getString(6);
                String password = cursor.getString(9);
                String tipoUsuario = cursor.getString(10);
                //preguntamos si los datos ingresados son iguales
                if (nombre.equals(correo) && pass.equals(password) && tipoUser.equals(tipoUsuario)) {
                    //si son iguales entonces vamos a otra ventana
                    //Menu es una nueva actividad empty
                    Intent intent = new Intent(MainActivity.this, MenuPrincipalActivity.class);
                    //lanzamos la actividad
                    startActivity(intent);
                    //limpiamos las las cajas de texto
                    etCorreoUser.setText("");
                    etPassword.setText("");
                }else{
                    Intent intent = new Intent(MainActivity.this, ListaCategoriaActivity.class);
                    intent.putExtra("id_usuario",id);
                    //lanzamos la actividad
                    startActivity(intent);
                    etCorreoUser.setText("");
                    etPassword.setText("");
                }
            } else {
                Toast toast = Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG);
                //mostramos el toast
                toast.show();
            }

        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();
    }
}