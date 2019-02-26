package com.example.dowy.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.dowy.foodapp.adapter.CarrinhoAdapter;
import com.example.dowy.foodapp.helper.ConfiguracaoFirebase;
import com.example.dowy.foodapp.model.ItemProduto;
import com.example.dowy.foodapp.model.Pedido;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class CarrinhoActivity extends AppCompatActivity {

    private ItemProduto itemProduto;
    private List<Pedido> listaPedido = new ArrayList<>();
    private RecyclerView recyclerCarrinho;
    private CarrinhoAdapter adapter;
    private FirebaseFirestore firestore;
    private CollectionReference pedidoRef;
    private TextView total;
    private CurrencyEditText cet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Carrinho");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCarrinho.setLayoutManager(layoutManager);
        recyclerCarrinho.setHasFixedSize(true);

        // Configurar Adapter
        adapter = new CarrinhoAdapter(this, listaPedido);
        recyclerCarrinho.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        pedidoRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    //There is an exception, just leave
                    return;
                }
                listaPedido.clear();
                for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                    Pedido pedido = ds.toObject(Pedido.class);
                    pedido.setId(ds.getId());
                    listaPedido.add(pedido);
                    String totalString = cet.formatCurrency(Long.toString(pedido.getTotal()));
                    total.setText(totalString);
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void inicializarComponentes() {

        itemProduto = new ItemProduto();
        recyclerCarrinho = findViewById(R.id.recyclerCarrinho);
        total = findViewById(R.id.carrinhoTotal);
        cet = new CurrencyEditText(this, null);
        Locale locale = new Locale("pt","MZ");
        cet.setLocale(locale);

        //Firebase
        firestore = ConfiguracaoFirebase.getFireStore();
        pedidoRef = firestore.collection("Pedidos");
    }

    public void efectuarPagamento(View view) {
        Toast.makeText(this, "TODO: M-Pesa API", Toast.LENGTH_SHORT).show();
    }
}
