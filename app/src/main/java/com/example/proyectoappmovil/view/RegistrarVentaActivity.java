package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;
import com.example.proyectoappmovil.util.Utilidades;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

public class RegistrarVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    TextView nombreProducto, detalleProducto, precioProducto, stockProducto, txtMulti,txtFolio;
    TextView txtFecha, idProducto, idSubcategoria, idCategoria, txtCantidad, txtResultado, idUsuario;
    Bitmap bitmap;
    ImageView imgFoto;
    Usuarios categoria=null;
    private int ultimoAnio, ultimoMes, ultimoDiaDelMes;
    int mes;
    int suma=1;
    MediaPlayer sonido;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreProducto=findViewById(R.id.txt_nombre_producto);
        detalleProducto=findViewById(R.id.txt_detalle);
        txtCantidad=findViewById(R.id.txt_cantidad);
        txtFolio=findViewById(R.id.txt_folio);
        txtResultado=findViewById(R.id.txt_resultado);
        txtMulti=findViewById(R.id.txt_multiplicado);
        idProducto=findViewById(R.id.txt_id_producto);
        sonido=MediaPlayer.create(this, R.raw.click);
        txtFecha=findViewById(R.id.txt_fecha);
        idCategoria=findViewById(R.id.txt_id_categoria);
        idSubcategoria=findViewById(R.id.txt_id_subcategoria);
        idUsuario=findViewById(R.id.txt_id_usuario);
        precioProducto=findViewById(R.id.txt_precio);
        stockProducto=findViewById(R.id.txt_stock);
        imgFoto=findViewById(R.id.id_foto);
        final Calendar calendario = Calendar.getInstance();
        ultimoAnio = calendario.get(Calendar.YEAR);
        ultimoMes = calendario.get(Calendar.MONTH);
        ultimoDiaDelMes = calendario.get(Calendar.DAY_OF_MONTH);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_articulo");
            String info2 = bundle.getString("id_subcategoria");
            String info3 = bundle.getString("id_categoria");
            String info4 = bundle.getString("id_usuario");
            idProducto.setText(info);
            idSubcategoria.setText(info2);
            idCategoria.setText(info3);
            idUsuario.setText(info4);
        }
        consultaArticulo();
        refrescarFechaEnEditText();
        consultaFolio();
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
    private void consultaArticulo(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=idProducto.getText().toString();
        Cursor cursor=db.rawQuery("select * from articulo where id_articulo='"+id+ "'",null);
        while (cursor.moveToNext()){
            categoria=new Usuarios();
            categoria.setNombre(cursor.getString(2));
            categoria.setDetalle(cursor.getString(3));
            categoria.setImagen(cursor.getBlob(4));
            categoria.setPrecio(cursor.getString(5));
            categoria.setStock(cursor.getString(6));
            nombreProducto.setText(categoria.getNombre());
            detalleProducto.setText(categoria.getDetalle());
            imgFoto.setImageBitmap(categoria.getImagen());
            precioProducto.setText(categoria.getPrecio());
            stockProducto.setText(categoria.getStock());

        }
        db.close();

    }
    public void registrarVenta(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        String pendiente="1";
        values.put(Utilidades.CAMPO_ID_ARTICULO, idProducto.getText().toString());
        values.put(Utilidades.CAMPO_ID_USUARIO, idUsuario.getText().toString());
        values.put(Utilidades.CAMPO_ID_FOLIO, txtFolio.getText().toString());
        values.put(Utilidades.CAMPO_VALOR_VENTA, txtMulti.getText().toString());
        values.put(Utilidades.CAMPO_CANTIDAD, txtCantidad.getText().toString());
        values.put(Utilidades.CAMPO_FECHA_VENTA, txtFecha.getText().toString());
        values.put(Utilidades.CAMPO_PENDIENTE_PAGO, pendiente);
        Long idResultado=bd.insert(Utilidades.TABLA_VENTAS,Utilidades.CAMPO_ID_VENTAS, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void volver(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaProductoVentaActivity.class);
        intent.putExtra("id_subcategoria", idSubcategoria.getText().toString());
        intent.putExtra("id_categoria", idCategoria.getText().toString());
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }

    public void registrar(View view) {
        sonido.start();
        if (txtCantidad.getText().toString().equals("")) {
            Toast.makeText(RegistrarVentaActivity.this,"Ingrese un valor",Toast.LENGTH_SHORT).show();
            return;
        }
        modificarArticulo();
        registrarVenta();
        finish();
        Toast.makeText(RegistrarVentaActivity.this,"Producto agregado al carro",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, ListaCategoriaActivity.class);
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }
    private void modificarArticulo(){
        formula();
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametro = {idProducto.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_STOCK, txtResultado.getText().toString());
        db.update(Utilidades.TABLA_ARTICULO, values,Utilidades.CAMPO_ID_ARTICULO+"=?",parametro);
        db.close();
    }
    public void formula(){
        BigDecimal precio = new BigDecimal(precioProducto.getText().toString());
        BigDecimal stock = new BigDecimal(stockProducto.getText().toString());
        BigDecimal cantidad = new BigDecimal(txtCantidad.getText().toString());
        BigDecimal totalResta = new BigDecimal(stock.subtract(cantidad).toString());
        BigDecimal resultMulti = precio.multiply(cantidad);
        txtMulti.setText(String.valueOf(resultMulti));
        txtResultado.setText(String.valueOf(totalResta));
    }
    private void consultaFolio(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=idProducto.getText().toString();
        Cursor cursor=db.rawQuery("select * from folio",null);
        while (cursor.moveToNext()){
            categoria=new Usuarios();
            categoria.setId(cursor.getString(0));
            txtFolio.setText(categoria.getId());

        }
        db.close();

    }
}