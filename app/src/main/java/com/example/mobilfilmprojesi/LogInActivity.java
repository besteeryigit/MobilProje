package com.example.mobilfilmprojesi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {
    EditText edtKullaniciAdi,edtSifre;
    Button btnGiris;
    CheckBox ckbRemember;
    TextView txtForgotPwd;
   FirebaseDatabase database;
  DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edtKullaniciAdi=(EditText)findViewById(R.id.edtKullaniciAdi);
        edtSifre=(EditText)findViewById(R.id.edtSifre);
        btnGiris=(Button)findViewById(R.id.btnGiris);
        ckbRemember=(CheckBox)findViewById(R.id.ckbRemember);

        txtForgotPwd=(TextView)findViewById(R.id.txtForgotPwd);
        Paper.init(this);
       database=FirebaseDatabase.getInstance();
         table_user=database.getReference("user");


        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckbRemember.isChecked())
                {
                    Paper.book().write(Common.USER_KEY,edtKullaniciAdi.getText().toString());
                    Paper.book().write(Common.PWD_KEY,edtSifre.getText().toString());
                }
                final ProgressDialog mDialog=new ProgressDialog(LogInActivity.this);
                mDialog.setMessage("Lütfen Bekleyiniz...");
                mDialog.show();



            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(edtKullaniciAdi.getText().toString()).exists()){
                    mDialog.dismiss();
                    User user=dataSnapshot.child(edtKullaniciAdi.getText().toString()).getValue(User.class);
                    user.setUserName(edtKullaniciAdi.getText().toString());



                    if(user.getPassword().equals(edtSifre.getText().toString()))
                    {
                        Intent homeIntent=new Intent(getApplicationContext(),Home.class);
                        Common.currentUser=user;
                        startActivity(homeIntent);
                        finish();
                        table_user.removeEventListener(this);


                    }

                    else
                    {
                        Toast.makeText(LogInActivity.this,"Kullanıcı adı ya da parola hatalı!",Toast.LENGTH_SHORT).show();
                    }


                }
                else
                    {
                        mDialog.dismiss();
                        Toast.makeText(LogInActivity.this,"Veritabanında öyle bir tablo yok!",Toast.LENGTH_SHORT).show();
                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            }
        });
       txtForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPwdDialog();
            }
        });


    }

    private void showForgotPwdDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Şifremi unuttum");
        builder.setMessage("Güvenlik kodunu giriniz");
        LayoutInflater inflater=this.getLayoutInflater();
        View forgot_view=inflater.inflate(R.layout.forgot_password_layout,null);
        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);
    final    MaterialEditText edtKullanici=(MaterialEditText)forgot_view.findViewById(R.id.edtKullanici);
       final  MaterialEditText edtSecureCode=(MaterialEditText)forgot_view.findViewById(R.id.edtSecureCode);
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.child(edtKullanici.getText().toString()).getValue(User.class);
                        if(user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(LogInActivity.this,"Şifreniz:"+user.getPassword(),Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(LogInActivity.this,"Güvenlik kodu yanlış",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
