package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.view.adapters.VentaAdaptar;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;
import com.example.proyectoappmovil.util.Utilidades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ListaVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios venta=null;
    VentaAdaptar adapter;
    RecyclerView recyclerVenta;
    TextView idProducto, idUsuario, txtFecha, idEmpresa, txtSuma, idVenta, txtFolio;
    MediaPlayer sonido;
    ArrayList<Usuarios> listaVenta = new ArrayList<>();
    private int ultimoAnio, ultimoMes, ultimoDiaDelMes;
    Button btnVolver;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_venta);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerVenta=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        txtFolio=findViewById(R.id.txt_folio);
        txtFecha=findViewById(R.id.txt_fecha);
        idVenta=findViewById(R.id.txt_id_venta);
        idEmpresa=findViewById(R.id.txt_id_empresa);
        txtSuma=findViewById(R.id.txt_suma);
        idProducto=findViewById(R.id.txt_id_categoria);
        idUsuario=findViewById(R.id.txt_id_usuario);
        btnVolver=findViewById(R.id.boton_volver);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_usuario");
            idUsuario.setText(info);

        }
        final Calendar calendario = Calendar.getInstance();
        ultimoAnio = calendario.get(Calendar.YEAR);
        ultimoMes = calendario.get(Calendar.MONTH);
        ultimoDiaDelMes = calendario.get(Calendar.DAY_OF_MONTH);
        listaVenta=new ArrayList<>();
        adapter=new VentaAdaptar(listaVenta);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerVenta.setLayoutManager(lmStaggeredHorizontal);
        recyclerVenta.setHasFixedSize(true);
        recyclerVenta.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        consultarListaVentas();
        consultarEmpresa();
        refrescarFechaEnEditText();


    }
    private DatePickerDialog.OnDateSetListener listenerDeDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int anio, int mes, int diaDelMes) {
            // Esto se llama cuando seleccionan una fecha. Nos pasa la vista, pero más importante, nos pasa:
            // El año, el mes y el día del mes. Es lo que necesitamos para saber la fecha completa


            // Refrescamos las globales
            ultimoAnio = anio;
            ultimoMes = mes;
            ultimoDiaDelMes = diaDelMes;

            // Y refrescamos la fecha
            refrescarFechaEnEditText();

        }
    };
    public void refrescarFechaEnEditText() {
        // Formateamos la fecha pero podríamos hacer cualquier otra cosa ;)
        String fecha = String.format(Locale.getDefault(), "%02d-%02d-%02d",ultimoDiaDelMes, ultimoMes+1,ultimoAnio );

        // La ponemos en el editTex
        txtFecha.setText(fecha);
    }
    private void consultarListaVentas(){

        //metodo para consultar la lista de categorias en la bd sqlite y mostrarla en un recyclerview
        SQLiteDatabase db=conn.getReadableDatabase();
        String pagoPendiente="1";

        try {
            //"SELECT Productos.Codigo, Productos.Descripcion, Productos.Precio_Venta FROM Productos LEFT JOIN Productos_Relacionados ON Productos.Codigo = Productos_Relacionados.Codigo_Relacionado
         // Cursor cursor=db.rawQuery("SELECT v.id_articulo, v.valor_venta, v.cantidad, v.fecha, a.imagen_articulo FROM ventas v JOIN articulo a ON v.id_venta = v.id_articulo",null);
            Cursor cursor=db.rawQuery("select * from ventas where pago="+pagoPendiente,null);
            while (cursor.moveToNext()) {

                venta = new Usuarios();
                venta.setId(cursor.getString(0));
                venta.setDirrecion(cursor.getString(1));
                venta.setApellido(cursor.getString(2));
                venta.setDato(cursor.getString(3));
                venta.setPrecio(cursor.getString(4));
                venta.setCantidad(cursor.getString(5));
                venta.setFecha(cursor.getString(6));
                venta.setDetalle(cursor.getString(7));
                idProducto.setText(venta.getDirrecion());
                txtFolio.setText(venta.getDato());
                String idArticulo = idProducto.getText().toString();
                Cursor cursorDos = db.rawQuery("select * from articulo where id_articulo=" + idArticulo, null);
                while (cursorDos.moveToNext()) {
                    venta.setDato(cursorDos.getString(2));

                    listaVenta.add(venta);
                    adapter = new VentaAdaptar(listaVenta);
                    recyclerVenta.setAdapter(adapter);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sonido.start();
                            idVenta.setText(listaVenta.get
                                            (recyclerVenta.getChildAdapterPosition(view))
                                    .getId());
                            finish();
                            Intent intent = new Intent(ListaVentaActivity.this, CrudVentaActivity.class);
                            intent.putExtra("id_venta", idVenta.getText().toString());
                            intent.putExtra("id_usuario", idUsuario.getText().toString());
                            startActivity(intent);


                        }
                    });


                }
            }
        } catch(Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        db.close();
    }
    private void modificarVenta(){
        String pendiente="1";
        String cambio="0";
        SQLiteDatabase db=conn.getReadableDatabase();

        Cursor cursor=db.rawQuery("update ventas set pago='"
                +cambio+"' where pago='"+pendiente+ "'",null);
        while (cursor.moveToNext()){
            venta=new Usuarios();
            venta.setId(cursor.getString(0));


        }
        db.close();
    }
    private void modificarFolio(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametro = {txtFolio.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_VALOR_FOLIO, txtSuma.getText().toString());
        values.put(Utilidades.CAMPO_FECHA_FOLIO, txtFecha.getText().toString());
        values.put(Utilidades.CAMPO_ID_EMPRESA, idEmpresa.getText().toString());
        db.update(Utilidades.TABLA_FOLIO, values,Utilidades.CAMPO_ID_FOLIO+"=?",parametro);
        db.close();

    }
    public void registrarfolio(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID_EMPRESA, idEmpresa.getText().toString());
        Long idResultado=bd.insert(Utilidades.TABLA_FOLIO,Utilidades.CAMPO_ID_FOLIO, values);
       // Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        bd.close();
    }

    private void sumador(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String pendiente="1";
        Cursor cursor=db.rawQuery("select sum(valor_venta) from ventas where pago='" +
                pendiente +"'",null);
        while (cursor.moveToNext()){
            venta=new Usuarios();
            venta.setDetalle(cursor.getString(0));

        }
        txtSuma.setText(venta.getDetalle());
        db.close();

    }
    private void consultarEmpresa() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_EMPRESA, null);
        while (cursor.moveToNext()) {
            venta = new Usuarios();
            venta.setPassword(cursor.getString(0));

        }
        idEmpresa.setText(venta.getPassword());
    }

    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaCategoriaActivity.class);
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }
    public void registrarCompra(){
        sumador();
        modificarVenta();
        sonido.start();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                modificarFolio();
                registrarfolio();
                finish();
                Intent intent=new Intent(ListaVentaActivity.this, ImprimirActivity.class);
                intent.putExtra("id_usuario", idUsuario.getText().toString());
                intent.putExtra("id_folio", txtFolio.getText().toString());
                startActivity(intent);

            }
        }, 500);
    }
    public void confirmarCompra(View view) {
        sonido.start();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ListaVentaActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("Confirmación de pago");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                registrarCompra();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
}