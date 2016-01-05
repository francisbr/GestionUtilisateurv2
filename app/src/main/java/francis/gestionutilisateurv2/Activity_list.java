package francis.gestionutilisateurv2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.nearby.Nearby.CONNECTIONS_API;

/**
 * Created by Francis on 2015-12-28.
 */
public class Activity_list extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener {
    public static ArrayList<Personne> listeUsers = new ArrayList();

    Intent actAdd;

    GoogleApiClient mGoogleApiClient;

    protected static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;
    private boolean mIsHost = false;
    String activityName = "Activity_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actAdd = new Intent(this, Activity_add_user.class);

        setContentView(R.layout.activity_list);

// Create a GoogleApiClient instance

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(CONNECTIONS_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            mGoogleApiClient.connect();


        Log.d(activityName, "onCreate()");

    }



    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET};

    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
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
                        String nom = usersList.get(i).get("Nom").toString();
                        String role = usersList.get(i).get("Role").toString();

                        Log.d("score", nom + " " + role);
                        getListeUsers().add(new Personne(nom,role));


                    }
                    updateListView();
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
        Log.d("Google play", "Connection Failed:"+connectionResult.toString());

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
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
        Log.d(activityName, "onActivityResult()");
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                handleResultConnectionResult(resultCode);
                break;
            default:
                Log.w(activityName, "onActivityResult " + requestCode);
        }

    }
    private void handleResultConnectionResult(int resultCode) {
        switch (resultCode) {
            case RESULT_OK:
                connectGoogleApiClient();
                break;
            default:
                Log.w(activityName, "Canceled request to connect to Google Play Services: " + resultCode);
        }
    }
        /**
         * Initiates a connection request (if not already connected) that will result in a call to
         * {@link #onClientConnected}.
         */
    protected void connectGoogleApiClient() {
            if (!mGoogleApiClient.isConnected()) {
                Log.i(activityName, "Connecting to GoogleApiClient");
                mGoogleApiClient.connect();
            } else {
                Log.i(activityName, "Is Connected to GoogleApiClient");

            }

    }

    private void startAdvertising() {
        if (!isConnectedToNetwork()) {
            // Implement logic when device is not connected to a network
        }

        // Identify that this device is the host
        mIsHost = true;

        // Advertising with an AppIdentifer lets other devices on the
        // network discover this application and prompt the user to
        // install the application.
        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(getPackageName()));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        // The advertising timeout is set to run indefinitely
        // Positive values represent timeout in milliseconds
        long NO_TIMEOUT = 0L;

        String name = null;
        Nearby.Connections.startAdvertising(mGoogleApiClient, name, appMetadata, NO_TIMEOUT,
                this).setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
            @Override
            public void onResult(Connections.StartAdvertisingResult result) {
                if (result.getStatus().isSuccess()) {
                    // Device is advertising
                } else {
                    int statusCode = result.getStatus().getStatusCode();
                    // Advertising failed - see statusCode for more details
                }
            }
        });
    }

    @Override
    public void onConnectionRequest(String s, String s1, String s2, byte[] bytes) {

    }

    @Override
    public void onEndpointFound(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onEndpointLost(String s) {

    }

    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {

    }

    @Override
    public void onDisconnected(String s) {

    }
}
