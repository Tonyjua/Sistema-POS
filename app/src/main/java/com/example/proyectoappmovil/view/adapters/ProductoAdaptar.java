package com.example.proyectoappmovil.view.adapters;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoappmovil.R;
import com.example.proyectoappmovil.model.Usuarios;

import java.util.List;

public class ProductoAdaptar extends RecyclerView.Adapter<ProductoAdaptar.ProductosHolder> implements View.OnClickListener {
    List<Usuarios> listaUsuarios;
    private View.OnClickListener listener;
    public ProductoAdaptar(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public ProductoAdaptar.ProductosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.galeria_list_producto,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new ProductoAdaptar.ProductosHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProductoAdaptar.ProductosHolder holder, int position) {
        holder.txtNombre.setText(listaUsuarios.get(position).getNombre().toString());
        holder.txtDetalle.setText(listaUsuarios.get(position).getDetalle().toString());
        holder.txtPrecio.setText(listaUsuarios.get(position).getPrecio().toString());
        holder.txtStock.setText(listaUsuarios.get(position).getStock().toString());
        if (listaUsuarios.get(position).getImagen()!=null){
            holder.imagen.setImageBitmap(listaUsuarios.get(position).getImagen());
        }else{
            holder.imagen.setImageResource(R.drawable.marcodefoto);
        }

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null) {
            listener.onClick(view);
        }
    }

    public class ProductosHolder extends RecyclerView.ViewHolder{

        TextView txtNombre,txtDetalle,txtPrecio,txtStock;
        ImageView imagen;

        public ProductosHolder(View itemView) {
            super(itemView);
            txtDetalle= itemView.findViewById(R.id.txt_detalle_producto);
            txtNombre= itemView.findViewById(R.id.txt_nombre_producto);
            txtPrecio= itemView.findViewById(R.id.txt_precio_producto);
            txtStock= itemView.findViewById(R.id.txt_stock_producto);
            imagen= itemView.findViewById(R.id.id_imagen);


        }
    }
    private static DiffUtil.ItemCallback<Usuarios> DIFF_CALLBACK = new DiffUtil.ItemCallback<Usuarios>() {
        @Override
        public boolean areItemsTheSame(Usuarios usuario, Usuarios neUsuario) {


            return usuario.getImagen().toString()== neUsuario.getImagen().toString();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(Usuarios usuario, Usuarios neUsuario) {
            return usuario.equals(neUsuario);
        }
    };

    public void swapItems(List< Usuarios > todolist){
        this.listaUsuarios = todolist;
        notifyDataSetChanged();
    }
}
