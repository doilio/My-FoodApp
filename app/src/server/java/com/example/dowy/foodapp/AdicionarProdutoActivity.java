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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.dowy.foodapp.helper.ConfiguracaoFirebase;
import com.example.dowy.foodapp.model.Produto;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class AdicionarProdutoActivity extends AppCompatActivity {

    private ImageView produtoImagem;
    private EditText campoNome;
    private CurrencyEditText campoValor;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageRef;
    private Spinner unidadeSpinner;
    private Produto produtoSelecionado;
    private Button butaoSalvar;

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

        // Carregar dados do spinner
        carregarDadosSpinner();

        produtoSelecionado = (Produto) getIntent().getSerializableExtra("produto_selecionado");
        if (produtoSelecionado != null) {
            toolbar.setTitle("Editar Produto");
            setSupportActionBar(toolbar);
            butaoSalvar.setText("Actualizar Produto");

            //Recuperar dados para exibicao
            campoNome.setText(produtoSelecionado.getNome());
            campoValor.setText(produtoSelecionado.getValor());

            String imageUrl = produtoSelecionado.getUrlImagem();
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(produtoImagem);
                produto.setUrlImagem(imageUrl);

            }
            //TODO recuperar posicao do spinner atraves do texto
        }

    }


    public void validarCampos(View view) {
        String nome = campoNome.getText().toString().trim();
        String valorRaw = String.valueOf(campoValor.getRawValue()); //campoValor.getText().toString().trim();
        String valor = campoValor.getText().toString().trim();
        String unidade = unidadeSpinner.getSelectedItem().toString();

        //if (listaImagem != null && !listaImagem.isEmpty()) {
        if (produto.getUrlImagem() != null && !produto.getUrlImagem().isEmpty()) {
            if (!nome.isEmpty()) {
                if (!valorRaw.isEmpty() && !valorRaw.equals("0")) {
                    if (!unidade.equals("Selecione unidade")) {
                        produto.setValor(valor);
                        produto.setNome(nome);
                        produto.setUnidade(unidade);

                        salvarProduto(produto);

                    } else {
                        Toast.makeText(this, "Escolha unidade", Toast.LENGTH_SHORT).show();
                    }
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

    private void salvarProduto(Produto produto) {

        if (produtoSelecionado != null) {
            // Actualizar produto
            produto.actualizar(produtoSelecionado.getId());
            Toast.makeText(this, "Produto Actualizado", Toast.LENGTH_SHORT).show();

        } else {
            // Salvar produto
            produto.salvar();
            Toast.makeText(this, "Produto Salvo", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void carregarDadosSpinner() {
        String[] unidade = getResources().getStringArray(R.array.unidade);

        // Configurar spinner de estados
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unidade);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unidadeSpinner.setAdapter(adapter);
    }


    private void inicializarComponentes() {
        campoValor = findViewById(R.id.editValorProduto);
        campoNome = findViewById(R.id.editNomeProduto);
        produtoImagem = findViewById(R.id.imageViewProduto);
        unidadeSpinner = findViewById(R.id.unidadeSpinner);
        produto = new Produto();
        butaoSalvar = findViewById(R.id.salvarProduto);

        Locale locale = new Locale("pt", "MZ");
        campoValor.setLocale(locale);

        //Firebase
        storageRef = ConfiguracaoFirebase.getStorage();
    }

    private void loading(String msg) {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(msg)
                .setCancelable(false)
                .build();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap image = null;
        if (requestCode == SELECAO_GALERIA && resultCode == RESULT_OK) {
            try {
                Uri imageLocation = data.getData();

                image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (image != null) {

                //Loading
                loading("salvando imagem");
                produtoImagem.setImageBitmap(image);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] imageData = baos.toByteArray();

                final StorageReference imageRef = storageRef.child("Imagens")
                        .child("Alimentos")
                        .child(System.currentTimeMillis() + "");

                UploadTask uploadTask = imageRef.putBytes(imageData);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String downloadUrl = downloadUri.toString();
                            produto.setUrlImagem(downloadUrl);
                            dialog.dismiss();

                        } else {
                            Toast.makeText(AdicionarProdutoActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
