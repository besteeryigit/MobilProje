package com.example.mobilfilmprojesi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Database.Database;
import com.example.mobilfilmprojesi.Interface.ItemclickListener;
import com.example.mobilfilmprojesi.Model.Category;
import com.example.mobilfilmprojesi.Model.Film;
import com.example.mobilfilmprojesi.ViewHolder.FilmViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FilmList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference filmList;
    FirebaseStorage storage;
    StorageReference storageReference;
    String categoryId="";
    FirebaseRecyclerAdapter<Film, FilmViewHolder> adapter;
    FirebaseRecyclerAdapter<Film, FilmViewHolder> searchAdapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;
   MaterialEditText edtAd_admin,edtAciklama_admin,edtTur_admin,edtTeknikBilgi_admin,edtFilmMenuId_admin,edtFragmanLink_admin;
   Button btnKapakFotoSec,btn1FotoSec,btn2FotoSec,btn3FotoSec,btnGuncelle;
    Film newFilm;

        Uri saveUri,saveUri1,saveUri2,saveUri3;

    RelativeLayout rootLayout;
    Database localDB;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);
        database=FirebaseDatabase.getInstance();
        filmList=database.getReference("Film");
        localDB=new Database(this);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_film);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent() != null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            loadListFilm(categoryId);

        }


        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Film adını giriniz");


        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<String> suggest=new ArrayList<>();
            for(String search:suggestList)
            {
                if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    suggest.add(search);
            }

            materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<Film, FilmViewHolder>(Film.class,R.layout.film_item,FilmViewHolder.class,filmList.orderByChild("ad").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(final FilmViewHolder viewHolder, final Film model, final int position) {
                viewHolder.txtFilmName.setText(model.getAd());
                viewHolder.txtTeknik.setText(model.getTeknikBilgi());
                Picasso.with(getBaseContext()).load(model.getFotograf()).into(viewHolder.film_image);






                final Film local=model;
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent filmDetail=new Intent(FilmList.this,FilmDetailActivity.class);
                        filmDetail.putExtra("filmId",searchAdapter.getRef(position).getKey());
                        startActivity(filmDetail);
                    }
                });

                    }
                };
        recyclerView.setAdapter(searchAdapter);

            }

    private void loadSuggest() {
        filmList.orderByChild("filmMenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Film item=postSnapshot.getValue(Film.class);
                    suggestList.add(item.getAd());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListFilm(String categoryId) {

        adapter=new FirebaseRecyclerAdapter<Film, FilmViewHolder>(Film.class,R.layout.film_item,FilmViewHolder.class,filmList.orderByChild("filmMenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FilmViewHolder viewHolder, Film model, int position) {
            viewHolder.txtFilmName.setText(model.getAd());
            viewHolder.txtTeknik.setText(model.getTeknikBilgi());
                Picasso.with(getBaseContext()).load(model.getFotograf()).into(viewHolder.film_image);

                final Film local=model;
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent filmDetail=new Intent(FilmList.this,FilmDetailActivity.class);
                        filmDetail.putExtra("filmId",adapter.getRef(position).getKey());
                        startActivity(filmDetail);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateFilmDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
        else  if(item.getTitle().equals(Common.DELETE))
        {
            deleteFilm(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFilm(String key) {
        filmList.child(key).removeValue();
    }

    private void showUpdateFilmDialog(final String key, final Film item) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FilmList.this);
        LayoutInflater inflater=this.getLayoutInflater();
        View update_menu_layout=inflater.inflate(R.layout.update_film_layout,null);
        edtAd_admin=update_menu_layout.findViewById(R.id.edtAd_admin);
        edtAciklama_admin=update_menu_layout.findViewById(R.id.edtAciklama_admin);
        edtTur_admin=update_menu_layout.findViewById(R.id.edtTur_admin);
        edtTeknikBilgi_admin=update_menu_layout.findViewById(R.id.edtTeknikBilgi_admin);
        edtFilmMenuId_admin=update_menu_layout.findViewById(R.id.edtFilmMenuId_admin);
        edtFragmanLink_admin=update_menu_layout.findViewById(R.id.edtFragmanLink_admin);

        btnKapakFotoSec=update_menu_layout.findViewById(R.id.btnKapakFotoSec);
        btn1FotoSec=update_menu_layout.findViewById(R.id.btn1FotoSec);
        btn2FotoSec=update_menu_layout.findViewById(R.id.btn2FotoSec);
        btn3FotoSec=update_menu_layout.findViewById(R.id.btn3FotoSec);
        btnGuncelle=update_menu_layout.findViewById(R.id.btnGuncelle);


        edtAd_admin.setText(item.getAd());
        edtAciklama_admin.setText(item.getAciklama());
        edtFilmMenuId_admin.setText(item.getFilmMenuId());
        edtTeknikBilgi_admin.setText(item.getTeknikBilgi());
        edtTur_admin.setText(item.getTur());
        edtFragmanLink_admin.setText(item.getFragmanLink());

        btnKapakFotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btn1FotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage1();

            }
        });

        btn2FotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage2();
            }
        });
        btn3FotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              chooseImage3();

            }
        });


        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
                changeImage1(item);
                changeImage2(item);
                changeImage3(item);

                item.setAd(edtAd_admin.getText().toString());
                item.setAciklama(edtAciklama_admin.getText().toString());
                item.setTur(edtTur_admin.getText().toString());
                item.setTeknikBilgi(edtTeknikBilgi_admin.getText().toString());
                item.setFilmMenuId(edtFilmMenuId_admin.getText().toString());
                item.setFragmanLink(edtFragmanLink_admin.getText().toString());





            }
        });

        alertDialog.setView(update_menu_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);
        alertDialog.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                filmList.child(key).setValue(item);
                Snackbar.make(rootLayout,"Film"+item.getAd()+"güncellendi",Snackbar.LENGTH_SHORT).show();




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

  private void changeImage(final Film item)
  {
      if(saveUri!=null)
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
                  Toast.makeText(FilmList.this,"Yüklendi",Toast.LENGTH_SHORT).show();
                  imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          item.setFotograf(uri.toString());


                      }
                  });
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  mDialog.dismiss();
                  Toast.makeText(FilmList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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

    private void changeImage1(final Film item)
    {
        if(saveUri1!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FilmList.this,"Yüklendi",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setFoto1(uri.toString());


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FilmList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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
    private void changeImage2(final Film item)
    {
        if(saveUri2!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FilmList.this,"Yüklendi",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setFoto2(uri.toString());


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FilmList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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

    private void changeImage3(final Film item)
    {
        if(saveUri3!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FilmList.this,"Yüklendi",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setFoto3(uri.toString());


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FilmList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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



    private void chooseImage() {

        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i,"Fotoğraf seçiniz"),Common.PICK_IMAGE_REQUEST);

    }
    private void chooseImage1() {

        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i,"Fotoğraf seçiniz"),Common.PICK_IMAGE_REQUEST_1);

    }
    private void chooseImage2() {

        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i,"Fotoğraf seçiniz"),Common.PICK_IMAGE_REQUEST_2);

    }
    private void chooseImage3() {

        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i,"Fotoğraf seçiniz"),Common.PICK_IMAGE_REQUEST_3);

    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null&& data.getData()!=null)
        {
            saveUri=data.getData();

            btnKapakFotoSec.setText("Fotoğraf seçildi");

        }
        else   if(requestCode== Common.PICK_IMAGE_REQUEST_1 && resultCode==RESULT_OK && data!=null&& data.getData()!=null)
        {
            saveUri1=data.getData();
            btn1FotoSec.setText("Birinci Foto seçildi");
        }
        else   if(requestCode== Common.PICK_IMAGE_REQUEST_2 && resultCode==RESULT_OK && data!=null&& data.getData()!=null)
        {
            saveUri2=data.getData();
            btn2FotoSec.setText("İkinci Foto seçildi");
        }
        else   if(requestCode== Common.PICK_IMAGE_REQUEST_3 && resultCode==RESULT_OK && data!=null&& data.getData()!=null)
        {
            saveUri3=data.getData();
            btn3FotoSec.setText("Üçüncü Foto seçildi");
        }
    }
}
