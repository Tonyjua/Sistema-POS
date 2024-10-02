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

public class VentaAdaptar extends RecyclerView.Adapter<VentaAdaptar.VentasHolder> implements View.OnClickListener {
    List<Usuarios> listaUsuarios;

    private View.OnClickListener listener;
    public VentaAdaptar(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public VentaAdaptar.VentasHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.galeria_list_venta,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new VentaAdaptar.VentasHolder(vista);
    }

    @Override
    public void onBindViewHolder(VentaAdaptar.VentasHolder holder, int position) {
        holder.txtProductor.setText(listaUsuarios.get(position).getDato().toString());
        holder.txtCantidad.setText(listaUsuarios.get(position).getCantidad().toString());
        holder.txtValor.setText(listaUsuarios.get(position).getPrecio().toString());
        holder.txtFecha.setText(listaUsuarios.get(position).getFecha().toString());
        if (listaUsuarios.get(position).getImagen()!=null){
            holder.imagen.setImageBitmap(listaUsuarios.get(position).getImagen());
        }else{
            holder.imagen.setImageResource(R.drawable.iconboleta);
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

    public class VentasHolder extends RecyclerView.ViewHolder{

        TextView txtProductor,txtCantidad, txtValor, txtFecha;
        ImageView imagen;

        public VentasHolder(View itemView) {
            super(itemView);

            txtProductor= itemView.findViewById(R.id.txt_producto);
            txtCantidad= itemView.findViewById(R.id.txt_cantidad);
            txtValor= itemView.findViewById(R.id.txt_valor);
            txtFecha= itemView.findViewById(R.id.txt_fecha);
            imagen= itemView.findViewById(R.id.idImagen);


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
