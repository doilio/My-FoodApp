package com.example.dowy.foodapp.model;

import com.example.dowy.foodapp.helper.ConfiguracaoFirebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;

public class Produto implements Serializable {

    private String id;
    private String nome;
    private long valor;
    private String unidade;
    private String urlImagem;

    public Produto() {
    }

    public void salvar() {

        FirebaseFirestore firebaseFirestore = ConfiguracaoFirebase.getFireStore();
        CollectionReference produtoRef = firebaseFirestore.collection("Produtos");
        produtoRef.add(this);
    }

    public void actualizar(String id) {
        FirebaseFirestore firebaseFirestore = ConfiguracaoFirebase.getFireStore();
        CollectionReference produtoRef = firebaseFirestore.collection("Produtos");
        DocumentReference documentRef = produtoRef.document(id);
        documentRef.set(this, SetOptions.merge());
    }

    public void remover() {
        FirebaseFirestore firebaseFirestore = ConfiguracaoFirebase.getFireStore();
        CollectionReference produtoRef = firebaseFirestore.collection("Produtos");
        DocumentReference documentRef = produtoRef.document(getId());

        documentRef.delete();
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }
}
