package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.view.adapters.CategoriaAdapter;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;

public class ListaCategoriaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios categoria=null;
    CategoriaAdapter adapter;
    RecyclerView recyclerCategoria;
    TextView idCategoria, idUsuario;
    MediaPlayer sonido, salir;
    ArrayList<Usuarios> listategoria = new ArrayList<>();

    Button btnVolver;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerCategoria=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        salir=MediaPlayer.create(this, R.raw.salir);
        idCategoria=findViewById(R.id.txt_id_categoria);
        idUsuario=findViewById(R.id.txt_id_usuario);
        btnVolver=findViewById(R.id.boton_volver);

        listategoria=new ArrayList<>();
        adapter=new CategoriaAdapter(listategoria);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerCategoria.setLayoutManager(lmStaggeredHorizontal);
        recyclerCategoria.setHasFixedSize(true);
        recyclerCategoria.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_usuario");
            idUsuario.setText(info);

        }
        consultarListaCategoria();



    }
    private void consultarListaCategoria(){
        SQLiteDatabase db=conn.getReadableDatabase();

        try {
            Cursor cursor=db.rawQuery("select * from categoria",null);
            while (cursor.moveToNext()){

                categoria=new Usuarios();
                categoria.setId(cursor.getString(0));
                categoria.setNombre(cursor.getString(1));
                listategoria.add(categoria);
                adapter=new CategoriaAdapter(listategoria);
                recyclerCategoria.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sonido.start();
                        idCategoria.setText(listategoria.get
                                        (recyclerCategoria.getChildAdapterPosition(view))
                                .getId());

                        Toast.makeText(getApplicationContext(),
                                listategoria.get
                                                (recyclerCategoria.getChildAdapterPosition(view))
                                        .getNombre(),Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent=new Intent(ListaCategoriaActivity.this, ListaSubCategoriaActivity.class);
                        intent.putExtra("id_categoria", idCategoria.getText().toString());
                        intent.putExtra("id_usuario", idUsuario.getText().toString());
                        startActivity(intent);

                    }
                });


            }

        } catch(Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        db.close();

    }

    public void salir(View view) {
        sonido.start();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ListaCategoriaActivity.this);
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

    public void listaVenta(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaVentaActivity.class);
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }
}