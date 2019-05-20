package com.example.mobilfilmprojesi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button btnGirisYap,btnKayitOl;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGirisYap=(Button)findViewById(R.id.btnGirisYap);
        btnKayitOl=(Button)findViewById(R.id.btnKayitOl);
        txtSlogan=(TextView) findViewById(R.id.txtSlogan);
        Paper.init(this);

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(i);

            }
        });

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),KayitActivity.class);
                startActivity(i);

            }
        });

        String user=Paper.book().read(Common.USER_KEY);
        String pwd=Paper.book().read(Common.PWD_KEY);
        if(user!=null  && pwd!=null)
        {
            if(!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }


    }

    private void login(final String user, final String pwd) {

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");
        final ProgressDialog mDialog=new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Lütfen Bekleyiniz...");
        mDialog.show();



        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(user).exists()){
                    mDialog.dismiss();
                    User user_=dataSnapshot.child(user).getValue(User.class);
                    if(user_.getPassword().equals(pwd))
                    {
                        Intent homeIntent=new Intent(getApplicationContext(),Home.class);
                        Common.currentUser=user_;
                        startActivity(homeIntent);
                        finish();



                    }

                    else
                    {
                        Toast.makeText(MainActivity.this,"Kullanıcı adı ya da parola hatalı!",Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    mDialog.dismiss();
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void adminGirisineGit(View view) {
        Intent i =new Intent(getApplicationContext(),AdminLogInActivity.class);
        startActivity(i);
    }
}
