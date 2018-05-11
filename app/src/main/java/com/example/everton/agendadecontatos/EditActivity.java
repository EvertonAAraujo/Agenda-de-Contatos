package com.example.everton.agendadecontatos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private Contato contato;

    private View layout;

    private ImageButton foto;
    private EditText nome;
    private EditText sobrenome;
    private EditText email;
    private EditText endereco;
    private EditText aniversario;
    private EditText telefone;

    private Button salvar;

    private final int CAMERA = 1;
    private final int GALERIA = 2;

    private final String IMAGE_DIR = "/fotosContatos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        contato = getIntent().getParcelableExtra("contato");

        layout = findViewById(R.id.mainLayout);

        foto = findViewById(R.id.imagemContato);
        nome = findViewById(R.id.nomeContato);
        sobrenome = findViewById(R.id.sobrenomeContato);
        telefone = findViewById(R.id.telefoneContato);
        endereco = findViewById(R.id.endereçoContato);
        email = findViewById(R.id.emailContato);
        aniversario = findViewById(R.id.aniversarioContato);

        nome.setText(contato.getNome());
        sobrenome.setText(contato.getSobrenome());
        telefone.setText(contato.getTelefone());
        endereco.setText(contato.getEndereco());
        email.setText(contato.getEmail());
        aniversario.setText(contato.getAniversario());

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaImagem();
            }
        });

        //Quando contato tem foto e abrir formulario a foto é carregada automaticamente
        File imgFile = new File(contato.getFoto());
        if(imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        salvar = findViewById(R.id.botaoSalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contato.setNome(nome.getText().toString());
                contato.setSobrenome(sobrenome.getText().toString());
                contato.setTelefone(telefone.getText().toString());
                contato.setEndereco(endereco.getText().toString());
                contato.setEmail(email.getText().toString());
                contato.setAniversario(aniversario.getText().toString());

                if(contato.getNome().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário preencher o nome", Toast.LENGTH_SHORT).show();
                    return;
                } else if(contato.getTelefone().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário preencher o telefone", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("contato", contato);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private void alertaImagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Origem da Foto");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clicaTirarFoto();
            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clicaCarregaImagem();
            }
        });

        builder.create().show();
    }

    private void clicaTirarFoto() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            requesCameraPermission();
        } else {
            showCamera();
        }
    }

    private void requesCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {//Mostra mensagem de permissão ao usuário

            Snackbar.make(layout,"É necessário permitir o uso da câmera",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA);//requestCode, se foi permitido
                }
            }).show();
        } else { // Se não precisar informar o usuário
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA);//requestCode, se foi permitido
        }
    }

    private void showCamera() { //Mostra a camera
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);
    }

    private void clicaCarregaImagem() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requesGaleriaPermission();
        } else {
            showGaleria();
        }
    }

    private void requesGaleriaPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {//Mostra mensagem de permissão ao usuário

            Snackbar.make(layout,"É necessário permitir o uso dos arquivos",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALERIA);//requestCode, se foi permitido
                }
            }).show();
        } else { // Se não precisar informar ao usuário
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GALERIA);//requestCode, se foi permitido
        }
    }

    private void showGaleria() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //Vai pegar uma imagem
        startActivityForResult(i, GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clicaTirarFoto();
                }
                break;

            case GALERIA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clicaCarregaImagem();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED || data == null) { //Se não retornar nada não salva arquivo
            return;
        }
        if(requestCode == GALERIA) {
            Uri contentURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);//Pega a imagem da galeria e transforma em bitmap
                contato.setFoto(saveImage(bitmap));//Quando salva ele retorna o local da imagem e é colocado no contato
                foto.setImageBitmap(bitmap);//Pega a foto e seta o bitmap

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            contato.setFoto(saveImage(bitmap));
            foto.setImageBitmap(bitmap);

        }
    }

    private String saveImage(Bitmap bitmap) { //Local que foto fica salva / Criação da pasta das fotos
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes); //Diminui a qualidade em 50% economizar espaço
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR); //Diretorio para salvar imagem, pegando caminho da img

        if(!directory.exists()) {//Se o diretorio não exitir, é criado
            directory.mkdirs();
        }

        try {
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);//informa a galeria que existe uma nova foto
            fo.close();
            return f.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
