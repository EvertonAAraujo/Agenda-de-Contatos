package com.example.everton.agendadecontatos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContactViewHolder>{

    private List<Contato> listaContatos;

    ContatoAdapter(List<Contato> lista) {
        listaContatos = lista;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celular_contato, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contato c = listaContatos.get(position);
        holder.nome.setText(c.getNome());
        holder.sobrenome.setText(c.getSobrenome());
        holder.telefone.setText(c.getTelefone());

        File imgFile = new File(c.getFoto());
        if(imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nome;
        TextView sobrenome;
        TextView telefone;

        ContactViewHolder(View v) {
            super(v);
            foto = v.findViewById(R.id.imagemContato);
            nome = v.findViewById(R.id.textoNome);
            sobrenome = v.findViewById(R.id.textoSobrenome);
            telefone = v.findViewById(R.id.textoTelefone);

        }
    }
}
