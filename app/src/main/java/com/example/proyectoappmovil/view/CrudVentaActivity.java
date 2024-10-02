package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;
import com.example.proyectoappmovil.util.Utilidades;

import java.math.BigDecimal;

public class CrudVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;

    TextView txtPrecio, txtIdProducto, txtIdVenta, idUsuario, txtStock, txtMulti, txtCantidad;
    MediaPlayer sonido;
    Usuarios venta=null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_venta);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        txtIdVenta=findViewById(R.id.txt_id_venta);
        txtIdProducto=findViewById(R.id.txt_id_producto);
        txtPrecio=findViewById(R.id.txt_precio);
        txtCantidad=findViewById(R.id.txt_camtidad);
        idUsuario=findViewById(R.id.txt_id_usuario);
        txtStock=findViewById(R.id.txt_stock);
        txtMulti=findViewById(R.id.txt_multiplo);
        sonido=MediaPlayer.create(this, R.raw.click);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_usuario");
            String info2 = bundle.getString("id_venta");
            idUsuario.setText(info);
            txtIdVenta.setText(info2);
        }
        consultaVenta();
        consultaProducto();
        Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    formula();

                }
            }, 500);


    }
    private void consultaVenta(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=txtIdVenta.getText().toString();
        Cursor cursor=db.rawQuery("select * from ventas where id_venta='"+id+ "'",null);
        while (cursor.moveToNext()){
            venta=new Usuarios();
            venta.setDato(cursor.getString(1));
            venta.setPrecio(cursor.getString(3));
            venta.setCantidad(cursor.getString(4));
            txtIdProducto.setText(venta.getDato());
            txtPrecio.setText(venta.getPrecio());
            txtCantidad.setText(venta.getCantidad());
        }
        db.close();

    }
    private void consultaProducto(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String id=txtIdProducto.getText().toString();
        Cursor cursor=db.rawQuery("select * from articulo where id_articulo='"+id+ "'",null);
        while (cursor.moveToNext()){
            venta=new Usuarios();
            venta.setDetalle(cursor.getString(6));
            txtStock.setText(venta.getDetalle());
        }
        db.close();
    }
    private void modificarVenta(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametro = {txtIdVenta.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_VALOR_VENTA, txtMulti.getText().toString());
        values.put(Utilidades.CAMPO_CANTIDAD, txtCantidad.getText().toString());
        db.update(Utilidades.TABLA_VENTAS, values,Utilidades.CAMPO_ID_VENTAS+"=?",parametro);
        db.close();

    }
    private void modificarPorducto(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametro = {txtIdProducto.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_STOCK, txtMulti.getText().toString());
        db.update(Utilidades.TABLA_ARTICULO, values,Utilidades.CAMPO_ID_ARTICULO+"=?",parametro);
        db.close();

    }
    public void formula(){
        BigDecimal cantidadOriginal = new BigDecimal(txtStock.getText().toString());
        BigDecimal cantidad = new BigDecimal(txtCantidad.getText().toString());
        BigDecimal sum = new BigDecimal(cantidadOriginal.add(cantidad).toString());
        String numCadena = String.valueOf(sum);
        txtMulti.setText(numCadena);
    }
    private void eliminarVenta(){
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametro = {txtIdVenta.getText().toString()};
        db.delete(Utilidades.TABLA_VENTAS,Utilidades.CAMPO_ID_VENTAS+"=?",parametro);
        db.close();
        txtIdVenta.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtIdProducto.setText("");
    }
    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaVentaActivity.class);
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }

    public void actualizar(View view) {
        sonido.start();
        String cantidad = txtCantidad.getText().toString();
        if(TextUtils.isEmpty(cantidad)){
            txtCantidad.setError(getString(R.string.error_ingrese_cantidad));
            txtCantidad.requestFocus();
            Toast.makeText(CrudVentaActivity.this,"Ingrese una cantidad",Toast.LENGTH_SHORT).show();
            return;
        }
       // formula();
       // modificarVenta();
        Toast.makeText(CrudVentaActivity.this,"Venta modificada",Toast.LENGTH_SHORT).show();

    }

    public void eliminar(View view) {
        sonido.start();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(CrudVentaActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Seguro que desea eliminar la venta "+txtIdVenta.getText().toString()+"?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
    public void aceptar() {
        String cantidad = txtCantidad.getText().toString();
        if(TextUtils.isEmpty(cantidad)){
            txtCantidad.setError(getString(R.string.error_ingrese_cantidad));
            txtCantidad.requestFocus();
            Toast.makeText(CrudVentaActivity.this,"Ingrese una cantidad",Toast.LENGTH_SHORT).show();
            return;
        }
        modificarPorducto();
        eliminarVenta();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(CrudVentaActivity.this);
        dialogo1.setCancelable(false);
        final AlertDialog.Builder confirmar = dialogo1.setPositiveButton("Venta eliminada", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
}