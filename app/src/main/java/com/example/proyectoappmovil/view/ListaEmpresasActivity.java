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
import com.example.proyectoappmovil.view.adapters.EmpresaAdaptar;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;

public class ListaEmpresasActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios empresa=null;
    EmpresaAdaptar adapter;
    RecyclerView recyclerEmpresa;
    TextView idEmpresa, idUsuario;
    MediaPlayer sonido;
    ArrayList<Usuarios> listaEmpresa = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empresas);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerEmpresa=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        idEmpresa=findViewById(R.id.txt_id_empresa);
        idUsuario=findViewById(R.id.txt_id_usuario);
        listaEmpresa=new ArrayList<>();
        adapter=new EmpresaAdaptar(listaEmpresa);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerEmpresa.setLayoutManager(lmStaggeredHorizontal);
        recyclerEmpresa.setHasFixedSize(true);
        recyclerEmpresa.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        consultarListaEmpresa();
    }
    private void consultarListaEmpresa(){
        SQLiteDatabase db=conn.getReadableDatabase();

        try {
            Cursor cursor=db.rawQuery("select * from empresa",null);
            while (cursor.moveToNext()){

                empresa=new Usuarios();
                empresa.setId(cursor.getString(0));
                empresa.setNombre(cursor.getString(2));
                empresa.setRut(cursor.getString(3));
                listaEmpresa.add(empresa);
                adapter=new EmpresaAdaptar(listaEmpresa);
                recyclerEmpresa.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sonido.start();
                        idEmpresa.setText(listaEmpresa.get
                                        (recyclerEmpresa.getChildAdapterPosition(view))
                                .getId());

                        Toast.makeText(getApplicationContext(),
                                listaEmpresa.get
                                                (recyclerEmpresa.getChildAdapterPosition(view))
                                        .getNombre(),Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent=new Intent(ListaEmpresasActivity.this, CrudEmpresaActivity.class);
                        intent.putExtra("id_empresa", idEmpresa.getText().toString());
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