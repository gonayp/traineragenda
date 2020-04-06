package com.traineragenda.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrainerPerfilActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView mNombreField;
    private TextView tvAnios;
    private TextView tvNumTrenamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_perfil);

        mNombreField = findViewById(R.id.fieldNombre);
        tvAnios = findViewById(R.id.tvAnios);
        tvNumTrenamentos = findViewById(R.id.tvNumTrenamientos);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Users");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();

                DataSnapshot d = dataSnapshot.child(mAuth.getUid());
                Usuario u = d.getValue(Usuario.class);

                mNombreField.setText(u.getNombre());

                RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.ratingBar); // initiate a rating bar
                simpleRatingBar.setVisibility(View.GONE);
                float puntuacion = u.getPuntuacion();
                if( puntuacion > 0) {
                    simpleRatingBar.setRating(puntuacion);
                    simpleRatingBar.setVisibility(View.VISIBLE);
                }

                int numEntrenamientos = u.getNumEntrenamientos();
                tvNumTrenamentos.setText(Integer.toString(numEntrenamientos));

                Date fecha_alta = u.getAlta();
                Calendar calendar = Calendar.getInstance(Locale.US);
                Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
//                Calendar cal = Calendar.getInstance();
//                Date fecha_ahora=cal.getTime();
                Calendar cal_alta = Calendar.getInstance();
                cal_alta.setTime(fecha_alta);
                Calendar cal_ahora = Calendar.getInstance();
                cal_ahora.setTime(fecha_ahora);
                long diff = cal_ahora.getTimeInMillis() - cal_alta.getTimeInMillis() ; //result in millis
                long years = diff / (365 * 24 * 60 * 60 * 1000);
                long days = diff / (24 * 60 * 60 * 1000);
                long hours = diff / (60 * 60 * 1000);
                long min = diff / (60 * 1000);

                String tiempo = Long.toString(days)  + " dias";//years +" Anos y "+ days;


                tvAnios.setText(tiempo);

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }


    public void onActionVolver(View view) {

        Intent intent = new Intent(this, TrainerPerfilMenuActivity.class);
        startActivity(intent);

    }

    //Para bloquear el boton volver del telefono
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
