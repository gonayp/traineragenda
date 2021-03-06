package com.traineragenda;



import android.content.Intent;
        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
import android.widget.Toast;

        import com.google.firebase.FirebaseApp;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.traineragenda.Compromisos.Compromiso;
        import com.traineragenda.Compromisos.CompromisoFragment;
        import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.Usuarios.Usuario;

import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;



public class MeusTreinosActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_treinos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Compromisos");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                Log.d("asd", "**************************************** ");
                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();


                Long cantidad = dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> list_compromisos = dataSnapshot.getChildren();


                for (int i=0; i < cantidad; i++) {
                    Compromiso a = list_compromisos.iterator().next().getValue(Compromiso.class);
                    List<String> Participantes =  a.getParticipantes();
                    String usuario = mAuth.getUid();
                    if(Participantes.get(0).equals( usuario) ) {//Si el participante del evento es el usuario actual

                        if(a.getEstado() != "pendiente"){ //Todas las actividades que no esten pendientes
                            CompromisoRepository.getInstance().addCompromiso(a);
                        }
                        else {

                            Date fecha_actividad = a.getFecha();
                            Calendar calendar = Calendar.getInstance(Locale.US);
                            Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);

                            if (fecha_actividad.compareTo(fecha_ahora) < 0) {//si la fecha del evento es menor a la actual
                                CompromisoRepository.getInstance().addCompromiso(a);

                                if (a.getEntrenador() == null && a.getEstado() == "pendiente") {//SI nunca se le asigno entrenador y esta pendiente
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference dbCompromiso = database.getReference("/Compromisos/Compromiso_" + a.getCompromisoID() + "/estado");
                                    dbCompromiso.setValue("cancelado");//Cambiamos estado a cancelado
                                }
//                                if (a.getEntrenador() != null && a.getEstado() == "pendiente") {//Si esta pendiente
//                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                    DatabaseReference dbCompromiso = database.getReference("/Compromisos/Compromiso_" + a.getCompromisoID() + "/estado");
//                                    dbCompromiso.setValue("evaluacion");//Cambiamos estado a pendiente de evaluacion
//                                    DatabaseReference dbUsuario = database.getReference("/Users/"+usuario+"/ocupado");
//                                    dbUsuario.setValue(false);//Cambiamos estado de usuario a desocupado
//                                }
                            }
                        }
                    }

                }


                CompromisoFragment leadsFragment = (CompromisoFragment)
                        getSupportFragmentManager().findFragmentById(R.id.compromiso_container);

                if (leadsFragment == null) {
                    leadsFragment = CompromisoFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.compromiso_container, leadsFragment)
                            .commit();
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }


        });




    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, MainMapActivity.class);
        startActivity(intent);

    }


    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Usuario post = dataSnapshot.getValue( Usuario.class);
                // [START_EXCLUDE]
                //mAuthorView.setText(post.userName);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(MeusTreinosActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_salir) {


            mAuth.signOut();
            Intent entrada = new Intent(getApplicationContext(), EntradaActivity.class);
            startActivity(entrada);
            return true;
        }
        if (id == R.id.action_settings) {
            Log.i("ActionBar", "Settings!");
            Intent config = new Intent(getApplicationContext(), ConfiguracionesActivity.class);
            startActivity(config);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
