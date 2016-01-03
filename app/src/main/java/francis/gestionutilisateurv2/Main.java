package francis.gestionutilisateurv2;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.parse.Parse;

public class Main extends Application {


    Activity_list actList = new Activity_list();
    Intent monIntent;

    @Override
    public void onCreate() {
        super.onCreate();

// Enable Local Datastore.
        Parse.initialize(this, "WFwmLbPapsykd8CvDaoy9OmFrQgzQnpYRXAHgr5L", "uSzyCWkVldJIJCfXoqBw7kjxrSNyZGXbRGYfM47e");


    }





}