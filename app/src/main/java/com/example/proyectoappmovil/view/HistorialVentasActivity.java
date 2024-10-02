package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.view.adapters.VentaAdaptar;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HistorialVentasActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Usuarios venta=null;
    VentaAdaptar adapter;
    RecyclerView recyclerVenta;
    TextView idProducto, idUsuario, txtNombreProducto, fechaUno, fechaDos, txtCantidad, txtTotal;
    MediaPlayer sonido;
    ArrayList<Usuarios> listaVenta = new ArrayList<>();

    Button btnBuscar;
    private int ultimoAnio, ultimoMes, ultimoDiaDelMes;
    private int ultimoAnio2, ultimoMes2, ultimoDiaDelMes2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ventas);
        txtCantidad=findViewById(R.id.txt_cantidad);
        txtTotal=findViewById(R.id.txt_total);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"IpPos.db",null,1);
        recyclerVenta=findViewById(R.id.idRecyclerImagen);
        sonido=MediaPlayer.create(this, R.raw.click);
        fechaUno=findViewById(R.id.txt_fechaUno);
        fechaDos=findViewById(R.id.txt_fechaDos);
        idProducto=findViewById(R.id.txt_id_categoria);
        idUsuario=findViewById(R.id.txt_id_usuario);
        final Calendar calendario = Calendar.getInstance();
        ultimoAnio = calendario.get(Calendar.YEAR);
        ultimoMes = calendario.get(Calendar.MONTH);
        ultimoDiaDelMes = calendario.get(Calendar.DAY_OF_MONTH);
        btnBuscar=findViewById(R.id.btn_buscar);
        final Calendar calendarioDos = Calendar.getInstance();
        ultimoAnio2 = calendarioDos.get(Calendar.YEAR);
        ultimoMes2 = calendarioDos.get(Calendar.MONTH);
        ultimoDiaDelMes2 = calendarioDos.get(Calendar.DAY_OF_MONTH);
        listaVenta=new ArrayList<>();
        adapter=new VentaAdaptar(listaVenta);
        StaggeredGridLayoutManager lmStaggeredHorizontal = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerVenta.setLayoutManager(lmStaggeredHorizontal);
        recyclerVenta.setHasFixedSize(true);
        recyclerVenta.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refrescarFechaEnEditText();
        refrescarFechaEnEditTextDos();

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
    private DatePickerDialog.OnDateSetListener listenerDeDatePickerDos = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int anio, int mes, int diaDelMes) {
            // Esto se llama cuando seleccionan una fecha. Nos pasa la vista, pero más importante, nos pasa:
            // El año, el mes y el día del mes. Es lo que necesitamos para saber la fecha completa


            // Refrescamos las globales
            ultimoAnio2 = anio;
            ultimoMes2 = mes;
            ultimoDiaDelMes2 = diaDelMes;

            // Y refrescamos la fecha
            refrescarFechaEnEditTextDos();

        }
    };
    public void refrescarFechaEnEditText() {
        // Formateamos la fecha pero podríamos hacer cualquier otra cosa ;)
        String fecha = String.format(Locale.getDefault(), "%02d-%02d-%02d",ultimoDiaDelMes, ultimoMes+1,ultimoAnio );

        // La ponemos en el editTex
        fechaUno.setText(fecha);
    }
    public void refrescarFechaEnEditTextDos() {
        // Formateamos la fecha pero podríamos hacer cualquier otra cosa ;)
        String fecha2 = String.format(Locale.getDefault(), "%02d-%02d-%02d",ultimoDiaDelMes2, ultimoMes2+1,ultimoAnio2 );

        // La ponemos en el editText
        fechaDos.setText(fecha2);
    }
    private void consultarListaVentas(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String fecha1=fechaUno.getText().toString();
        String fecha2=fechaDos.getText().toString();
        String pendiente="0";

        try {
            Cursor cursor=db.rawQuery("select * from ventas where pago='" + pendiente + "' and fecha between'" +
                    fecha1 + "' and '" + fecha2 + "'",null);
            while (cursor.moveToNext()) {

                venta = new Usuarios();
                venta.setId(cursor.getString(0));
                venta.setDirrecion(cursor.getString(1));
                venta.setApellido(cursor.getString(2));
                venta.setPrecio(cursor.getString(3));
                venta.setCantidad(cursor.getString(4));
                venta.setFecha(cursor.getString(5));
                venta.setDetalle(cursor.getString(6));
                idProducto.setText(venta.getDirrecion());
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
                            idUsuario.setText(listaVenta.get
                                            (recyclerVenta.getChildAdapterPosition(view))
                                    .getId());

                            Toast.makeText(getApplicationContext(),
                                    listaVenta.get
                                                    (recyclerVenta.getChildAdapterPosition(view))
                                            .getDato(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        } catch(Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        db.close();
    }
    private void contador(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String fecha1=fechaUno.getText().toString();
        String fecha2=fechaDos.getText().toString();
        Cursor cursor=db.rawQuery("select count(id_venta) from ventas where fecha between'" +
                fecha1 + "' and '" + fecha2 + "'",null);
        while (cursor.moveToNext()){
            venta=new Usuarios();
            venta.setCantidad(cursor.getString(0));
            txtCantidad.setText(venta.getCantidad());

        }
        db.close();
    }
    private void sumador(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String fecha1=fechaUno.getText().toString();
        String fecha2=fechaDos.getText().toString();
        Cursor cursor=db.rawQuery("select sum(valor_venta) from ventas where fecha between'" +
                fecha1 + "' and '" + fecha2 + "'",null);
        while (cursor.moveToNext()){
            venta=new Usuarios();
            venta.setDetalle(cursor.getString(0));
            txtTotal.setText(venta.getDetalle());

        }
        db.close();

    }
    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent= new Intent(HistorialVentasActivity.this, MenuPrincipalActivity.class);
        startActivity(intent);
    }

    public void fechaDesde(View view) {
        sonido.start();
        DatePickerDialog dialogoFecha = new DatePickerDialog(this, listenerDeDatePicker, ultimoAnio, ultimoMes, ultimoDiaDelMes);
        //Mostrar
        //calendario fecha actual

        // Refrescar la fecha en el EditText
        refrescarFechaEnEditText();
        dialogoFecha.show();
    }

    public void fechaHasta(View view) {
        sonido.start();
        DatePickerDialog dialogoFecha = new DatePickerDialog(this, listenerDeDatePickerDos, ultimoAnio2, ultimoMes2, ultimoDiaDelMes2);
        //Mostrar
        //calendario fecha actual

        // Refrescar la fecha en el EditText
        refrescarFechaEnEditTextDos();
        dialogoFecha.show();
    }

    public void buscar(View view) {
        sonido.start();
        btnBuscar.setVisibility(View.GONE);
        consultarListaVentas();
        contador();
        sumador();
    }


    public void actualizar(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, HistorialVentasActivity.class);
        startActivity(intent);
    }
}