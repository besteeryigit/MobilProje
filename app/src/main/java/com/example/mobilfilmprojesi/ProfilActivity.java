package com.example.mobilfilmprojesi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Model.Film;
import com.example.mobilfilmprojesi.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ProfilActivity extends AppCompatActivity {
    TextView txtSifreDegistir;
    String userId=" ";
    ImageView imgProfil;
    User user;
    DatabaseReference ref;
    FirebaseDatabase database;
    StorageReference imagePathProfilFoto;
    private String imagelink = "";
    private int Gallery_intent=10;
    StorageReference storage;
    private Uri imageUriProfilFoto;

    TextView tvProfilAdi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        txtSifreDegistir=(TextView)findViewById(R.id.txtSifremiDegistir);
        tvProfilAdi=(TextView)findViewById(R.id.tvProfilAdi);
        tvProfilAdi.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        tvProfilAdi.setText(Common.currentUser.getNameSurname());
        database= FirebaseDatabase.getInstance();
        ref=database.getReference("User");
        storage= FirebaseStorage.getInstance().getReference();

        if(getIntent() != null)
            userId=getIntent().getStringExtra("UserId");
        txtSifreDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });
    }

    public void profilFotoDegistir(View view) {
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,Gallery_intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_intent && resultCode==RESULT_OK)
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgProfil.setImageBitmap(bitmap);
                imageUriProfilFoto = data.getData();
                imagePathProfilFoto=storage.child("User").child(imageUriProfilFoto.getLastPathSegment());

                imagePathProfilFoto.putFile(imageUriProfilFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
                imagePathProfilFoto.putFile(imageUriProfilFoto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imagePathProfilFoto.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            imagelink = downUri.toString();
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void showChangePasswordDialog() {



        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProfilActivity.this);
        alertDialog.setTitle("Şifremi Değiştir");
        alertDialog.setMessage("Tüm bilgileri doldurunuz");
        LayoutInflater inflater=LayoutInflater.from(this);
        View layout_pwd=inflater.inflate(R.layout.change_password_layout,null);
        final MaterialEditText edtPassword=(MaterialEditText)layout_pwd.findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword=(MaterialEditText)layout_pwd.findViewById(R.id.edtNewPassword);
        final MaterialEditText edtRepeatPassword=(MaterialEditText)layout_pwd.findViewById(R.id.edtRepeatPassword);
        alertDialog.setView(layout_pwd);
        alertDialog.setPositiveButton("Değiştir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final android.app.AlertDialog waitingDialog=new SpotsDialog(ProfilActivity.this);
                waitingDialog.show();
                if(edtPassword.getText().toString().equals(Common.currentUser.getPassword()))
                {
                    if(edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString()))
                    {
                       Map<String,Object> passwordUpdate=new HashMap<>();
                        passwordUpdate.put("password",edtNewPassword.getText().toString());
                        DatabaseReference user= FirebaseDatabase.getInstance().getReference("user");
                        user.child(Common.currentUser.getUserName()).updateChildren(passwordUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                Toast.makeText(ProfilActivity.this,"Şifre değiştirildi",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfilActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else
                    {
                        waitingDialog.dismiss();
                        Toast.makeText(ProfilActivity.this,"Yeni parolalar eşleşmiyor.",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {   waitingDialog.dismiss();
                    Toast.makeText(ProfilActivity.this,"Eski Yanlış Şifre",Toast.LENGTH_SHORT).show();
                }

            }
        });

    alertDialog.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });
        alertDialog.show();
    }



}
