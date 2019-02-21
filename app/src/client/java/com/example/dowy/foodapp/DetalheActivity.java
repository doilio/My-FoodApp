package com.example.dowy.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowy.foodapp.model.Produto;
import com.squareup.picasso.Picasso;

public class DetalheActivity extends AppCompatActivity {

    private Produto produtoSelecionado;
    private ImageView imagem;
    private TextView unidade, preco;
    private NumberPicker nrPicker;

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

        //Number Picker Listener
        nrPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Toast.makeText(DetalheActivity.this, "selected number: "+picker.getValue(), Toast.LENGTH_SHORT).show();
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

        // Valor Minimo e maximo do picker
        nrPicker.setMinValue(1);
        nrPicker.setMaxValue(20);
    }
}
