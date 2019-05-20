package com.example.mobilfilmprojesi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriListActivity extends AppCompatActivity {
    ImageView Favfilm_image;
    TextView Favfilm_name,Favfilm_teknikbilgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori_list);
        Favfilm_image=(ImageView)findViewById(R.id.Favfilm_image);
        Favfilm_name=(TextView)findViewById(R.id.Favfilm_name);
        Favfilm_teknikbilgi=(TextView)findViewById(R.id.Favfilm_teknikbilgi);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
