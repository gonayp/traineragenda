package com.traineragenda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class EntradaActivity extends AppCompatActivity {
  private FirebaseAuth mAuth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FirebaseApp.initializeApp(this);
    mAuth = FirebaseAuth.getInstance();

    setContentView(R.layout.activity_entrada);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


  }


  private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "Chanel_1";//getString(R.string.channel_name);
      String description = getString(R.string.app_actividades);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("Chanel_1", name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
      Intent intent = new Intent(this, DivisorActivity.class);
      startActivity(intent);
    }
  }

  /** Called when the user taps the Send button */
  public void onActionLogin(View view) {
    Intent intent = new Intent(this, EmailLoginActivity.class);
    startActivity(intent);

  }

  /** Called when the user taps the Send button */
  public void onActionRegister(View view) {
    try {

      Intent intent = new Intent(this, EntradaregistroActivity.class);
      startActivity(intent);
    }
    catch (Exception e){
      FirebaseDatabase database =  FirebaseDatabase.getInstance();
      Date now = new Date();
      FirebaseUser user = mAuth.getCurrentUser();
      String userId;
      if(user == null){
        userId = "Sin usuario";
      }
      else{
        userId = user.getUid();
      }
      String message = e.getMessage();
      sendEmail(message);
//            String cause = e.getLocalizedMessage();
//            String message = e.getMessage();
//            Error error = new Error("Error",cause,message,userId,this.getClass().getSimpleName());
//
//            DatabaseReference mRef =  database.getReference().child("Errores").child(now.toString());
//            mRef.setValue(error);


    }


  }

  protected void sendEmail(String mensaje) {
    Log.i("Send email", "");

    String[] TO = {"gonayp@gmail.com"};
    String[] CC = {"gonayp@gmail.com"};
    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setData(Uri.parse("mailto:"));
    emailIntent.setType("text/plain");


    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
    emailIntent.putExtra(Intent.EXTRA_CC, CC);
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
    emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

    try {
      startActivity(Intent.createChooser(emailIntent, "Send mail..."));
      finish();
      Log.i("Finished sending email", "");
    } catch (android.content.ActivityNotFoundException ex) {
      Toast.makeText(this,
              "There is no email client installed.", Toast.LENGTH_SHORT).show();
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
