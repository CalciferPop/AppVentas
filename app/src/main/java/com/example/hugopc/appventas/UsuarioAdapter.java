package com.example.hugopc.appventas;

import android.content.Context;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuariosHolder> {

    private List<Usuario> UsuariosList;
    Context context;

    public static class UsuariosHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvNickname;
        FloatingActionButton btnDelete;
        FloatingActionButton btnUpdate;

        public UsuariosHolder(View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.txtNickname);
            btnDelete = itemView.findViewById(R.id.btnDeleteUsuario);
            btnUpdate  = itemView.findViewById(R.id.btnUpdateUsuario);
        }
    }

    public UsuarioAdapter(List<Usuario> List, Context con) {
        UsuariosList = List;
        context = con;
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_usuario, parent, false);
        UsuarioAdapter.UsuariosHolder UsuariosHolder = new UsuarioAdapter.UsuariosHolder(view);
        return UsuariosHolder;
    }

    @Override
    public void onBindViewHolder(UsuariosHolder holder, int position) {
        final Usuario Usuario = UsuariosList.get(position);
        holder.tvNickname.setText(Usuario.getnickname());
    }

    @Override
    public int getItemCount() {

        return UsuariosList.size();
    }
}