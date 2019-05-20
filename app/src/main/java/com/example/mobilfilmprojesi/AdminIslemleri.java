package com.example.mobilfilmprojesi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.UUID;

public class AdminIslemleri extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;
    Button btnFilmEkle,btnFilmSil,btnFilmGuncelle,btnKategoriEkle;
    MaterialEditText edtCName;
    Button btnCSec,btnCKaydet,btnListele;
    Category newCategory;
    Uri saveUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_islemleri);
        database=FirebaseDatabase.getInstance();
        categories=database.getReference("Category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        btnFilmEkle=(Button)findViewById(R.id.btnFilmEkle);
        btnFilmSil=(Button)findViewById(R.id.btnFilmSil);
        btnKategoriEkle=(Button)findViewById(R.id.btnKategoriEkle);
        btnListele=(Button)findViewById(R.id.btnListele);

        btnListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminIslemleri.this,TumFilmList.class);
                startActivity(i);
            }
        });



        btnFilmEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FilmEkleActivity.class);
                startActivity(i);
            }
        });

        btnKategoriEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnFilmSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminIslemleri.this,AdminPaneli.class);
                startActivity(i);
            }
        });





    }

    private void showDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(AdminIslemleri.this);
        alertDialog.setTitle("Yeni Kategori Ekle");
        alertDialog.setMessage("Lütfen tüm bilgileri doldurunuz.");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);
        edtCName=add_menu_layout.findViewById(R.id.edtCAd);
        btnCSec=add_menu_layout.findViewById(R.id.btnCSec);
        btnCKaydet=add_menu_layout.findViewById(R.id.btnCKaydet);
        btnCSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



        btnCKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoYukle();
            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);
        alertDialog.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(newCategory !=null)
                {
                    categories.push().setValue(newCategory);
                }

            }
        });
        alertDialog.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void fotoYukle() {
        if(saveUri!=null )
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(AdminIslemleri.this,"Yüklendi",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory=new Category(edtCName.getText().toString(),uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(AdminIslemleri.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0+taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Yüklendi"+progress+"%");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null&& data.getData()!=null)
        {
            saveUri=data.getData();
            btnCSec.setText("Fotoğraf seçildi");
        }
    }

    private void chooseImage() {

        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i,"Fotoğraf seçiniz"),Common.PICK_IMAGE_REQUEST);

    }

    public void adminAnaMenuyeDon(View view) {
        Intent i=new Intent(AdminIslemleri.this,AdminPaneli.class);
        startActivity(i);
    }
}
