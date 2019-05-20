package com.example.mobilfilmprojesi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Model.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogInActivity extends AppCompatActivity {
    EditText edtAdminKullaniciAdi,edtAdminSifre;
    Button btnAdminGirisYap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);
        edtAdminKullaniciAdi=(EditText)findViewById(R.id.edtAdminAdi);
        edtAdminSifre=(EditText)findViewById(R.id.edtAdminSifre);
        btnAdminGirisYap=(Button)findViewById(R.id.btnAdminGiris);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_Admin=database.getReference("Admin");
        btnAdminGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog=new ProgressDialog(AdminLogInActivity.this);
                mDialog.setMessage("Lütfen Bekleyiniz...");
                mDialog.show();


                table_Admin.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(edtAdminKullaniciAdi.getText().toString()).exists()){
                            mDialog.dismiss();
                            Admin admin =dataSnapshot.child(edtAdminKullaniciAdi.getText().toString()).getValue(Admin.class);



                            if(admin.getAdminSifre().equals(edtAdminSifre.getText().toString()))
                            {
                                Intent i =new Intent(getApplicationContext(),AdminPaneli.class);
                                startActivity(i);
                                finish();

                            }

                            else
                            {
                                Toast.makeText(AdminLogInActivity.this,"Admin adı ya da parola hatalı!",Toast.LENGTH_SHORT).show();
                            }


                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(AdminLogInActivity.this,"Veritabanında öyle bir tablo yok!",Toast.LENGTH_SHORT).show();
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
