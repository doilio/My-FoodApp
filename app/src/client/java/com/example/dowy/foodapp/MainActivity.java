package com.example.dowy.foodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dowy.foodapp.adapter.ProdutoAdapter;
import com.example.dowy.foodapp.helper.ConfiguracaoFirebase;
import com.example.dowy.foodapp.helper.RecyclerItemClickListener;
import com.example.dowy.foodapp.model.Produto;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos = new ArrayList<>();
    private FirebaseFirestore firestore;
    private CollectionReference produtoRef;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Configuracoes Iniciais
        inicializarComponentes();

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Configurar Adapter
        adapter = new ProdutoAdapter(this, listaProdutos);
        recyclerView.setAdapter(adapter);

        // Eventos de Clique na recyclerView
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Aqui estou a usar duas Listas, logo este metodo tem que servir para ambas
                        List<Produto> listaProdutosActualizada = adapter.getListaProdutos();
                        //Produto produtoSelecionado = listaProdutos.get(position);
                        Produto produtoSelecionado = listaProdutosActualizada.get(position);
                        Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                        i.putExtra("produto_selecionado", produtoSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                // Senao ao fecharmos a searchView ele vai mostrar a 2a lista
                recarregarLista();
            }
        });

        // Configurar Listener para o Search Box
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    procurarProduto(newText.toLowerCase());
                }
                return true;
            }
        });

    }

    private void recarregarLista() {
        adapter = new ProdutoAdapter(this, listaProdutos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void procurarProduto(String newText) {
        List<Produto> listaProdutoPesquisa = new ArrayList<>();
        for (Produto produto : listaProdutos) {

            if (produto.getNome() != null) {
                String nome = produto.getNome().toLowerCase();

                if (nome.contains(newText)) {
                    listaProdutoPesquisa.add(produto);
                }
            }
        }
        adapter = new ProdutoAdapter(this, listaProdutoPesquisa);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu);

        // Configurar Botao de Pesquisa
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Usuario Logado
        //menu.setGroupVisible(R.id.group_logado, true);

        // Usuario Deslogado
        //menu.setGroupVisible(R.id.group_deslogado,true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:
                break;
            case R.id.cart:
                abrirCarrinho();
                break;
            case R.id.signIn:
                Toast.makeText(this, "Sign in", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOut:
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Insert loader aqui
        produtoRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // There's an exception... LEAVE
                    return;
                }
                listaProdutos.clear();
                for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                    Produto produto = ds.toObject(Produto.class);
                    produto.setId(ds.getId());
                    listaProdutos.add(produto);
                }
                adapter.notifyDataSetChanged();

            }

            // dismiss loader aqui
        });

    }

    private void abrirCarrinho() {
        Intent i = new Intent(this, CarrinhoActivity.class);
        startActivity(i);
    }

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.recyclerProdutos);
        searchView = findViewById(R.id.materialSearchPrincipal);

        // Firebase
        firestore = ConfiguracaoFirebase.getFireStore();
        produtoRef = firestore.collection("Produtos");
    }
}
