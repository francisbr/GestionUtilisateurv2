package francis.gestionutilisateurv2;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francis on 2015-12-28.
 */
public class Activity_list extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static ArrayList<Personne> listeUsers = new ArrayList();

    Intent actAdd;

    GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    String activityName = "Activity_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actAdd = new Intent(this, Activity_add_user.class);

        setContentView(R.layout.activity_list);

// Create a GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mGoogleApiClient.connect();


        Log.d(activityName, "onCreate()");

    }

    public void setGetNomWindow(View view){

        startActivity(actAdd);
    }

    public void updateListView(){
        ListView listeView = (ListView) findViewById(R.id.myListView);

        ArrayList<String> listItems = new ArrayList();


        for (int i = 0 ; i < listeUsers.size() ; i++){
            listItems.add(listeUsers.get(i).getNom().toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        listeView.setAdapter(adapter);

    }

    public void downloadList() {

        getListeUsers().clear();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");



        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> usersList, ParseException e) {
                if (e == null) {

                    for(int i=0;i<usersList.size();i++) {
                        try {
                            String nom = query.find().get(i).get("Nom").toString();
                            String role = query.find().get(i).get("Role").toString();

                            getListeUsers().add(new Personne(nom,role));

                            updateListView();

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }


            }
        });
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
        downloadList();
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

    public static ArrayList<Personne> getListeUsers() {
        return listeUsers;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Google play", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Google play", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
                    if (!mGoogleApiClient.isConnecting() &&
                            !mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }
                } else {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

}
