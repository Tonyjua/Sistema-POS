package com.example.proyectoappmovil.view;

import static android.Manifest.permission.CAMERA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.ConexionSQLiteHelper;
import com.example.proyectoappmovil.model.Usuarios;
import com.example.proyectoappmovil.util.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RegistrarProductoActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText nombreProducto, detalleProducto, precioProducto, stockProducto;
    TextView selectUser, selectProducto, idProducto;
    Spinner categoriaSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<String> listaCategoria;
    ArrayList<Usuarios> categoriaLista;
    MediaPlayer sonido;
    private String path;
    private final int PHOTO_CODE = 200;
    private static final int COD_SELECCIONADO = 10;
    private static final int REQUEST_PERMISION_CAMERA = 101;
    Bitmap bitmap;
    ImageView imgFoto;
    Usuarios categoria=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "IpPos.db", null, 1);
        nombreProducto=findViewById(R.id.txt_nombre_producto);
        detalleProducto=findViewById(R.id.txt_detalle);
        selectProducto=findViewById(R.id.select_producto);
        idProducto=findViewById(R.id.id_producto);
        precioProducto=findViewById(R.id.txt_precio);
        stockProducto=findViewById(R.id.txt_stock);
        categoriaSpinner=findViewById(R.id.btnSpinner);
        selectUser=findViewById(R.id.select_user);
        imgFoto=findViewById(R.id.id_foto);
        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.marcodefoto);
        imgFoto.setImageBitmap(bitmap);
        sonido=MediaPlayer.create(this, R.raw.click);
        llenarSubcategorias();
        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCategoria);
        categoriaSpinner.setAdapter(adapter);
        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectProducto.setText(parent.getItemAtPosition(position).toString());
                consultarIdSubcategoria();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

    }
    public void registrarProducto(){
        SQLiteDatabase bd=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID_SUBCATEGORIA, idProducto.getText().toString());
        values.put(Utilidades.CAMPO_NOMBRE_ARTICULO, nombreProducto.getText().toString());
        values.put(Utilidades.CAMPO_DETALLE_ARTICULO, detalleProducto.getText().toString());
        values.put(Utilidades.CAMPO_PRECIO, precioProducto.getText().toString());
        values.put(Utilidades.CAMPO_STOCK, stockProducto.getText().toString());
        values.put(Utilidades.CAMPO_IMAGEN,  Utilidades.getBytes(bitmap));
        Long idResultado=bd.insert(Utilidades.TABLA_ARTICULO,Utilidades.CAMPO_ID_ARTICULO, values);
        Toast.makeText(getApplicationContext(),"Se registró: "+idResultado,Toast.LENGTH_SHORT).show();
        nombreProducto.setText("");
        detalleProducto.setText("");
        precioProducto.setText("");
        stockProducto.setText("");
       // imgFoto.setImageBitmap(null);
        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.marcodefoto);
        imgFoto.setImageBitmap(bitmap);
        bd.close();
    }

    public void registrar(View view) {
        sonido.start();
        String inNombre = nombreProducto.getText().toString();
        String inDetalle = detalleProducto.getText().toString();
        String inPrecio = precioProducto.getText().toString();
        String inStock = stockProducto.getText().toString();
        if(TextUtils.isEmpty(inNombre)){
            nombreProducto.setError(getString(R.string.error_ingrese_nombre));
            nombreProducto.requestFocus();
            Toast.makeText(RegistrarProductoActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(inDetalle)){
            detalleProducto.setError(getString(R.string.error_ingrese_detalle));
            detalleProducto.requestFocus();
            Toast.makeText(RegistrarProductoActivity.this,"Ingrese una clave",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(inPrecio)){
            precioProducto.setError(getString(R.string.error_ingrese_nombre));
            precioProducto.requestFocus();
            Toast.makeText(RegistrarProductoActivity.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(inStock)){
            stockProducto.setError(getString(R.string.error_ingrese_detalle));
            stockProducto.requestFocus();
            Toast.makeText(RegistrarProductoActivity.this,"Ingrese una clave",Toast.LENGTH_SHORT).show();
            return;

        }
        registrarProducto();

    }


    public void volverpantalla(View view) {
        sonido.start();
        finish();
        Intent intent=new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }



    private void llenarSubcategorias(){
        SQLiteDatabase db = conn.getReadableDatabase();
        categoriaLista=new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("select * from subcategoria", null);
            while (cursor.moveToNext()) {
                categoria = new Usuarios();
                categoria.setId(cursor.getString(0));
                categoria.setNombre(cursor.getString(2));
                categoriaLista.add(categoria);
            }
            obtenerLista();
        db.close();
    }
    private void obtenerLista(){
        listaCategoria= new ArrayList<String>();
        for(int i=0;i<categoriaLista.size();i++){
            listaCategoria.add(categoriaLista.get(i).getNombre());
        }
    }
    private void consultarIdSubcategoria() {
        String nombre=selectProducto.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from subcategoria where nombre_subcategoria='" + nombre +"'", null);
        try {
            if (cursor.moveToFirst()) {
                //capturamos los valores del cursos y lo almacenamos en variable
                String id = cursor.getString(0);
                idProducto.setText(id);

            }


        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
        db.close();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Path",""+path);
                                }
                            });
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgFoto.setImageBitmap(bitmap);
                    break;
                case COD_SELECCIONADO:
                    Uri miPath=data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imgFoto.setImageURI(miPath);
                    Cursor cursor = getApplicationContext().getContentResolver().query(miPath, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),miPath);
                        imgFoto.setImageBitmap(bitmap);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    cursor.close();
                    break;

            }
            bitmap=redimensionarImagen(bitmap,600,800);
        }else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(RegistrarProductoActivity.this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }
    private void openCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, PHOTO_CODE);
        }
    }
    private void showDialogOption() {
        final CharSequence[] opciones = {"Tomar foto","Elegir de Galeria","Cancelar"};
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, (dialogInterface, i) -> {
            if(opciones[i].equals("Tomar foto")){
                //Check sdk version
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(RegistrarProductoActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED){
                        openCamara();
                    }else{
                        ActivityCompat.requestPermissions(RegistrarProductoActivity.this, new String[]{CAMERA}, REQUEST_PERMISION_CAMERA);
                    }
                }else{
                    openCamara();
                }
            }
            if (opciones[i].equals("Elegir de Galeria")){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONADO);
            }else{
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            openCamara();
        }else{
            Toast.makeText(this, "Necesita habilitar los permisos", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }


    public void sacarFoto(View view) {
        sonido.start();
        showDialogOption();
    }
}