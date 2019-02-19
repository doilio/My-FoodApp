package com.example.dowy.foodapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.dowy.foodapp.helper.ConfiguracaoFirebase;
import com.example.dowy.foodapp.model.Produto;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class AdicionarProdutoActivity extends AppCompatActivity {

    private ImageView produtoImagem;
    private EditText campoNome;
    private CurrencyEditText campoValor;
    private static final int SELECAO_GALERIA = 200;
    private List<String> listaImagem = new ArrayList<>();
    private StorageReference storageRef;
    private StorageReference alimentosRef;

    private Produto produto;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_produto);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.inserir_produto);
        setSupportActionBar(toolbar);

        // Configuracoes Iniciais
        inicializarComponentes();

        // Tratar evento de clique da Galeria
        produtoImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
    }

    public void validarCampos(View view) {
        String nome = campoNome.getText().toString().trim();
        String valorRaw = String.valueOf(campoValor.getRawValue());//campoValor.getText().toString().trim();
        String valor = campoValor.getText().toString().trim();

        if (listaImagem != null && !listaImagem.isEmpty()) {
            if (!nome.isEmpty()) {
                if (!valorRaw.isEmpty() && !valorRaw.equals("0")) {
                    produto.setValor(valor);
                    produto.setNome(nome);

                    salvarAnuncio();
                } else {
                    Toast.makeText(this, "Insira valor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Insira Nome", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Selecione uma foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarAnuncio() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando Anuncio")
                .setCancelable(false)
                .build();
        dialog.show();
        // Garantir que imagem seja salva

        String urlImagem = listaImagem.get(0);
        salvarFotoStorage(urlImagem);
    }

    private void salvarFotoStorage(String urlImagem) {

        //Criar NO no storage
        storageRef = ConfiguracaoFirebase.getStorage();
        alimentosRef = storageRef.child("Imagens")
                .child("Alimentos")
                .child(System.currentTimeMillis() + "");

        //Fazer upload do arquivo
        Uri fotoUri = Uri.parse(urlImagem);
        UploadTask uploadTask = alimentosRef.putFile(fotoUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return alimentosRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String downloadUrl = downloadUri.toString();
                    produto.setUrlImagem(downloadUrl);
                    produto.salvar();

                    dialog.dismiss();
                    finish();
                    Toast.makeText(AdicionarProdutoActivity.this, "Produto Salvo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdicionarProdutoActivity.this, "Erro ao carregar Imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void inicializarComponentes() {
        campoValor = findViewById(R.id.editValorProduto);
        campoNome = findViewById(R.id.editNomeProduto);
        produtoImagem = findViewById(R.id.imageViewProduto);
        produto = new Produto();


        Locale locale = new Locale("pt", "MZ");
        campoValor.setLocale(locale);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configurar imagem no imageview
            Picasso.get().load(imagemSelecionada).into(produtoImagem);

            listaImagem.add(caminhoImagem);
        }
    }
}
