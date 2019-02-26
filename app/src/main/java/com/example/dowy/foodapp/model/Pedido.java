package com.example.dowy.foodapp.model;

import com.example.dowy.foodapp.helper.ConfiguracaoFirebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Pedido {

    private String id;
    private List<ItemProduto> produtos;
    private long total;

    public Pedido() {
    }

    public void salvar() {
        FirebaseFirestore firestore = ConfiguracaoFirebase.getFireStore();
        CollectionReference pedido = firestore.collection("Pedidos");
        pedido.add(this);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemProduto> produtos) {
        this.produtos = produtos;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


}
