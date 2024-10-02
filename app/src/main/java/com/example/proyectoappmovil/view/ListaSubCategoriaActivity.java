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
import com.example.proyectoappmovil.view.adapters.CategoriaAdapter;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;

public class ListaSubCategoriaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios categoria=null;
    CategoriaAdapter adapter;
    RecyclerView recyclerCategoria;
    TextView idCategoria, idUsuario, idSubcategoria;
    MediaPlayer sonido;
    ArrayList<Usuarios> listategoria = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sub_categoria);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerCategoria=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        idCategoria=findViewById(R.id.txt_id_categoria);
        idSubcategoria=findViewById(R.id.txt_id_subcategoria);
        idUsuario=findViewById(R.id.txt_id_usuario);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_categoria");
            String info2 = bundle.getString("id_usuario");
            idUsuario.setText(info2);
            idCategoria.setText(info);

        }
        listategoria=new ArrayList<>();
        adapter=new CategoriaAdapter(listategoria);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerCategoria.setLayoutManager(lmStaggeredHorizontal);
        recyclerCategoria.setHasFixedSize(true);
        recyclerCategoria.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        consultarListaSubcategoria();
}
    private void consultarListaSubcategoria(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id= idCategoria.getText().toString();
        try {
            Cursor cursor=db.rawQuery("select * from subcategoria where id_categoria='"+id+"'",null);
            while (cursor.moveToNext()){

                categoria=new Usuarios();
                categoria.setId(cursor.getString(0));
                categoria.setNombre(cursor.getString(2));
                listategoria.add(categoria);
                adapter=new CategoriaAdapter(listategoria);
                recyclerCategoria.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sonido.start();
                        idSubcategoria.setText(listategoria.get
                                        (recyclerCategoria.getChildAdapterPosition(view))
                                .getId());

                        Toast.makeText(getApplicationContext(),
                                listategoria.get
                                                (recyclerCategoria.getChildAdapterPosition(view))
                                        .getNombre(),Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent=new Intent(ListaSubCategoriaActivity.this, ListaProductoVentaActivity.class);
                        intent.putExtra("id_subcategoria", idSubcategoria.getText().toString());
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

    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaCategoriaActivity.class);
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }
}
