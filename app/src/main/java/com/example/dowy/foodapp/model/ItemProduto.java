package com.example.dowy.foodapp.model;

import java.io.Serializable;

public class ItemProduto implements Serializable {

    private String idProduto;
    private String nomeProduto;
    private String urlImagem;
    private long valor;
    private long preco;
    private int quantidade;

    public ItemProduto() {
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public long getPreco() {
        return preco;
    }

    public void setPreco(long preco) {
        this.preco = preco;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
