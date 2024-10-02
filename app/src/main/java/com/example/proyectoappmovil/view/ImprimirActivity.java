package com.example.proyectoappmovil.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;
import com.example.proyectoappmovil.util.Utilidades;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ImprimirActivity extends AppCompatActivity implements View.OnClickListener{
    public ImageView boleta;
    public TextView RutEmpresa, Fecha, FechaBoleta, idEmpresa, txtIdComuna, sucursal, idUsuario, txtFolio;
    public Button imprimirBoleta;
    public TextView razonSocial, direccion, comuna, fono, folio, montoTotal, Iva, ciudad, Total, VUnitario;
    SunmiPrinterService sunmiPrinterService;
    Usuarios usuario = null;
    ConexionSQLiteHelper conn;
    private int ultimoAnio, ultimoMes, ultimoDiaDelMes;
    MediaPlayer sonido;
    InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            sunmiPrinterService = service;
        }

        @Override
        protected void onDisconnected() {

        }
    };

    InnerResultCallback innerResultCallbcak = new InnerResultCallback() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {

        }
        @Override
        public void onReturnString(String result) throws RemoteException {

        }
        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {

        }
        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {

        }
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        sucursal = (TextView) findViewById(R.id.txt_sucursales);
        txtIdComuna = (TextView) findViewById(R.id.txt_comunaS);
        Total = (TextView) findViewById(R.id.txt_total);
        txtFolio=findViewById(R.id.txt_id_folio);
        VUnitario = (TextView) findViewById(R.id.valor_unitario);
        ciudad = (TextView) findViewById(R.id.txt_ciudad);
        Iva = (TextView) findViewById(R.id.txt_iva);
        folio = (TextView) findViewById(R.id.txt_folio);
        montoTotal = (TextView) findViewById(R.id.txt_montoTotal);
        sonido=MediaPlayer.create(this, R.raw.click);
        razonSocial = (TextView) findViewById(R.id.txt_razon);
        direccion = (TextView) findViewById(R.id.txt_direccion);
        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        comuna = (TextView) findViewById(R.id.txt_comuna);
        fono = (TextView) findViewById(R.id.txt_fono);
        fono.setText(date);
        imprimirBoleta = (Button) findViewById(R.id.btn_imprimir);
        RutEmpresa = (TextView) findViewById(R.id.rut_empresa);
        Fecha = (TextView) findViewById(R.id.fecha);
        boleta = (ImageView) findViewById(R.id.img_boleta);
        FechaBoleta = (TextView) findViewById(R.id.txt_fecha_boleta);
        final Calendar calendario = Calendar.getInstance();
        ultimoAnio = calendario.get(Calendar.YEAR);
        ultimoMes = calendario.get(Calendar.MONTH);
        ultimoDiaDelMes = calendario.get(Calendar.DAY_OF_MONTH);
        idEmpresa = (TextView) findViewById(R.id.id_empresa);
        idUsuario=findViewById(R.id.txt_id_usuario);
        imprimirBoleta.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            String info = bundle.getString("id_usuario");
            String info2 = bundle.getString("id_folio");
            idUsuario.setText(info);
            txtFolio.setText(info2);

        }

        try {
            InnerPrinterManager.getInstance().bindService(getApplicationContext(), innerPrinterCallback);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }

        consultarEmpresa();
        consultarComuna();
        consultarCiudad();
        consultarFolio();
        Handler handler2 = new Handler();
        Handler handler = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                ivaTotal();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        imprimir();

                    }
                }, 1000);

            }
        }, 500);

        refrescarFechaEnEditText();

    }


    @Override
    public void onClick(View view) {
        if (view == imprimirBoleta) {
            sonido.start();
            imprimir();

        }
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
        String fecha = String.format(Locale.getDefault(), "%02d-%02d-%02d", ultimoDiaDelMes, ultimoMes + 1, ultimoAnio);

        // La ponemos en el editText
        FechaBoleta.setText(fecha);
    }


    public void imprimir() {
        try {
            boleta.buildDrawingCache();
            Bitmap bmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.codigoqr2);
            sunmiPrinterService.setAlignment(1, innerResultCallbcak);
            sunmiPrinterService.printText(razonSocial.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("R.U.T.:" + RutEmpresa.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.setAlignment(0, innerResultCallbcak);
            sunmiPrinterService.printText("CIUDAD: " + ciudad.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("COMUNA: " + comuna.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText(direccion.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("--------------------------------", innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("BOLETA ELECTRONICA NRO.: " + folio.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("FECHA: " + FechaBoleta.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("HORA: " + fono.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("--------------------------------", innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("ARTICULO P.UNITARIO  CANT. TOTAL", innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("________________________________", innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("Producto  " + VUnitario.getText().toString() + "      1.0   " + montoTotal.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("________________________________", innerResultCallbcak);
            sunmiPrinterService.printText("\n",innerResultCallbcak);
            sunmiPrinterService.printText("El IVA de esta boleta es $" + Iva.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.setFontSize(48, innerResultCallbcak);
            sunmiPrinterService.printText("Total:" + montoTotal.getText().toString(), innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printBitmap(bmap, innerResultCallbcak);
            // sunmiPrinterService.printBitmap(bitmap, innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.setAlignment(1, innerResultCallbcak);
            sunmiPrinterService.setFontSize(24, innerResultCallbcak);
            sunmiPrinterService.printText("Timbre Electrónico SII", innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("Documento generado por", innerResultCallbcak);
            sunmiPrinterService.printText("\n", innerResultCallbcak);
            sunmiPrinterService.printText("www.appexpress.cl", innerResultCallbcak);
            sunmiPrinterService.printText("\n" + "\n" + "\n" + "\n", innerResultCallbcak);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(ImprimirActivity.this, "Imprimiendo", Toast.LENGTH_SHORT).show();
    }

    public void ivaTotal() {
        if (montoTotal.getText().toString().equals("")) {
            Toast.makeText(ImprimirActivity.this,"Falta monto",Toast.LENGTH_SHORT).show();
            return;
        }
        String precio = montoTotal.getText().toString();
        int cad = Integer.parseInt(precio);
        double cant = 1.19;
        double multi = 0.19;
        double neto = cad / cant;
        int ntotal = (int) neto;
        double iva = neto * multi;
        //para redondear un decimal match.round
        Iva.setText(String.valueOf(Math.round(iva)));

    }

    private void consultarEmpresa() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_EMPRESA, null);
        while (cursor.moveToNext()) {
            usuario = new Usuarios();
            usuario.setId(cursor.getString(1));
            usuario.setNombre(cursor.getString(2));
            usuario.setRut(cursor.getString(3));
            usuario.setDirrecion(cursor.getString(6));

        }
        idEmpresa.setText(usuario.getId());
        razonSocial.setText(usuario.getNombre());
        RutEmpresa.setText(usuario.getRut());
        direccion.setText(usuario.getDirrecion());
        db.close();
    }
    private void consultarComuna() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String idComuna=idEmpresa.getText().toString();
        Cursor cursor = db.rawQuery("select * from comuna where id_comuna="+idComuna,null);
        while (cursor.moveToNext()) {
            usuario = new Usuarios();
            usuario.setId(cursor.getString(1));
            usuario.setNombre(cursor.getString(2));

        }
        txtIdComuna.setText(usuario.getId());
        comuna.setText(usuario.getNombre());
        db.close();
    }
    private void consultarCiudad() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String idCiudad=txtIdComuna.getText().toString();
        Cursor cursor = db.rawQuery("select * from ciudad where id_ciudad="+idCiudad,null);
        while (cursor.moveToNext()) {
            usuario = new Usuarios();
            usuario.setNombre(cursor.getString(1));

        }
        ciudad.setText(usuario.getNombre());
        db.close();
    }

    private void consultarFolio() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String idFolio=txtFolio.getText().toString();
        Cursor cursor = db.rawQuery("select * from folio where id_folio="+idFolio, null);
        while (cursor.moveToNext()) {
            usuario = new Usuarios();
            usuario.setCantidad(cursor.getString(0));
            usuario.setDetalle(cursor.getString(2));
            usuario.setFecha(cursor.getString(3));

        }
        folio.setText(usuario.getCantidad());
        montoTotal.setText(usuario.getDetalle());
        Total.setText(usuario.getDetalle());
        VUnitario.setText(usuario.getDetalle());
        db.close();
        return;

    }
    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, ListaCategoriaActivity.class);
        intent.putExtra("id_usuario", idUsuario.getText().toString());
        startActivity(intent);
    }

}