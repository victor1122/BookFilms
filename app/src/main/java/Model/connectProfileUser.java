package Model;

import android.app.Activity;
import android.util.Log;

import com.darkwinter.bookfilms.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by DarkWinter on 11/1/17.
 */

public class connectProfileUser {
    private FirebaseDatabase database= FirebaseDatabase.getInstance();

    private DatabaseReference mUserReference = database.getReference("Users");

    public ConnectDB connectDB = new ConnectDB();

    public User currentUser = new User();

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public interface OnLoadedUser {
        void onLoadedUser(String ID, User user);
    }

    public void loadCurrentUser(final OnLoadedUser onLoadedUser){
        final FirebaseUser user = connectDB.mAuth.getCurrentUser();
        currentUser = new User();
        if(user != null){
            mUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = new User();
                    final String ID = user.getUid();
                    DataSnapshot userDataSnapshot = dataSnapshot.child(ID);
                    currentUser.setEmail(user.getEmail());
                    currentUser.setName(userDataSnapshot.child("name").getValue().toString());
                    currentUser.setDOB(userDataSnapshot.child("DOB").getValue().toString());
                    currentUser.setPhone(userDataSnapshot.child("Phone number").getValue().toString());
                    currentUser.setZipcode(userDataSnapshot.child("CitizenID").getValue().toString());
                    if(onLoadedUser!=null){
                        onLoadedUser.onLoadedUser(ID, currentUser);
                        Log.d("currentUser", currentUser.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


}
