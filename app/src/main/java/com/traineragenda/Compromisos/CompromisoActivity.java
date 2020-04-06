package com.traineragenda.Compromisos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.MainActivity;
import com.traineragenda.MainMapActivity;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.otros.Chat;
import com.traineragenda.otros.DatabaseUtil;
import com.traineragenda.otros.MessageAdapter;
import com.traineragenda.personal.TrainerMenuActivity;
import com.traineragenda.personal.TrainerPerfilMenuActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CompromisoActivity extends AppCompatActivity {

    private String tipo_usuario;

    private FirebaseAuth mAuth;

    private TextView mTextoFieldTitulo;
    private TextView mTextoFieldFecha;
    private ImageView mImagen;
    private TextView getmTextoFieldEntrenador;
    private android.support.design.widget.FloatingActionButton verTrenador;
    private android.support.design.widget.FloatingActionButton btnBorrar;
    //private android.support.design.widget.FloatingActionButton btnTerminar;
    private Button btn_terminar;
    Long compromiso_id;
    Double latitud;
    Double longitude;

    String cliente;

    Compromiso compromiso;

    //Chat
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromiso);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextoFieldTitulo = findViewById(R.id.tv_title);
        mTextoFieldFecha = findViewById(R.id.tv_fecha);
        getmTextoFieldEntrenador = findViewById(R.id.tv_entrenador);
        mImagen = findViewById(R.id.iv_avatar);
        verTrenador = findViewById(R.id.fab_ver_entrenador);
        btnBorrar = findViewById(R.id.fab_borrar);
        btn_terminar = findViewById(R.id.btn_terminar);

        //CHAT
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        compromiso_id = intent.getLongExtra("mesg_compromiso_id",0);

        mAuth = FirebaseAuth.getInstance();

    //MEtodo para buscar un compromiso de la lista
    DatabaseUtil.findCompromisoById(compromiso_id, new DatabaseUtil.Listener() {
      @SuppressLint("RestrictedApi")
      @Override
      public void onCompromisoRetrieved(Compromiso c) {
          compromiso = c;
        //Log.d("********************",compromiso.getId());
          mTextoFieldTitulo.setText(compromiso.getTitle());
          mTextoFieldFecha.setText(compromiso.getFecha().toString());
          latitud = compromiso.getLatitud();
          longitude = compromiso.getLongitud();

          cliente = compromiso.getParticipantes().get(0);

          Usuario u = compromiso.getEntrenador();
          if(u != null) {
              getmTextoFieldEntrenador.setText(u.getNombre());
              verTrenador.setVisibility(View.GONE);
              btnBorrar.setVisibility(View.GONE);
              if(u.getUsuarioID().equals(mAuth.getUid())){
                  btnBorrar.setVisibility(View.VISIBLE);
              }
              RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.ratingBar); // initiate a rating bar
              simpleRatingBar.setVisibility(View.GONE);
              float puntuacion = u.getPuntuacion();
              if( puntuacion > 0) {
                  simpleRatingBar.setRating(puntuacion);
                  simpleRatingBar.setVisibility(View.VISIBLE);
              }
              if (mAuth.getUid().equals(u.usuarioID)) {
                  readMessages(mAuth.getUid(), compromiso.getParticipantes().get(0));
              }
              else{
                  readMessages(mAuth.getUid(), u.usuarioID);
              }

          }
          else {
              getmTextoFieldEntrenador.setText("");
              verTrenador.setVisibility(View.GONE);
          }

          Date fecha_actividad = compromiso.getFecha();
          Calendar calendar = Calendar.getInstance(Locale.US);
          Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
          btnBorrar.setVisibility(View.VISIBLE);
          if (fecha_actividad.compareTo(fecha_ahora) == -1) {//Si es una actividad pasada no podemos cancelarla
              btnBorrar.setVisibility(View.GONE);
          }


          Glide.with(getApplicationContext()).load(compromiso.getImage()).into(mImagen);
      }
    });




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


        //CHAT
        btn_send.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String message = text_send.getText().toString();
                if(!message.equals("")){
                    if(compromiso.getEntrenador().getUsuarioID().equals(mAuth.getUid())) {//El usuario actual es el entrenador
                        sendMessage(mAuth.getUid(), compromiso.getParticipantes().get(0),message);
                    }
                    else{
                        sendMessage(mAuth.getUid(), compromiso.getEntrenador().getUsuarioID(),message);
                    }
                }
                else{
                    Toast.makeText(CompromisoActivity.this, "Você não pode enviar mensagens vazias", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

    }


    private void sendMessage (String sender, String receiver, String  message){

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        Calendar calendar = Calendar.getInstance(Locale.US);
        Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
        String strDt = simpleDate.format(fecha_ahora);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCompromisoChat = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/Chat/"+strDt);
        dbCompromisoChat.setValue(hashMap);

        //reference.child("Compromisos").child("Compromiso_"+compromiso.getId()).child("Chat").push().setValue(hashMap);
    }

    private void readMessages (final String myid, final String userId){
        mChat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/Compromisos/Compromiso_" + compromiso_id + "/Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userId) ||
                        chat.getReceiver().equals(userId) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter( CompromisoActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onActionVolver(View view) {

        if(tipo_usuario.equals("cliente")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, TrainerPerfilMenuActivity.class);
            startActivity(intent);
        }

    }

    public void onActionTerminar(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCompromisoEstado = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/estado");
        dbCompromisoEstado.setValue("evaluacion");
        DatabaseReference dbEntrenador = database.getReference("/Users/"+mAuth.getUid()+"/ocupado");
        dbEntrenador.setValue(false);
        DatabaseReference dCliente = database.getReference("/Users/"+cliente+"/ocupado");
        dCliente.setValue(false);



        if(tipo_usuario.equals("cliente")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, TrainerPerfilMenuActivity.class);
            startActivity(intent);
        }


    }


    public void onActionBorrar(final View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCompromiso = database.getReference("/Compromisos/Compromiso_" + compromiso_id+ "/estado");
        dbCompromiso.setValue("cancelado");//Cambiamos estado a cancelado

        if(tipo_usuario.equals("cliente")) {
            Intent intent = new Intent(view.getContext(), MainMapActivity.class);
            startActivity(intent);
        }
        else{
           Intent intent = new Intent(view.getContext(), TrainerPerfilMenuActivity.class);
           startActivity(intent);
       }

        //CODIGO PARA BORRAR
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        Query applesQuery = ref.child("Compromisos").orderByChild("compromisoID").equalTo(compromiso_id);
//
//        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                    if(tipo_usuario.equals("cliente")) {
//                        Intent intent = new Intent(view.getContext(), MainMapActivity.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Intent intent = new Intent(view.getContext(), TrainerPerfilMenuActivity.class);
//                        startActivity(intent);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("DB.Error", "onCancelled", databaseError.toException());
//            }
//        });

    }

    public void onActionVerEnMapa(View view) {

        if(tipo_usuario.equals("cliente")) {
            Intent intent = new Intent(this, MainMapActivity.class);
            intent.putExtra("currentLatitude", latitud);
            intent.putExtra("currentLongitude", longitude);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(view.getContext(), TrainerMenuActivity.class);
            intent.putExtra("currentLatitude", latitud);
            intent.putExtra("currentLongitude", longitude);
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
}
