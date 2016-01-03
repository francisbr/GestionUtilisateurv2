package francis.gestionutilisateurv2;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

/**
 * Created by Francis on 2015-12-28.
 */
public class Personne {

    protected String nom;
    protected String role;
    Location userLocation;

    public Personne(String nom, String role) {
        this.nom = nom;
        this.role = role;
    }




    public String getNom() {
        return nom;
    }

    public String getRole() {
        return role;
    }
}
