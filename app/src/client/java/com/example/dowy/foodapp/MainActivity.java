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
import android.view.View;
import android.widget.AdapterView;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos = new ArrayList<>();
    private FirebaseFirestore firestore;
    private CollectionReference produtoRef;

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
                        Produto produtoSelecionado = listaProdutos.get(position);
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

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.recyclerProdutos);

        // Firebase
        firestore = ConfiguracaoFirebase.getFireStore();
        produtoRef = firestore.collection("Produtos");
    }
}
