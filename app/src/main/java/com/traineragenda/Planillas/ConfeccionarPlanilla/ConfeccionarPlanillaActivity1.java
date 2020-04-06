package com.traineragenda.Planillas.ConfeccionarPlanilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Administrador.ListaPlanillasXUsuarioActivity;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.util.Calendar;
import java.util.Date;

public class ConfeccionarPlanillaActivity1 extends AppCompatActivity {

    private String usuario_id;

    long pace;

    Long peso,altura,edad;

    private TextView tv_nombre;
    private TextView tv_edad;

    private Spinner spinner_aptitud;
    private EditText et_objetivo;
    private EditText et_distancia;
    private EditText et_tiempo;
    private Spinner spinner_modalidad;
    private Spinner spinner_periocidad;

    private Long cantidad_planillas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeccionar_planilla1);

        tv_nombre = findViewById(R.id.tv_nombre);
        tv_edad = findViewById(R.id.tv_edad);

        spinner_aptitud = findViewById(R.id.spinner_aptitud);
        et_objetivo = findViewById(R.id.et_objetivo);
        et_distancia = findViewById(R.id.et_c1);
        et_tiempo = findViewById(R.id.et_c2);
        spinner_modalidad = findViewById(R.id.spinner_modalidad);
        spinner_periocidad = findViewById(R.id.spinner_periocidad);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        usuario_id = intent.getStringExtra("mesg_usuario_id");

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Planillas");

        //contamos la cantidad de planillas
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cantidad_planillas = dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ERROR FIREBASE", error.getMessage());
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
                edad = Math.round(u.getAge());
                //String edad = getAge(nacimiento.getYear(),nacimiento.getMonth(),nacimiento.getDate());
                tv_edad.setText(edad.toString()+" Años");

                peso = Long.parseLong(u.getPeso().toString());
                altura = Long.parseLong(u.getAltura().toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ERROR FIREBASE", error.getMessage());
            }

        });

        et_distancia.addTextChangedListener(filterTextWatcher);
        et_tiempo.addTextChangedListener(filterTextWatcher);

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Long dist = 0l;
            if (!et_distancia.getText().toString().equals("")){
                dist = Long.parseLong(et_distancia.getText().toString());
            }
            Long tiem = 0l;
            if(!et_tiempo.getText().toString().equals("")){
                tiem = Long.parseLong(et_tiempo.getText().toString());
            }

            if(dist > 0 && tiem > 0) {
                pace =  tiem/ dist;
                et_objetivo.setText(et_distancia.getText().toString() + " Km " + et_tiempo.getText().toString() + " min Pace:" + pace);
            }

        }
    };

    public void onActionRegistrar(View view) {

        //Codigo para crear una planilla nuevo
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Long planilla_id = 1l;
        if (cantidad_planillas != null) {
            planilla_id = cantidad_planillas + 1;
        }
        Planilla a = new Planilla(planilla_id,usuario_id,new Date(),false,spinner_aptitud.getSelectedItemId(),et_objetivo.getText().toString(),spinner_modalidad.getSelectedItemId(),
                spinner_periocidad.getSelectedItemId(),peso,altura,edad,pace);

        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id));
        mRef.setValue(a);


        Intent intent = new Intent(this, ConfeccionarPlanillaActivity2.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ListaPlanillasXUsuarioActivity.class);
        intent.putExtra("mesg_usuario_id", usuario_id);
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
