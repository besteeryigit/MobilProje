package com.example.mobilfilmprojesi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Model.Film;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class FilmEkleActivity extends AppCompatActivity {

    ImageButton imgKapakFoto,imgFoto1,imgFoto2,imgFoto3;
    EditText edtAd,edtAcikla,edtId,edtLink,edtTeknik,edtTur;
    Button btnFilmiKaydet;
    FirebaseDatabase database;
    StorageReference imagePath,imgFoto1Path,imgFoto2Path,imgFoto3Path;
    private int Gallery_intent=2;
    private int Gallery_fotointent1=3;
    private int Gallery_fotointent2=4;
    private int Gallery_fotointent3=5;
    DatabaseReference ref;
    StorageReference storage;
    private Uri imageUri,imageUri1,imageUri2,imageUri3;
    private Film film;
    private String imagelink = "";
    private String imagelink1 = "";
    private String a;
    private String imagelink2 = "";
    private String imagelink3 = "";
    private String ad, aciklama,fragmanLink, teknikBilgi,filmMenuId,tur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_ekle);
        imgKapakFoto=(ImageButton) findViewById(R.id.imgKapakFoto);
        imgFoto1=(ImageButton)findViewById(R.id.imgFoto1);
        imgFoto2=(ImageButton)findViewById(R.id.imgFoto2);
        imgFoto3=(ImageButton)findViewById(R.id.imgFoto3);
        edtAd=(EditText)findViewById(R.id.edtAd);
        edtAcikla=(EditText)findViewById(R.id.edtAcikla);
        edtId=(EditText)findViewById(R.id.edtId);
        edtLink=(EditText)findViewById(R.id.edtLink);
        edtTeknik=(EditText)findViewById(R.id.edtTeknik);
        edtTur=(EditText)findViewById(R.id.edtTur);
        btnFilmiKaydet=(Button)findViewById(R.id.btnFilmiKaydett);

        database= FirebaseDatabase.getInstance();
        ref=database.getReference("Film");
        film=new Film();
        storage=FirebaseStorage.getInstance().getReference();


    }

   /* private void getValues()
    {
        film.setAd(edtAd.getText().toString());
        film.setAciklama(edtAcikla.getText().toString());
        film.setFilmMenuId(edtId.getText().toString());
        film.setFragmanLink(edtLink.getText().toString());
        film.setTeknikBilgi(edtTeknik.getText().toString());
        film.setTur(edtTur.getText().toString());
        film.setFotograf(imagelink.toString());
        film.setFoto1(imagelink1.toString());
        film.setFoto2(imagelink2.toString());
        film.setFoto3(imagelink3.toString());

    }*/


  /*  public void btnKaydet(View view)
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {


                    getValues();

                        String id =ref.push().getKey();
                        ref.child(id).setValue(film);



                    Toast.makeText(getLayoutInflater().getContext(), "Kaydedildi", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getLayoutInflater().getContext(), "Problem...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    public void btnImageKapak(View view) {
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,Gallery_intent);
    }

    public void btnImageFoto1(View view) {
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,Gallery_fotointent1);

    }


    public void btnImageFoto2(View view) {
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,Gallery_fotointent2);


    }

    public void btnImageFoto3(View view) {
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,Gallery_fotointent3);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_intent && resultCode==RESULT_OK)
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgKapakFoto.setImageBitmap(bitmap);
                imageUri = data.getData();
                imagePath=storage.child("Film").child(imageUri.getLastPathSegment());

                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
                imagePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imagePath.getDownloadUrl();
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

        else if(requestCode==Gallery_fotointent1 && resultCode==RESULT_OK)
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgFoto1.setImageBitmap(bitmap);
                imageUri1 = data.getData();
                imgFoto1Path=storage.child("Film").child(imageUri1.getLastPathSegment());

                imgFoto1Path.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
                imgFoto1Path.putFile(imageUri1).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imgFoto1Path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            imagelink1 = downUri.toString();
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==Gallery_fotointent2 && resultCode==RESULT_OK)
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgFoto2.setImageBitmap(bitmap);
                imageUri2 = data.getData();
                imgFoto2Path=storage.child("Film").child(imageUri2.getLastPathSegment());

                imgFoto2Path.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
                imgFoto2Path.putFile(imageUri2).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imgFoto2Path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            imagelink2 = downUri.toString();
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==Gallery_fotointent3 && resultCode==RESULT_OK)
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgFoto3.setImageBitmap(bitmap);
                imageUri3 = data.getData();
                imgFoto3Path=storage.child("Film").child(imageUri3.getLastPathSegment());

                imgFoto3Path.putFile(imageUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
                imgFoto3Path.putFile(imageUri3).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imgFoto3Path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            imagelink3 = downUri.toString();
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void btnKaydet(View view) {
        ad = edtAd.getText().toString();
       aciklama=edtAcikla.getText().toString();
        filmMenuId=edtId.getText().toString();
       fragmanLink=edtLink.getText().toString();
       teknikBilgi=edtTeknik.getText().toString();
       tur=edtTur.getText().toString();
       veritabaninaEkle(ad,aciklama,fragmanLink,teknikBilgi,filmMenuId,tur,imagelink,imagelink1,imagelink2,imagelink3);




    }
    public void veritabaninaEkle(String ad,String aciklama,String fragmanLink,String teknikBilgi,String filmMenuId,String tur,String imagelink,String imagelink1,String imagelink2,String imagelink3)
    {
        try {
            String id = ref.push().getKey();

            film = new Film(ad, aciklama, fragmanLink,teknikBilgi,filmMenuId,tur,imagelink,imagelink1,imagelink2,imagelink3);
            ref.child(id).setValue(film);
            Toast.makeText(getLayoutInflater().getContext(), "Kaydedildi", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getLayoutInflater().getContext(), "Problem...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }



}
