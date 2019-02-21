package com.example.dowy.foodapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dowy.foodapp.R;
import com.example.dowy.foodapp.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder> {

    private Context context;
    private List<Produto> listaProdutos;

    public ProdutoAdapter(Context context, List<Produto> listaProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
    }

    public List<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagem;
        private TextView unidade;
        private TextView preco;
        private TextView nome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.item_produto_imagem);
            unidade = itemView.findViewById(R.id.item_produto_unidade);
            preco = itemView.findViewById(R.id.item_produto_preco);
            nome = itemView.findViewById(R.id.item_produto_nome);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_produto2, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Produto produtoActual = listaProdutos.get(i);

        Uri photoUri = Uri.parse(produtoActual.getUrlImagem());

        if (photoUri != null) {
            Picasso.get().load(photoUri).into(viewHolder.imagem);
        }
        viewHolder.nome.setText(produtoActual.getNome());
        viewHolder.preco.setText(produtoActual.getValor());
        viewHolder.unidade.setText(produtoActual.getUnidade());

    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }


}
