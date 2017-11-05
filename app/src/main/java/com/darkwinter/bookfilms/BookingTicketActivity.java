package com.darkwinter.bookfilms;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Model.GetDataModel;

public class BookingTicketActivity extends AppCompatActivity {

    private Spinner Cinemas, Seats, Times, Dates;
    private Button Booking;
    private Films film;
    private String Room;
    private ArrayList<String> ListCinemas = new ArrayList<>();
    private ArrayList<String> ListDates = new ArrayList<>();
    private ArrayList<String> ListSlots = new ArrayList<>();
    private ArrayList<String> ListSeats = new ArrayList<>();
    private ArrayAdapter<String> CinemaAdater, DateAdapter, SlotAdapter, SeatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ticket);
        film = (Films) getIntent().getSerializableExtra("Film");
        setupWidget();
        new AsyncLoadDatesFirebase().execute();
        Dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tmpDate = adapterView.getItemAtPosition(i).toString();
                loadCinemasFromFirebase(tmpDate);
                notifychange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Cinemas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cinema = adapterView.getItemAtPosition(i).toString();
                loadSlotFirebase(cinema, film.getId(), Dates.getSelectedItem().toString());
                notifychange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadSeatsFromFirebase(film.getId(),
                        Dates.getSelectedItem().toString(),
                        Cinemas.getSelectedItem().toString(), Times.getSelectedItem().toString());
                clearSeat();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void loadCinemasFromFirebase(String date){
        ListCinemas.clear();
        GetDataModel md = new GetDataModel();
        md.getCinema(film, date, new GetDataModel.IGetCinema() {
            @Override
            public void onChildAdded(String cinema) {
                ListCinemas.add(cinema);
                notifychange();
            }
        });
    }

    private void loadSeatsFromFirebase(String film, String date, String cinema, String slot) {
        ListSeats.clear();
        GetDataModel md = new GetDataModel();
        md.getSeat(film,
                date, cinema, slot, new GetDataModel.IGetSeat() {
                    @Override
                    public void getRoom(String room) {
                        Room = room;
                    }

                    @Override
                    public void getSeat(String seat) {

                        ListSeats.add(seat);
                        notifychange();
                    }
                });
    }

    private class AsyncLoadDatesFirebase extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ListDates.clear();
            GetDataModel md = new GetDataModel();
            md.getDate(film, new GetDataModel.IGetDate() {
                @Override
                public void onChildAdded(String date) {
                    ListDates.add(date);
                    DateAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(String date) {
                    ListDates.remove(date);
                    DateAdapter.notifyDataSetChanged();
                }
            });
            return null;
        }
    }

    private void loadSlotFirebase(String cine, String Film, String date){
        ListSlots.clear();
        GetDataModel md = new GetDataModel();
        md.getSlot(Film, date, cine, new GetDataModel.IGetSlot() {
            @Override
            public void onChildAdded(String slot) {
                ListSlots.add(slot);
                notifychange();
            }

            @Override
            public void onChildRemoved(String slot) {
                ListSlots.remove(slot);
                notifychange();
            }
        });
    }

    private void clearTime(){
        ListSlots.clear();
        notifychange();
    }

    private void clearSeat(){
        ListSeats.clear();
        notifychange();
    }

    private void setupWidget(){
        Cinemas = (Spinner) findViewById(R.id.spinCinema);
        Seats = (Spinner)findViewById(R.id.spinSeat);
        Times = (Spinner)findViewById(R.id.spinTime);
        Dates = (Spinner) findViewById(R.id.spinDate);
        Booking = (Button) findViewById(R.id.btnBookAction);
        Booking = (Button) findViewById(R.id.btnBookAction);
        DateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListDates);
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Dates.setAdapter(DateAdapter);

        CinemaAdater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListCinemas);
        CinemaAdater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cinemas.setAdapter(CinemaAdater);

        SlotAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListSlots);
        SlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Times.setAdapter(SlotAdapter);

        SeatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListSeats);
        SeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Seats.setAdapter(SeatAdapter);

        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ConfirmIntent;
                ConfirmIntent = new Intent(BookingTicketActivity.this, ConfirmInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Film", film.getId());
                bundle.putString("Date", Dates.getSelectedItem().toString());
                bundle.putString("Cinema", Cinemas.getSelectedItem().toString());
                bundle.putString("Slot", Times.getSelectedItem().toString());
                bundle.putString("Room", Room);
                bundle.putString("Seat", Seats.getSelectedItem().toString());
                ConfirmIntent.putExtra("infoticket",bundle );
                startActivity(ConfirmIntent);
                finish();
            }
        });


    }

    private void notifychange(){
        DateAdapter.notifyDataSetChanged();
        CinemaAdater.notifyDataSetChanged();
        SlotAdapter.notifyDataSetChanged();
        SeatAdapter.notifyDataSetChanged();
    }



}
