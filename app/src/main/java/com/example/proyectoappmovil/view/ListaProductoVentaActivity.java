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
import com.example.proyectoappmovil.view.adapters.ProductoAdaptar;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;

public class ListaProductoVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios producto=null;
    ProductoAdaptar adapter;
    RecyclerView recyclerProductos;
    TextView idProducto, idSubcategoria, idCategoria, idUsuario;
    MediaPlayer sonido;
    ArrayList<Usuarios> listaProductos = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_venta);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerProductos=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        idProducto=findViewById(R.id.txt_id_producto);
        idSubcategoria=findViewById(R.id.txt_id_subcategoria);
        idCategoria=findViewById(R.id.txt_id_categoria);
        idUsuario=findViewById(R.id.txt_id_usuario);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_subcategoria");
            String info2 = bundle.getString("id_categoria");
            String info3 = bundle.getString("id_usuario");
            idSubcategoria.setText(info);
            idCategoria.setText(info2);
            idUsuario.setText(info3);

        }
        listaProductos=new ArrayList<>();
        adapter=new ProductoAdaptar(listaProductos);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerProductos.setLayoutManager(lmStaggeredHorizontal);
        recyclerProductos.setHasFixedSize(true);
        recyclerProductos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        consultarListaProductos();
    }
    private void consultarListaProductos(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id= idSubcategoria.getText().toString();

        try {
            Cursor cursor=db.rawQuery("select * from articulo where id_subcategoria='"+id+"'",null);
            while (cursor.moveToNext()){

                producto=new Usuarios();
                producto.setId(cursor.getString(0));
                producto.setNombre(cursor.getString(2));
                producto.setDetalle(cursor.getString(3));
                producto.setImagen(cursor.getBlob(4));
                producto.setPrecio(cursor.getString(5));
                producto.setStock(cursor.getString(6));
                listaProductos.add(producto);
                adapter=new ProductoAdaptar(listaProductos);
                recyclerProductos.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sonido.start();
                        idProducto.setText(listaProductos.get
                                        (recyclerProductos.getChildAdapterPosition(view))
                                .getId());

                        Toast.makeText(getApplicationContext(),
                                listaProductos.get
                                                (recyclerProductos.getChildAdapterPosition(view))
                                        .getNombre(),Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent=new Intent(ListaProductoVentaActivity.this, RegistrarVentaActivity.class);
                        intent.putExtra("id_articulo", idProducto.getText().toString());
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
        Intent intent=new Intent(this, ListaSubCategoriaActivity.class);
        intent.putExtra("id_categoria", idCategoria.getText().toString());
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }
}