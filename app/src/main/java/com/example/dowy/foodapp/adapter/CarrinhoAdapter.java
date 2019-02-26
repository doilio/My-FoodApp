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

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.dowy.foodapp.R;
import com.example.dowy.foodapp.model.ItemProduto;
import com.example.dowy.foodapp.model.Pedido;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder> {

    private Context context;
    private List<Pedido> listaPedido;

    public CarrinhoAdapter(Context context, List<Pedido> listaPedido) {
        this.context = context;
        this.listaPedido = listaPedido;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagem;
        TextView nome, preco, valor, quantidade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.imageCarrinho);//
            nome = itemView.findViewById(R.id.nome_produto_carrinho);//
            preco = itemView.findViewById(R.id.total_item_carrinho);
            valor = itemView.findViewById(R.id.preco_carrinho);
            quantidade = itemView.findViewById(R.id.quantidade_carrinho);//
        }
    }

    @NonNull
    @Override
    public CarrinhoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_carrinho, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoAdapter.MyViewHolder myViewHolder, int i) {

        Pedido pedidoActual = listaPedido.get(i);

        List<ItemProduto> listaItemProduto = pedidoActual.getProdutos();

        CurrencyEditText cet = new CurrencyEditText(context,null);
        Locale locale = new Locale("pt","MZ");
        cet.setLocale(locale);
        long total = 0 ;
        for (ItemProduto currentItem: listaItemProduto){
            Uri photoUri = Uri.parse(currentItem.getUrlImagem());
            if (photoUri != null){
                Picasso.get().load(photoUri).into(myViewHolder.imagem);
            }
            myViewHolder.nome.setText(currentItem.getNomeProduto());
            myViewHolder.quantidade.setText(String.valueOf(currentItem.getQuantidade()));


            String preco = cet.formatCurrency(Long.toString(currentItem.getPreco()));
            myViewHolder.preco.setText(preco);
            String valor = cet.formatCurrency(Long.toString(currentItem.getValor()));
            myViewHolder.valor.setText(valor);

            // Calculo do Total a pagar
            total+= currentItem.getPreco();
        }
        //String totalExibicao = cet.formatCurrency(Long.toString(total));
        //myViewHolder.valor.setText(totalExibicao);
        pedidoActual.setTotal(total);

    }

    @Override
    public int getItemCount() {
        return listaPedido.size();
    }

}
