package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.proyectoappmovil.R;

import java.util.Timer;
import java.util.TimerTask;

public class MenuPrincipalActivity extends AppCompatActivity {
    MediaPlayer sonido, salir;
    FrameLayout bordes;
    Button menuVenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        sonido=MediaPlayer.create(this, R.raw.click);
        salir=MediaPlayer.create(this, R.raw.salir);
        bordes=findViewById(R.id.borde_efecto);
        //cliclo();
    }

    public void listaUsuarios(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, ListaUsuariosActivity.class);
        startActivity(intent);
    }

    public void listaProductos(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, ListaProductosActivity.class);
        startActivity(intent);
    }

    public void agregarUsuario(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistroUsuarioActivity.class);
        startActivity(intent);
    }

    public void agregarProducto(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarProductoActivity.class);
        startActivity(intent);

    }


    public void historialVentas(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, HistorialVentasActivity.class);
        startActivity(intent);
    }

    public void agregarCategorias(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarCategoriaActivity.class);
        startActivity(intent);
    }

    public void agregarSubCategorias(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarSubCategoriaActivity.class);
        startActivity(intent);
    }

    public void agregarCiudad(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarCuidadActivity.class);
        startActivity(intent);
    }

    public void agregarComuna(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarComunaActivity.class);
        startActivity(intent);
    }

    public void agregarEmpresa(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarEmpresaActivity.class);
        startActivity(intent);
    }

    public void listaEmpresa(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, ListaEmpresasActivity.class);
        startActivity(intent);
    }

    public void menuVentas(View view) {
        sonido.start();
        finish();
        String id="0";
        Intent intent= new Intent(MenuPrincipalActivity.this, ListaCategoriaActivity.class);
        intent.putExtra("id_usuario",id);
        startActivity(intent);
    }

    public void salir(View view) {
        sonido.start();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MenuPrincipalActivity.this);
        dialogo1.setTitle("Cerrando la aplicación");
        dialogo1.setMessage("¿Seguro que desea salir de la aplicación?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                salir.start();
               finish();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
    public void cliclo(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bordes.setBackground(getDrawable(R.drawable.rectangulodeblanco));
                    }
                },1000);
                bordes.setBackground(getDrawable(R.drawable.rectangulodenegro));
            }
        };

        timer.schedule(task, 0, 2000);
    }


    public void folio(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(MenuPrincipalActivity.this, RegistrarFolioActivity.class);
        startActivity(intent);
    }
}