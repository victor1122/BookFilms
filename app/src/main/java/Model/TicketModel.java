package Model;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.darkwinter.bookfilms.ConfirmInfoActivity;
import com.darkwinter.bookfilms.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by hieum on 11/2/2017.
 */

public class TicketModel {
    private DatabaseReference mUserRef;
    private DatabaseReference mRoomRef;

    public TicketModel() {
    }

    public interface IbookTicket{
        void onComplete();
    }

    public void bookNewTicket(HashMap<String,Object> seat, HashMap<String,Object> ticket, String Room, String Date, String Slot, final IbookTicket Iticket){
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mRoomRef = FirebaseDatabase.getInstance().getReference()
                .child("Roms")
                .child(Room)
                .child("Dates")
                .child(Date)
                .child(Slot);
        mRoomRef.updateChildren(seat);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Tickets");
        mUserRef.push().updateChildren(ticket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful() && Iticket != null){
                    Iticket.onComplete();
                }
            }
        });
    }

}
