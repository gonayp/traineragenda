package com.traineragenda.Planillas.VerPlanilla.ConfeccionarPlanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Administrador.ListaPlanillasXUsuarioActivity;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.Planillas.ListadoPlanillasActivity;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.util.Calendar;

public class VerPlanillaActivity1 extends AppCompatActivity {

    private String usuario_id;
    private String usuario_actual;
    private Long planilla_id;

    private FirebaseAuth mAuth;

    private String tipo;

    private TextView tv_nombre;
    private TextView tv_edad;

    private TextView spinner_aptitud;
    private TextView et_objetivo;
    private TextView spinner_modalidad;

    private Long cantidad_planillas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_planilla1);

        tipo = "CLIENTE";
        mAuth = FirebaseAuth.getInstance();
        usuario_actual = mAuth.getUid();

        tv_nombre = findViewById(R.id.tv_nombre);
        tv_edad = findViewById(R.id.tv_edad);

        spinner_aptitud = findViewById(R.id.spinner_aptitud);
        et_objetivo = findViewById(R.id.et_objetivo);
        spinner_modalidad = findViewById(R.id.spinner_modalidad);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Planillas");


//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                DataSnapshot d = dataSnapshot.child("Planilla_"+planilla_id);
                Planilla p = d.getValue(Planilla.class);

                usuario_id = p.getUsuario_id();

                String[] mAptitudArray;
                mAptitudArray =   getResources().getStringArray(R.array.array_aptitud);
                String[] mModalidadArray;
                mModalidadArray =   getResources().getStringArray(R.array.array_modalidad);


                if(p.getDatosAptitud()!= null) {
                    spinner_aptitud.setText(mAptitudArray[p.getDatosAptitud().intValue()]);
                }
                if(p.getDatosObjetivo() != null) {
                    et_objetivo.setText(p.getDatosObjetivo());
                }
                if(p.getDatosModalidad() != null){
                    spinner_modalidad.setText(mModalidadArray[p.getDatosModalidad().intValue()]);
                }


            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

        //apuntamos al nodo que queremos leer
        DatabaseReference myRef_user_actual = fdb.getReference("Users");

        myRef_user_actual.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();

                DataSnapshot d = dataSnapshot.child(usuario_actual);
                Usuario u = d.getValue(Usuario.class);

                if(u.getTipo().equals("admin")){
                    tipo = "ADMIN";
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

        //apuntamos al nodo que queremos leer
        DatabaseReference myRef_user = fdb.getReference("Users");

        //contamos la cantidad de planillas
        myRef_user.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot d = dataSnapshot.child(usuario_id);
                Usuario u = d.getValue(Usuario.class);

                tv_nombre.setText(u.getNombre() + " "+ u.getApellido());
                Long edad = Math.round(u.getAge());
                //Date nacimiento = u.getNacimiento();
                //String edad = getAge(nacimiento.getYear(),nacimiento.getMonth(),nacimiento.getDate());
                tv_edad.setText(edad.toString() + " Años");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ERROR FIREBASE", error.getMessage());
            }

        });

    }

    public void onActionRegistrar(View view) {



        Intent intent = new Intent(this, VerPlanillaActivity2.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {

        if(tipo.equals("ADMIN")) {
            Intent intent = new Intent(this, ListaPlanillasXUsuarioActivity.class);
            intent.putExtra("mesg_usuario_id", usuario_id);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ListadoPlanillasActivity.class);
            startActivity(intent);
        }

    }


    //Para bloquear el boton volver del telefono
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
