package com.darkwinter.bookfilms;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import Model.TicketModel;

public class ConfirmInfoActivity extends AppCompatActivity {

    private TextView Film, Date, Cine, Slot, Seat;
    private String Room;
    private Button Confirm;
    private DatabaseReference mUserRef;
    private DatabaseReference mRoomRef;
    private int notiID;
    private FirebaseAuth mAuth;
    AlarmManager alarmManager;

    private PendingIntent pending_intent;
    Context context;
    ConfirmInfoActivity inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_info);
        setupWidget();
        Intent receiveIntent = getIntent();
        Bundle bundle = receiveIntent.getBundleExtra("infoticket");
        Film.setText(bundle.getString("Film"));
        Date.setText(bundle.getString("Date"));
        Cine.setText(bundle.getString("Cinema"));
        Slot.setText(bundle.getString("Slot"));
        Room = bundle.getString("Room");
        Seat.setText(bundle.getString("Seat"));

    }

    private void setupWidget(){
        Film = (TextView)findViewById(R.id.txtFilm);
        Date = (TextView)findViewById(R.id.txtDate);
        Cine = (TextView)findViewById(R.id.txtCinema);
        Slot = (TextView)findViewById(R.id.txtSlot);
        Seat = (TextView)findViewById(R.id.txtSeat);
        Confirm = (Button)findViewById(R.id.btnConfirm);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                updateUserTicket();
                AlarmClock();
            }
        });
    }

    private void updateUserTicket() {
        HashMap<String, Object> tmp = new HashMap<>();
        tmp.put(Seat.getText().toString(), "0");
        TicketModel md = new TicketModel();
        HashMap<String, Object> ticket = new HashMap<>();
        ticket.put("Film", Film.getText().toString());
        ticket.put("Date", Date.getText().toString());
        ticket.put("Cinema", Cine.getText().toString());
        ticket.put("Slot", Slot.getText().toString());
        ticket.put("Room", Room);
        ticket.put("Seat", Seat.getText().toString());
        md.bookNewTicket(tmp, ticket, Room, Date.getText().toString(), Slot.getText().toString(),
                new TicketModel.IbookTicket() {
            @Override
            public void onComplete() {
                Intent fini = new Intent(ConfirmInfoActivity.this, MainActivity.class);
                startActivity(fini);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    public void AlarmClock(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.usa).setContentTitle("Have message").setContentText("ahihi").setTimeoutAfter(1000);
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(mPendingIntent);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);

        notiID = 113;

        NotificationManager mNofityMn = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNofityMn.notify(notiID, mBuilder.build());

        // Push notification after 45 minutes
        this.context = this;

        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        //int hour = Calendar.HOUR;
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        //int minute = Calendar.MINUTE  + 2;
        Log.e("MyActivity", "In the receiver with " + hours + " and " + minutes);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        myIntent.putExtra("extra", "yes");
        pending_intent = PendingIntent.getBroadcast(ConfirmInfoActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

        alarmManager.cancel(pending_intent);
    }



}
