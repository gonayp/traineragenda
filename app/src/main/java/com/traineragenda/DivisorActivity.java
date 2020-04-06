package com.traineragenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Administrador.MainAdministradorActivity;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.personal.TrainerMenuActivity;

public class DivisorActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divisor);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fdb.getReference();


        //Single event listener
        myRef.child("Users").child(uid).addListenerForSingleValueEvent(
                new  ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Usuario user = dataSnapshot.getValue(Usuario.class);
                        String t = user.getTipo();
                        if(t.equals( "cliente")){
                            Intent intent = new Intent(DivisorActivity.this, MainMapActivity .class);
                            startActivity(intent);
                        }
                        else{
                            if(t.equals( "admin")){
                                Intent intent = new Intent(DivisorActivity.this, MainAdministradorActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(DivisorActivity.this, TrainerMenuActivity.class);
                                startActivity(intent);
                            }
                        }
                        //user.email now has your email value
                    }
                    public void onCancelled(DatabaseError e){

                    }
                });

    }
}
