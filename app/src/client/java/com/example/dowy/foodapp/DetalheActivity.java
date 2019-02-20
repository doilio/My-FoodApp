package com.example.dowy.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dowy.foodapp.model.Produto;
import com.squareup.picasso.Picasso;

public class DetalheActivity extends AppCompatActivity {

    private Produto produtoSelecionado;
    private ImageView imagem;
    private TextView unidade, preco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.scrollingToolbar);
        toolbar.setTitle("Detalhes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();

        produtoSelecionado = (Produto) getIntent().getSerializableExtra("produto_selecionado");
        if (produtoSelecionado != null) {
            toolbar.setTitle(produtoSelecionado.getNome());
            setSupportActionBar(toolbar);

            String urlImagem = produtoSelecionado.getUrlImagem();
            if (urlImagem != null) {
                Picasso.get().load(urlImagem).into(imagem);
            }
            unidade.setText(produtoSelecionado.getUnidade());
            preco.setText(produtoSelecionado.getValor());
        }
    }

    private void inicializarComponentes() {
        produtoSelecionado = new Produto();
        imagem = findViewById(R.id.imagem_detalhe);
        unidade = findViewById(R.id.unidade_detalhe);
        preco = findViewById(R.id.valor_detalhe);
    }
}
