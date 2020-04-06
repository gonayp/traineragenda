package com.traineragenda;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.wheel.WheelView;
import com.traineragenda.wheel.adapters.AbstractWheelTextAdapter;
import com.traineragenda.wheel.adapters.ArrayWheelAdapter;
import com.traineragenda.wheel.adapters.NumericWheelAdapter;

public class AgendarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Double vl_latitude ;
    Double vl_longitude;
    Long cantidad_compromisos;

    Calendar calendar;

    WheelView hours;
    WheelView mins;
    WheelView day;


    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        vl_latitude = bundle.getDouble("currentLatitude");
        vl_longitude = bundle.getDouble("currentLongitude");


        calendar = Calendar.getInstance(Locale.US);


        hours = (WheelView) findViewById(R.id.hour);
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23, "%02d");
        hourAdapter.setItemResource(R.layout.wheel_text_item);
        hourAdapter.setItemTextResource(R.id.text);
        hours.setViewAdapter(hourAdapter);

        mins = (WheelView) findViewById(R.id.mins);
        NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
        minAdapter.setItemResource(R.layout.wheel_text_item);
        minAdapter.setItemTextResource(R.id.text);
        mins.setViewAdapter(minAdapter);
        mins.setCyclic(true);

        final WheelView ampm = (WheelView) findViewById(R.id.ampm);
        ArrayWheelAdapter<String> ampmAdapter =
                new ArrayWheelAdapter<String>(this, new String[]{"AM", "PM"});
        ampmAdapter.setItemResource(R.layout.wheel_text_item);
        ampmAdapter.setItemTextResource(R.id.text);
        ampm.setViewAdapter(ampmAdapter);


        hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
        mins.setCurrentItem(calendar.get(Calendar.MINUTE));
        ampm.setCurrentItem(calendar.get(Calendar.AM_PM));


        day = (WheelView) findViewById(R.id.day2);
        day.setViewAdapter(new DayArrayAdapter(this, calendar));


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
            public void onDataChange(DataSnapshot dataSnapshot) {

                cantidad_compromisos = dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ERROR FIREBASE", error.getMessage());
            }

        });


        DatabaseReference myRef_user = fdb.getReference("Users");

        myRef_user.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot d = dataSnapshot.child(mAuth.getUid());
                u = d.getValue(Usuario.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onActionVolver(View view) {
        Intent intent = new Intent(this, MainMapActivity.class);
        startActivity(intent);

    }

    public void onActionGrabar(View view) {

        if(u.getOcupado() == false) {

            Context context = getApplicationContext();
            //CharSequence text = (String) R.string.guardar_agendamento;
            int duration = Toast.LENGTH_SHORT;

            int h = hours.getCurrentItem();
            int m = mins.getCurrentItem();
            int d = day.getCurrentItem();//numero de dias desde hoy, para sumar a la fecha

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();


            Date fecha = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), h, m, 0);
            //Date fecha=calendar.getTime();
            Date fecha_final = sumarRestarDiasFecha(fecha, d);
            LatLng coordenadas = new LatLng(vl_latitude, vl_longitude);

            //Codigo para crear un compromiso nuevo
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            List<String> mParticipantes = new ArrayList<String>();
            mParticipantes.add(userId);
            Long compromiso_id = cantidad_compromisos + 1;
            Compromiso a = new Compromiso(compromiso_id, "Corrida", mParticipantes, fecha_final, coordenadas.latitude, coordenadas.longitude, "asd", R.drawable.baseline_airline_seat_recline_extra_black_18dp, "pendiente", null);
            a.setEstado("pendiente");
            a.setVisto(false);
            DatabaseReference mRef = database.getReference().child("Compromisos").child("Compromiso_" + (compromiso_id));
            mRef.setValue(a);

            Toast toast = Toast.makeText(context, R.string.guardar_agendamento, duration);
            toast.show();

            Intent intent = new Intent(this, MainMapActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(AgendarActivity.this, "Você deve terminar a atividade atual primeiro.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Day adapter
     *
     */
    private class DayArrayAdapter extends AbstractWheelTextAdapter {
        // Count of days to be shown
        private final int daysCount = 20;

        // Calendar
        Calendar calendar;

        /**
         * Constructor
         */
        protected DayArrayAdapter(Context context, Calendar calendar) {
            super(context, R.layout.time2_day, NO_RESOURCE);
            this.calendar = calendar;

            setItemTextResource(R.id.time2_monthday);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            int day =  index;//-daysCount/2 +
            Calendar newCalendar = (Calendar) calendar.clone();
            newCalendar.roll(Calendar.DAY_OF_YEAR, day);

            View view = super.getItem(index, cachedView, parent);
            TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
            if (day == 0) {
                weekday.setText("");
            } else {
                DateFormat format = new SimpleDateFormat("EEE");
                weekday.setText(format.format(newCalendar.getTime()));
            }

            TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
            if (day == 0) {
                monthday.setText(R.string.hoy);
                monthday.setTextColor(0xFF0000F0);
            } else {
                DateFormat format = new SimpleDateFormat("MMM d");
                monthday.setText(format.format(newCalendar.getTime()));
                monthday.setTextColor(0xFF111111);
            }

            return view;
        }

        @Override
        public int getItemsCount() {
            return daysCount + 1;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return "";
        }
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    // Suma los días recibidos a la fecha
    public Date sumarRestarDiasFecha(Date fecha, int dias){

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha); // Configuramos la fecha que se recibe

        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos



    }

}
