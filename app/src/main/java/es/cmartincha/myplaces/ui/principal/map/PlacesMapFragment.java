
package es.cmartincha.myplaces.ui.principal.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;
import es.cmartincha.myplaces.ui.dialog.LocationErrorDialogFragment;
import es.cmartincha.myplaces.ui.place.EditPlaceActivity;
import es.cmartincha.myplaces.ui.place.ShowPlaceActivity;
import es.cmartincha.myplaces.ui.principal.PrincipalActivityFragment;

/**
 * Clase que muestra el mapa de lugares
 */
public class PlacesMapFragment extends SupportMapFragment implements OnMapClickListener,
        OnMarkerClickListener, PrincipalActivityFragment,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationSource, LocationListener {

    private static final int REQUEST_EDIT_PLACE = 1;
    private static final float DEFAULT_ZOOM = 14;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final long UPDATE_INTERVAL_MILIS = 5000;
    private static final long FASTEST_INTERVAL_MILIS = 1000;

    private GoogleMap mMap;
    private List<Place> mPlacesList;
    private Map<String, Place> mMarkerPlaceHash;
    private LocationClient mLocationClient;
    private OnLocationChangedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mMarkerPlaceHash = new HashMap<String, Place>();
        mLocationClient = new LocationClient(getActivity(), this, this);

        setPadding(rootView);
        //Evita que se destruya el fragment al volver a crear la actividad
        setRetainInstance(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpMapIfNeeded();
    }

    private void setPadding(View rootView) {
        int horizontalPadding = (int) getResources().getDimension(
                R.dimen.activity_horizontal_margin);
        int verticalPadding = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

        rootView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
    }

    @Override
    /**
     * Se llama cuando google maps activa el servicio de localizacion
     */
    public void activate(OnLocationChangedListener listener) {
        connectLocationService();

        mListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        mListener.onLocationChanged(location);

        //Actualizo la posicion y el zoom del mapa para que muestre la posicion actual
        updateMapCamera(location);
    }

    private void updateMapCamera(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);

        mMap.moveCamera(cameraUpdate);
    }

    @Override
    /**
     * Se llama cuando google maps desactiva el servicio de localizacion
     */
    public void deactivate() {
        stopLocationService();
    }

    private void stopLocationService() {
        removeLocationUpdates();
        mLocationClient.disconnect();
    }

    private void removeLocationUpdates() {
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
    }

    private void connectLocationService() {
        if (!mLocationClient.isConnected() && !mLocationClient.isConnecting() && servicesConnected()) {
            mLocationClient.connect();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        connectLocationService();
    }

    @Override
    public void onPause() {
        stopLocationService();

        super.onPause();
    }

    @Override
    public void onStop() {
        stopLocationService();

        super.onStop();
    }

    @Override
    public void onMapClick(LatLng point) {
        navigateToEditPlaceActivity(point);
    }

    private void navigateToEditPlaceActivity(LatLng point) {
        Intent intent = new Intent(getActivity(), EditPlaceActivity.class);
        Place place = new Place();

        place.setLatitude(point.latitude);
        place.setLongitude(point.longitude);

        intent.putExtra(EditPlaceActivity.EXTRA_PLACE_INFO, place.toBundle());

        startActivityForResult(intent, REQUEST_EDIT_PLACE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        navigateToShowPlaceActivity(marker);

        return false;
    }

    private void navigateToShowPlaceActivity(Marker marker) {
        Intent intent = new Intent(getActivity(), ShowPlaceActivity.class);
        Place lugar = mMarkerPlaceHash.get(marker.getId());

        intent.putExtra(ShowPlaceActivity.EXTRA_PLACE_INFO, lugar.toBundle());

        startActivity(intent);
    }

    @Override
    /**
     * Pinta los marcadores con la informacion actualizada
     */
    public void notififyDataChanged(Cursor data) {
        mMap.clear();

        if (data != null) {
            mPlacesList = Place.lugaresFromCursor(data);

            addMarkers();
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = getMap();

            if (mMap == null) {
                Toast.makeText(getActivity(), R.string.text_map_unavaliable, Toast.LENGTH_SHORT).show();
            } else {
                mMap.setOnMapClickListener(this);
                mMap.setOnMarkerClickListener(this);
                mMap.setMyLocationEnabled(true);
                mMap.setLocationSource(this);
            }
        }
    }

    private void addMarkers() {
        for (Place place : mPlacesList) {
            Marker marker = mMap.addMarker(getMarker(place));

            mMarkerPlaceHash.put(marker.getId(), place);
        }
    }

    private MarkerOptions getMarker(Place place) {
        return new MarkerOptions()
                .position(new LatLng(place.getLatitude(), place.getLongitude()))
                .title(place.getName()).snippet(place.getDescription());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //Si no ha habido problema en conectarse a los servicios de google play conecto el servicio de localizaion
                        connectLocationService();
                        break;
                }
        }
    }

    /**
     * Verifica que has podido conectarte a los servicios de google play. En caso de no poder muestra un dialogo con un error
     *
     * @return
     */
    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if (resultCode == ConnectionResult.SUCCESS) {
            return true;
        } else {
            int errorCode = resultCode;

            showErrorDialog(errorCode);

            return false;
        }
    }

    private void showErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, getActivity(),
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        if (errorDialog != null) {
            LocationErrorDialogFragment errorFragment = new LocationErrorDialogFragment();

            errorFragment.setDialog(errorDialog);
            errorFragment.show(getFragmentManager(), "Location Updates");
        }
    }

    @Override
    /**
     * Se llama si ha fallado la conexion al servicio de localizacion
     */
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    /**
     * Se llama cuando se ha conectado al servicio de localizacion
     */
    public void onConnected(Bundle dataBundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_MILIS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MILIS);

        mLocationClient.requestLocationUpdates(locationRequest, this);
    }

    @Override
    /**
     * Se llama cuando se ha desconectado del servicio de localizacion
     */
    public void onDisconnected() {
        removeLocationUpdates();
    }
}
