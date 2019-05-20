package com.example.mobilfilmprojesi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;

import com.example.mobilfilmprojesi.Interface.ItemclickListener;
import com.example.mobilfilmprojesi.Model.Film;
import com.example.mobilfilmprojesi.ViewHolder.FilmViewHolder;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TumFilmList extends AppCompatActivity {
    RecyclerView mRecyclerTumFilmler;
    FirebaseDatabase mFirebaseDatabase;
    TumFilmList tumFilmList;
    DatabaseReference mRef;
    FirebaseRecyclerAdapter<Film, FilmViewHolder> fadapter;
    FirebaseRecyclerAdapter<Film, FilmViewHolder> searchAdapterTumFilmler;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_film_list);

        mRecyclerTumFilmler=(RecyclerView)findViewById(R.id.recylerTumFilmler);
        mRecyclerTumFilmler.setHasFixedSize(true);
        mRecyclerTumFilmler.setLayoutManager(new LinearLayoutManager(this));
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("Film");

        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBarTumFilmler);
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
                    mRecyclerTumFilmler.setAdapter(fadapter);
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
        searchAdapterTumFilmler=new FirebaseRecyclerAdapter<Film, FilmViewHolder>(Film.class,R.layout.film_item,FilmViewHolder.class,mRef.orderByChild("ad").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(FilmViewHolder viewHolder, Film model, int position) {
                viewHolder.txtFilmName.setText(model.getAd());
                viewHolder.txtTeknik.setText(model.getTeknikBilgi());
                Picasso.with(getBaseContext()).load(model.getFotograf()).into(viewHolder.film_image);
                final Film local=model;
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent filmDetail=new Intent(TumFilmList.this,FilmDetailActivity.class);
                        filmDetail.putExtra("filmId",searchAdapterTumFilmler.getRef(position).getKey());
                        startActivity(filmDetail);
                    }
                });

            }
        };
        mRecyclerTumFilmler.setAdapter(searchAdapterTumFilmler);

    }


    private void loadSuggest() {
        mRef.addValueEventListener(new ValueEventListener() {
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


    @Override
    protected void onStart() {
        super.onStart();
        fadapter=new FirebaseRecyclerAdapter<Film, FilmViewHolder>(Film.class,R.layout.film_item,FilmViewHolder.class,mRef) {
            @Override
            protected void populateViewHolder(FilmViewHolder viewHolder, Film model, int position) {
                viewHolder.txtFilmName.setText(model.getAd());
                viewHolder.txtTeknik.setText(model.getTeknikBilgi());
                Picasso.with(getBaseContext()).load(model.getFotograf()).into(viewHolder.film_image);
                final Film local=model;
               viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent filmDetail=new Intent(TumFilmList.this,FilmDetailActivity.class);
                        filmDetail.putExtra("filmId",fadapter.getRef(position).getKey());
                        startActivity(filmDetail);
                    }
                }


                );
            }
        };

        fadapter.notifyDataSetChanged();
        mRecyclerTumFilmler.setAdapter(fadapter);



    }





}
