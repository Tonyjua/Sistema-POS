package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.view.adapters.UsuarioAdaptar;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;

public class ListaUsuariosActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios usuario=null;
    UsuarioAdaptar adapter;
    RecyclerView recyclerUsuarios;
    TextView idUser;
    MediaPlayer sonido;
    ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerUsuarios=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        idUser=findViewById(R.id.txt_id_usuario);
        listaUsuarios=new ArrayList<>();
        adapter=new UsuarioAdaptar(listaUsuarios);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerUsuarios.setLayoutManager(lmStaggeredHorizontal);
        recyclerUsuarios.setHasFixedSize(true);
        recyclerUsuarios.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        consultarListaUsuarios();
    }
    private void consultarListaUsuarios(){
        SQLiteDatabase db=conn.getReadableDatabase();

        try {
            Cursor cursor=db.rawQuery("select * from usuario",null);
            while (cursor.moveToNext()){

                usuario=new Usuarios();
                usuario.setId(cursor.getString(0));
                usuario.setNombre(cursor.getString(3));
                usuario.setTipoUser(cursor.getString(10));
              //  usuario.setImagen(cursor.getBlob(4));
                listaUsuarios.add(usuario);
                adapter=new UsuarioAdaptar(listaUsuarios);
                recyclerUsuarios.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sonido.start();
                        idUser.setText(listaUsuarios.get
                                        (recyclerUsuarios.getChildAdapterPosition(view))
                                .getId());

                        Toast.makeText(getApplicationContext(),
                                listaUsuarios.get
                                                (recyclerUsuarios.getChildAdapterPosition(view))
                                        .getNombre(),Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent=new Intent(ListaUsuariosActivity.this, CrudUsuarioActivity.class);
                        intent.putExtra("id_usuario", idUser.getText().toString());
                        startActivity(intent);

                    }
                });


                }

        } catch(Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        db.close();
    }

    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
}