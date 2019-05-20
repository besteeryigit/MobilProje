package com.example.mobilfilmprojesi;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class KayitActivity extends AppCompatActivity {

    MaterialEditText edtKullaniciAd,edtAdSoyad,edtParola,edtSecureCode;
    Button btnKayit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        edtKullaniciAd=(MaterialEditText)findViewById(R.id.edtKullaniciAdi);
        edtAdSoyad=(MaterialEditText)findViewById(R.id.edtAdSoyad);
        edtParola=(MaterialEditText)findViewById(R.id.edtSifre);
        edtSecureCode=(MaterialEditText)findViewById(R.id.edtSecureCode);
        btnKayit=(Button)findViewById(R.id.btnKayit);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");
        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog=new ProgressDialog(KayitActivity.this);
                mDialog.setMessage("Lütfen Bekleyiniz...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtKullaniciAd.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                           // Toast.makeText(KayitActivity.this,"Böyle bir kullanıcı zaten var.",Toast.LENGTH_SHORT).show();




                        }
                        else
                        {
                            Toast.makeText(KayitActivity.this,"Kayıt Başarılı!",Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            User user=new User(edtAdSoyad.getText().toString(),edtParola.getText().toString(),edtSecureCode.getText().toString(),edtKullaniciAd.getText().toString());
                            table_user.child(edtKullaniciAd.getText().toString()).setValue(user);

                            finish();
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
