package Model;

/**
 * Created by DarkWinter on 11/1/17.
 */
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.darkwinter.bookfilms.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;



public class ConnectDB {
    public String curuid;
    public String uid;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DatabaseReference databaseReference;
    private Context context;
    private  Activity activity;
    public int flag = 0;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ConnectDB(){

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface OnLoadedUser {
        void onLoadedUser(Task task);
    }

    public void  pushCurrentUser( String email, String password,final OnLoadedUser onLoadedUser){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((this.activity), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onLoadedUser.onLoadedUser(task);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("+++++++", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    public void saveDataUser(final String email, final String name, final String pass, final String dob, final String phone, final String zipcode, final OnLoadedUser onLoadedUser) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = current_user.getUid();
                    Log.d("this is ID in login", uid);
                    saveData(email, name, dob, phone, zipcode, uid );
                    onLoadedUser.onLoadedUser(task);
                }
            }
        });
    }
    public void saveData(String email, String name, String dob, String phone, String zipcode, String uid){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("name", name);
        userMap.put("CitizenID", zipcode );
        userMap.put("DOB", dob);
        userMap.put("Phone number", phone);
        databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else {

                }
            }
        });
    }
}
