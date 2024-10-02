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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.view.adapters.ProductoAdaptar;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;

public class ListaProductosActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios producto=null;

    Spinner categoriaSpinner;
    ArrayAdapter<CharSequence> adaptador;

    ArrayList<String> listaCategoria;

    ArrayList<Usuarios> categoriaLista;

    ProductoAdaptar adapter;
    RecyclerView recyclerProductos;
    TextView idProducto, idCategoria, selecCategoria, selecSubCategoria,idSubcategoria;
    MediaPlayer sonido;
    Button btnBuscar;
    ArrayList<Usuarios> listaProductos = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerProductos=findViewById(R.id.idRecyclerImagen);
        categoriaSpinner=findViewById(R.id.btnSpinner);
        selecSubCategoria=findViewById(R.id.select_subcategoria);
        idSubcategoria=findViewById(R.id.txt_idsubcategoria);
        idCategoria=findViewById(R.id.txt_idcat);
        btnBuscar=findViewById(R.id.btn_buscar);
        selecCategoria=findViewById(R.id.select_categoria);
        sonido=MediaPlayer.create(this, R.raw.click);
        idProducto=findViewById(R.id.txt_id_producto);
        listaProductos=new ArrayList<>();
        adapter=new ProductoAdaptar(listaProductos);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerProductos.setLayoutManager(lmStaggeredHorizontal);
        recyclerProductos.setHasFixedSize(true);
        recyclerProductos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        llenarCategorias();
        adaptador=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCategoria);
        categoriaSpinner.setAdapter(adaptador);
        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selecCategoria.setText(parent.getItemAtPosition(position).toString());
                consultarIdCategoria();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


    }
    private void consultarListaProductos(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=idCategoria.getText().toString();
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
                        Intent intent=new Intent(ListaProductosActivity.this, CrudProductoActivity.class);
                        intent.putExtra("id_articulo", idProducto.getText().toString());
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

    public void buscar(View view) {
        sonido.start();
        btnBuscar.setVisibility(View.GONE);
        consultarListaProductos();
    }
    private void llenarCategorias(){
        SQLiteDatabase db = conn.getReadableDatabase();
        categoriaLista=new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("select * from subcategoria", null);
        while (cursor.moveToNext()) {
            producto = new Usuarios();
            producto.setId(cursor.getString(0));
            producto.setNombre(cursor.getString(2));
            categoriaLista.add(producto);
        }
        obtenerLista();
        db.close();
    }
    private void obtenerLista(){
        listaCategoria= new ArrayList<String>();
        for(int i=0;i<categoriaLista.size();i++){
            //listaCategoria.add(categoriaLista.get(i).getId()+" - "+categoriaLista.get(i).getNombre());
            listaCategoria.add(categoriaLista.get(i).getNombre());
        }
    }
    private void consultarIdCategoria() {
        String nombre=selecCategoria.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from subcategoria where nombre_subcategoria='" + nombre +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                idCategoria.setText(id);

            }


        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();
    }

    public void actualizar(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaProductosActivity.class);
        startActivity(intent);
    }
}