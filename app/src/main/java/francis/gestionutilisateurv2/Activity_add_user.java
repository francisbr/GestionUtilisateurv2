package francis.gestionutilisateurv2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by Francis on 2015-12-28.
 */
public class Activity_add_user extends Activity {


    ParseObject data_users = new ParseObject("Users");

    String activityName = "Activity_add_user";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_add_user);

        Log.d(activityName, "onCreate()");
    }



    public void setLocation(){

    }

    public void cancel(View view){
        Toast myToast = Toast.makeText(getApplicationContext(),"Canceled", Toast.LENGTH_SHORT);

        myToast.show();
        setListWindow(view);
    }

    public void setListWindow(View view){
        finish();
    }

    public void addUser(View view) throws ParseException{
        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        Spinner s = (Spinner) findViewById(R.id.selectionDuRole);

        String nom = editNom.getText().toString();
        String role = s.getSelectedItem().toString();



        Personne personne = new Personne(nom,role);


        uploadPersonne(personne);
        editNom.setText("");

        Toast myToast = Toast.makeText(getApplicationContext(),"User added", Toast.LENGTH_SHORT);

        myToast.show();

        setListWindow(view);
    }

    public void uploadPersonne(Personne user) throws ParseException {

            data_users.put("Nom", user.getNom());
            data_users.put("Role", user.getRole());


            data_users.save();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(activityName, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(activityName, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(activityName, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(activityName, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(activityName, "onDestroy()");
    }

}
