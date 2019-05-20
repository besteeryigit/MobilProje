package com.example.mobilfilmprojesi;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import 	java.util.Arrays;
import java.util.Queue;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Database.Database;
import com.example.mobilfilmprojesi.Model.Film;
import com.example.mobilfilmprojesi.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import static android.support.v4.os.LocaleListCompat.create;

public class FilmDetailActivity extends AppCompatActivity implements RatingDialogListener {

    ImageView img_film,img_foto1,img_foto2,img_foto3;
    FloatingActionButton btnFragmanIzle,btnIzleneceklereEkle,btnIzlenenlereEkle,btnRating,btnFavori;
    Button btnShowComment;
    TextView txtFilmTur,txtFragman,txtIzleneceklereEkle,txtIzlenenlereEkle,txtFilmKonusu,txtFilmTeknikBilgi;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String filmId="";
    String userName="";
    FirebaseDatabase database;
    DatabaseReference films;
    Database localDB;
    RatingBar ratingBar;
    DatabaseReference ratingTbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        database=FirebaseDatabase.getInstance();
        films=database.getReference("Film");
        localDB=new Database(this);
        btnRating=(FloatingActionButton)findViewById(R.id.btn_rating);
        ratingTbl=database.getReference("Rating");
        btnFavori=(FloatingActionButton)findViewById(R.id.btnFavori);
        btnFragmanIzle=(FloatingActionButton)findViewById(R.id.btnFragmanıİzle);

        img_film=(ImageView)findViewById(R.id.img_film);
        img_foto1=(ImageView)findViewById(R.id.image_foto1);
        img_foto2=(ImageView)findViewById(R.id.image_foto2);
        img_foto3=(ImageView)findViewById(R.id.image_foto3);

        txtFilmTur=(TextView)findViewById(R.id.txtFilmTur);
        txtFragman=(TextView)findViewById(R.id.txtFragmanİzle);

        txtFilmKonusu=(TextView)findViewById(R.id.txtFilmKonusu);
        txtFilmTeknikBilgi=(TextView)findViewById(R.id.txtFilmTeknikBilgi);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        if(getIntent()!=null)
            filmId=getIntent().getStringExtra("filmId");
        userName=Common.currentUser.getUserName();
        if(!filmId.isEmpty())
        {

            getDetailFilm(filmId);
            //getRatingFilm(filmId);

        }



        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showRatingDialog();
            }
        });

        btnFragmanIzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmanLinkleri(filmId);
            }
        });

       if(localDB.isFavorite(filmId,userName))
            btnFavori.setImageResource(R.drawable.ic_favorite_black_24dp);

        btnFavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!localDB.isFavorite(filmId,userName))
                {
                    localDB.addToFavorites(filmId,userName);
                    btnFavori.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Toast.makeText(FilmDetailActivity.this,"Favorilere eklendi",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    localDB.removeFromFavorites(filmId,userName);
                    btnFavori.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(FilmDetailActivity.this,"Favorilerden çıkarıldı",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

   /* private void getRatingFilm(String filmId) {

        Query filmRatingg=ratingTbl.orderByChild("filmId").equalTo(filmId);
        filmRatingg.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item=postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count!=0)
                {
                    float average= sum/count;
                    ratingBar.setRating(average);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Gönder")
                .setNegativeButtonText("Kapat").setNoteDescriptions(Arrays.asList("Çok Kötü","İyi Değil","İyi","Çok İyi","Mükemmel"))
                .setDefaultRating(1)
                .setTitle("Bu filmi oylayın")
                .setDescription("Lütfen yıldızları seçiniz ve geribildirim gönderiniz")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Lütfen yorumlarınızı buraya yazın")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FilmDetailActivity.this)
                .show();
    }




    private void getDetailFilm(String filmId) {
        films.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Film film=dataSnapshot.getValue(Film.class);
                Picasso.with(getBaseContext()).load(film.getFotograf()).into(img_film);
                Picasso.with(getBaseContext()).load(film.getFoto1()).into(img_foto1);
                Picasso.with(getBaseContext()).load(film.getFoto2()).into(img_foto2);
                Picasso.with(getBaseContext()).load(film.getFoto3()).into(img_foto3);
                collapsingToolbarLayout.setTitle(film.getAd());
                txtFilmKonusu.setText(film.getAciklama());
                txtFilmTeknikBilgi.setText(film.getTeknikBilgi());
                txtFilmTur.setText("Tür:  "+film.getTur());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        private void getFragmanLinkleri(String filmId)
    {
        films.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Film birFilm=dataSnapshot.getValue(Film.class);
               String link=birFilm.getFragmanLink();

               Intent i=new Intent(Intent.ACTION_VIEW);
               i.setData(Uri.parse(link));
               startActivity(i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        final Rating rating=new Rating(Common.currentUser.getUserName(),filmId,String.valueOf(i),s);
        ratingTbl.child(Common.currentUser.getUserName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentUser.getUserName()).exists())
                {
                   ratingTbl.child(Common.currentUser.getUserName()).removeValue();
                    ratingTbl.child(Common.currentUser.getUserName()).setValue(rating);


                }
                else
                {
                    ratingTbl.child(Common.currentUser.getUserName()).setValue(rating);
                }
                Toast.makeText(FilmDetailActivity.this,"Geribildiriminiz için teşekkürler",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }




}
