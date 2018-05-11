package com.example.everton.agendadecontatos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContatoDAO helper;

    private RecyclerView contatosRecycle;
    private ContatoAdapter adapter;

    private List<Contato> listaContatos;

    private final int RESQUEST_NEW = 1;
    private final int REQUEST_ALTER =2;

    private String order = "ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("contato", new Contato());
                startActivityForResult(i, RESQUEST_NEW);
            }
        });

        helper = new ContatoDAO(this);
        listaContatos = helper.getLista(order);

        contatosRecycle = findViewById(R.id.contatosRecycle);
        contatosRecycle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        contatosRecycle.setLayoutManager(llm);

        adapter = new ContatoAdapter(listaContatos);
        contatosRecycle.setAdapter(adapter);

        //Quando um item for clicado ele tras o item e a posição
        contatosRecycle.addOnItemTouchListener(new RecycleItemClickListener(getApplicationContext(),
                new RecycleItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        abrirOpcoes(listaContatos.get(position));
                    }
                }));
    }

    private void abrirOpcoes(final Contato contato) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contato.getNome());
        builder.setItems(new CharSequence[]{"Ligar", "Editar", "Excluir"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel: " + contato.getTelefone()));
                                startActivity(intent);
                                break;

                            case 1:
                                Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
                                intent1.putExtra("contato", contato);
                                startActivityForResult(intent1, REQUEST_ALTER);
                                break;

                            case 2:
                                listaContatos.remove(contato);
                                helper.apagarContato(contato);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESQUEST_NEW && resultCode == RESULT_OK) { //Criar contato
            Contato contato = data.getParcelableExtra("contato");//Pega o contato recebido
            helper.inserirContato(contato);
            listaContatos = helper.getLista(order); //Carrega a lista de contatos
            adapter = new ContatoAdapter(listaContatos);
            contatosRecycle.setAdapter(adapter);

        } else if(requestCode == REQUEST_ALTER && resultCode == RESULT_OK) { //Alterar contato
            Contato contato = data.getParcelableExtra("contato");
            helper.alteraContato(contato);
            listaContatos = helper.getLista(order);
            adapter = new ContatoAdapter(listaContatos);
            contatosRecycle.setAdapter(adapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order_az) {
            order = "ASC";
        } else if (id == R.id.order_za) {
            order = "DESC";
        }

        listaContatos = helper.getLista(order);
        adapter = new ContatoAdapter(listaContatos);
        contatosRecycle.setAdapter(adapter);

        return true;
    }
}
