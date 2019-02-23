package com.example.dowy.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.dowy.foodapp.model.Produto;
import com.squareup.picasso.Picasso;

public class DetalheActivity extends AppCompatActivity {

    private Produto produtoSelecionado;
    private ImageView imagem;
    private TextView unidade, preco;
    private NumberPicker nrPicker;
    private TextView precoProd;
    private TextView qtdEscolhida;
    private TextView valorTotal;
    private String precoProduto;
    private CurrencyEditText cet;

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


            String valorFormatado = cet.formatCurrency(produtoSelecionado.getValor());
            preco.setText(valorFormatado);
        }

        //Number Picker Listener
        nrPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                qtdEscolhida.setText(newVal);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detalhe, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.share:
                Toast.makeText(this, "TODO: implement Sharing", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void inicializarComponentes() {
        produtoSelecionado = new Produto();
        imagem = findViewById(R.id.imagem_detalhe);
        unidade = findViewById(R.id.unidade_detalhe);
        preco = findViewById(R.id.valor_detalhe);
        nrPicker = findViewById(R.id.numberPicker);


        cet = new CurrencyEditText(this, null);

        precoProd = findViewById(R.id.quantidadeEscolhida);
        qtdEscolhida = findViewById(R.id.valor_detalheProd);
        valorTotal = findViewById(R.id.valorTotalProduto);

        // Valor Minimo e maximo do picker
        nrPicker.setMinValue(1);
        nrPicker.setMaxValue(20);
    }

    public void adicionarACarrinho(View view) {
        Toast.makeText(this, "TODO: Criar Lista de Itens num carrinho", Toast.LENGTH_SHORT).show();
    }
}
