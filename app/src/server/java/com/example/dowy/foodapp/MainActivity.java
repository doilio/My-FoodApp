package com.example.dowy.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Configuracoes Iniciais
        configuracoesIniciais();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_inserir_produto:
                //Toast.makeText(this, "Inserir Produtos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdicionarProdutoActivity.class));
                break;
            case R.id.menu_listar_produto:

                Toast.makeText(this, "Listar Pedidos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_sair:

                Toast.makeText(this, "Sair", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void configuracoesIniciais() {

    }

}
