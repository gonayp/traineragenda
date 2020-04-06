package com.traineragenda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.otros.DatabaseUtil;
import com.traineragenda.personal.TrainerPerfilMenuActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CompromisoTerminadoActivity extends AppCompatActivity {

    private String tipo_usuario;

    private FirebaseAuth mAuth;

    private TextView mTextoFieldTitulo;
    private TextView mTextoFieldDescripcion;
    private TextView mTextoFieldFecha;
    private EditText mEditTextComentario;
    private ImageView mImagen;
    private TextView getmTextoFieldEntrenador;
    private android.support.design.widget.FloatingActionButton verTrenador;
    private android.support.design.widget.FloatingActionButton btnGuardar;
    Long compromiso_id;
    Double latitud;
    Double longitude;
    Compromiso compromiso;
    RatingBar simpleRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromiso_terminado);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextoFieldTitulo = findViewById(R.id.tv_title);
        mTextoFieldFecha = findViewById(R.id.tv_fecha);
        mTextoFieldDescripcion = findViewById(R.id.tv_description);
        mEditTextComentario =findViewById(R.id.et_comentarios);
        getmTextoFieldEntrenador = findViewById(R.id.tv_entrenador);
        mImagen = findViewById(R.id.iv_avatar);
        verTrenador = findViewById(R.id.fab_ver_entrenador);
        btnGuardar = findViewById(R.id.fab_borrar);



        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        compromiso_id = intent.getLongExtra("mesg_compromiso_id",0);


        //MEtodo para buscar un compromiso de la lista
        DatabaseUtil.findCompromisoById(compromiso_id, new DatabaseUtil.Listener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCompromisoRetrieved(Compromiso c) {
                compromiso = c;
                Log.d("********************",compromiso.getId());
                mTextoFieldTitulo.setText(compromiso.getTitle());
                SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yy HH:mm");
                String strDt = simpleDate.format(compromiso.getFecha());
                mTextoFieldFecha.setText(strDt);
                latitud = compromiso.getLatitud();
                longitude = compromiso.getLongitud();
                Usuario u = compromiso.getEntrenador();
                if(u != null) {
                    getmTextoFieldEntrenador.setText(u.getNombre());
                    verTrenador.setVisibility(View.VISIBLE);
                }
                else {
                    getmTextoFieldEntrenador.setText("");
                    verTrenador.setVisibility(View.GONE);
                }

                //RatingBar
                simpleRatingBar = (RatingBar) findViewById(R.id.ratingBar); // initiate a rating bar
                Float ratingNumber = simpleRatingBar.getRating();
                HashMap<String, Float> puntuaciones = compromiso.getPuntuacion();
                if(puntuaciones != null)
                    simpleRatingBar.setRating((puntuaciones.get(compromiso.getParticipantes().get(0))));

                //Comentario
                HashMap<String, String> comentarios = compromiso.getComentario();
                if(comentarios != null){
                    mEditTextComentario.setText(comentarios.get(compromiso.getParticipantes().get(0)));
                }

                //Direccion
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(CompromisoTerminadoActivity.this, Locale.getDefault());

                String direccion = "";
                try {
                    addresses = geocoder.getFromLocation(compromiso.getLatitud(), compromiso.getLongitud(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    direccion = address ;
                }
                catch (IOException e){
                    direccion = compromiso.getLatitud().toString() + " / "+ compromiso.getLongitud().toString();
                }
                mTextoFieldDescripcion.setText(direccion);

                btnGuardar.setVisibility(View.GONE);

                //ESTADO
                int image = 0;
                String estado = compromiso.getEstado();
                if(estado != null) {
                    if (estado.equals("pendiente"))
                        image = R.drawable.baseline_directions_run_black_18dp;
                    if (estado.equals("cancelado"))
                        image = R.drawable.ic_dialog_close_light;
                    if (estado.equals("evaluacion")) {
                        image = R.drawable.baseline_how_to_reg_black_18dp;
                        btnGuardar.setVisibility(View.VISIBLE);
                    }
                    if (estado.equals("terminado"))
                        image = R.drawable.baseline_check_box_black_18dp;
                }
                Glide.with(getApplicationContext()).load(image).into(mImagen);
            }
        });



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
                        tipo_usuario = user.getTipo();
                    }
                    public void onCancelled(DatabaseError e){

                    }
                });

    }


    public void onActionVolver(View view) {

        if(tipo_usuario.equals("cliente")) {
            Intent intent = new Intent(this, MeusTreinosActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, TrainerPerfilMenuActivity.class);
            startActivity(intent);
        }

    }

    public void onActionGuardar(final View view) {

        Boolean valid = true;

        String comentario = mEditTextComentario.getText().toString();
        if (TextUtils.isEmpty(comentario)) {
            mEditTextComentario.setError("Required.");
            valid = false;
        } else {
            mEditTextComentario.setError(null);
        }

        if(valid) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbCompromisoComentario = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/comentario");
            HashMap<String, String> comentarios = new HashMap<>();
            comentarios.put(compromiso.getParticipantes().get(0),comentario);
            dbCompromisoComentario.setValue(comentarios);

            DatabaseReference dbCompromisoPuntuacion = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/puntuacion");
            HashMap<String, Float> puntuacion = new HashMap<>();
            puntuacion.put(compromiso.getParticipantes().get(0),simpleRatingBar.getRating());
            dbCompromisoPuntuacion.setValue(puntuacion);

            DatabaseReference dbCompromisoEstado = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/estado");
            dbCompromisoEstado.setValue("terminado");

            Intent intent = new Intent(view.getContext(), MainMapActivity.class);
            startActivity(intent);
        }


    }

    public void onActionVerEnMapa(View view) {

        Intent intent = new Intent(this, MainMapActivity.class);
        intent.putExtra("currentLatitude",latitud );
        intent.putExtra("currentLongitude", longitude);
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
