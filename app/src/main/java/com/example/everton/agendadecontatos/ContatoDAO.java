package com.example.everton.agendadecontatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContatoDAO extends SQLiteOpenHelper{

    private static final int VERSAO = 1;
    private final String TABELA = "Contatos";
    private static final String DATABASE = "DadosAgenda";

    public ContatoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    //Cria a tabela no banco
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "sobrenome TEXT, "
                + "email TEXT, "
                + "endereco TEXT, "
                + "telefone TEXT NOT NULL, "
                + "aniversario TEXT, "
                + "foto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Contato> getLista(String order) {
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + " ORDER BY nome " +
        order + ";", null);

        while(cursor.moveToNext()) {
            Contato c = new Contato();

            c.setId(cursor.getLong(cursor.getColumnIndex("id")));
            c.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            c.setSobrenome(cursor.getString(cursor.getColumnIndex("sobrenome")));
            c.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            c.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            c.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            c.setAniversario(cursor.getString(cursor.getColumnIndex("aniversario")));
            c.setFoto(cursor.getString(cursor.getColumnIndex("foto")));

            contatos.add(c);
        }

        cursor.close();

        return contatos;
    }

    public void inserirContato(Contato c) {
        ContentValues values = new ContentValues(); //Setar valores no banco de dados

        values.put("nome", c.getNome());
        values.put("sobrenome", c.getSobrenome());
        values.put("telefone", c.getTelefone());
        values.put("endereco", c.getEmail());
        values.put("email", c.getEmail());
        values.put("aniversario", c.getAniversario());
        values.put("foto", c.getFoto());

        getWritableDatabase().insert(TABELA, null, values);//Valores salvos no banco
    }

    public void alteraContato(Contato c) {
        ContentValues values = new ContentValues(); //Setar valores no banco de dados

        values.put("id", c.getId());
        values.put("nome", c.getNome());
        values.put("sobrenome", c.getSobrenome());
        values.put("telefone", c.getTelefone());
        values.put("endereco", c.getEmail());
        values.put("email", c.getEmail());
        values.put("aniversario", c.getAniversario());
        values.put("foto", c.getFoto());

        String[] idParaSerAlterado = {c.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", idParaSerAlterado);
    }

    public void apagarContato(Contato c) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {c.getId().toString()};
        db.delete(TABELA, "id=?", args);
    }
}
