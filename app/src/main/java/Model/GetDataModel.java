package Model;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import com.darkwinter.bookfilms.Films;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hieum on 11/1/2017.
 */

public class GetDataModel {
    private DatabaseReference mREF, mCinema, mSlot, mSeat ,mRoomRef;

    public GetDataModel() {
    }

    public interface IGetFilm{
        void onChildAdded(Films film);
    }

    public interface IGetDate{
        void onChildAdded(String date);
        void onChildRemoved(String date);
    }

    public interface IGetCinema{
        void onChildAdded(String cinema);
    }

    public interface IGetSlot{
        void onChildAdded(String slot);
        void onChildRemoved(String slot);
    }

    public interface IGetSeat{
        void getRoom(String room);
        void getSeat(String seat);
    }

    public void getFilm(final IGetFilm IFilm){
        mREF = FirebaseDatabase.getInstance().getReference().child("Films");
        mREF.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                    String id = data.getKey();
                    String name = data.child("Name").getValue().toString();
                    String Image = data.child("Picture").child("image1").getValue().toString();
                    String Descrip = data.child("Description").getValue().toString();
                    String Duration = data.child("Duration").getValue().toString();
                    String Producer = data.child("Producer").getValue().toString();
                    String Rating = data.child("Rating").getValue().toString();
                    String Trailer = data.child("Trailers").getValue().toString();
                    Films film = new Films(id, name, Image, Descrip, Duration, Producer, Rating, Trailer);
                    if(IFilm != null){
                        IFilm.onChildAdded(film);
                    }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDate(Films film, final IGetDate IDate){
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        mREF = FirebaseDatabase.getInstance().getReference().child("Films").child(film.getId()).child("Dates");
        mREF.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Date tmp = null;
                try {
                    tmp = sdf.parse(dataSnapshot.getKey());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(Calendar.getInstance().getTime().before(tmp) && IDate != null) {
                    IDate.onChildAdded(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(IDate != null){
                    IDate.onChildRemoved(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCinema(Films film, String date, final IGetCinema Icinema){
        mCinema = FirebaseDatabase.getInstance().getReference().child("Films").child(film.getId()).child("Dates").child(date);
        mCinema.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String Cinema = dataSnapshot.getKey();
                if(Icinema != null){
                    Icinema.onChildAdded(Cinema);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getSlot(String Film, String date, String cine, final IGetSlot Islot){
        mSlot = FirebaseDatabase.getInstance().getReference()
                .child("Cinemas").child(cine)
                .child("Films").child(Film)
                .child(date);
        mSlot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String slot = dataSnapshot.getKey();
                if(Islot != null){
                    Islot.onChildAdded(slot);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(Islot != null) {
                    Islot.onChildRemoved(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getSeat(String Film, final String date, String cine, final String time, final IGetSeat ISeat){
        mSeat = FirebaseDatabase.getInstance().getReference()
                .child("Cinemas").child(cine)
                .child("Films").child(Film)
                .child(date).child(time);
        mSeat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String room = dataSnapshot.getKey();
                if(ISeat != null){
                    ISeat.getRoom(room);
                }
                mRoomRef = FirebaseDatabase.getInstance().getReference()
                        .child("Roms").child(room).child("Dates")
                        .child(date).child(time);
                mRoomRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String isEmp = dataSnapshot.getValue().toString();
                        String seat = dataSnapshot.getKey().toString();
                        if(isEmp.equals("1")){
                            if(ISeat != null){
                                ISeat.getSeat(seat);
                            }
                            /*ArrSeat.add(seat);
                            SeatAdapter.notifyDataSetChanged();*/
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    }

