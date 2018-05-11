package com.example.everton.agendadecontatos;

import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable{

    private Long id = -1L;
    private String nome = "";
    private String sobrenome = "";
    private String email = "";
    private String telefone = "";
    private String endereco = "";
    private String aniversario = "";
    private String foto = "";

    Contato() {

    }

    private Contato(Parcel in) {
        String[] data = new String[8];
        in.readStringArray(data);
        setNome(data[0]);
        setSobrenome(data[1]);
        setAniversario(data[2]);
        setEmail(data[3]);
        setEndereco(data[4]);
        setFoto(data[5]);
        setTelefone(data[6]);
        setId(Long.parseLong(data[7]));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getAniversario() {
        return aniversario;
    }

    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
            getNome(), getSobrenome(), getAniversario(), getEmail(), getEndereco(), getFoto(), getTelefone(), String.valueOf(getId())
        });
    }

    public static final Parcelable.Creator<Contato> CREATOR = new Parcelable.Creator<Contato>(){

        @Override
        public Contato createFromParcel(Parcel source) {
            return new Contato(source);
        }

        @Override
        public Contato[] newArray(int size) {
            return new Contato[size];
        }
    };
}
