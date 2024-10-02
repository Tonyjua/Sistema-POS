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

public class UsuarioAdaptar extends RecyclerView.Adapter<UsuarioAdaptar.UsuariosHolder> implements View.OnClickListener {
    List<Usuarios> listaUsuarios;

    private View.OnClickListener listener;
    public UsuarioAdaptar(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public UsuarioAdaptar.UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.galeria_list_user,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new UsuarioAdaptar.UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(UsuarioAdaptar.UsuariosHolder holder, int position) {
        holder.txtId.setText(listaUsuarios.get(position).getId().toString());
        holder.txtNombre.setText(listaUsuarios.get(position).getNombre().toString());
        holder.txtTipoUser.setText(listaUsuarios.get(position).getTipoUser().toString());
        if (listaUsuarios.get(position).getImagen()!=null){
            holder.imagen.setImageBitmap(listaUsuarios.get(position).getImagen());
        }else{
            holder.imagen.setImageResource(R.drawable.iconousuario);
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

    public class UsuariosHolder extends RecyclerView.ViewHolder{

        TextView txtId,txtNombre, txtTipoUser;
        ImageView imagen;

        public UsuariosHolder(View itemView) {
            super(itemView);
            txtId= itemView.findViewById(R.id.txt_id);
            txtNombre= itemView.findViewById(R.id.txt_nombre);
            txtTipoUser= itemView.findViewById(R.id.txt_tipo_user);
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