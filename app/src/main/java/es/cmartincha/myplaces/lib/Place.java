
package es.cmartincha.myplaces.lib;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import es.cmartincha.myplaces.db.PlacesDB;
import es.cmartincha.myplaces.db.PlacesProvider;

public class Place {
    private static final String PROPERTY_ID = "_id";
    private static final String PROPOERTY_NAME = "name";
    private static final String PROPERTY_DESCRIPTION = "description";
    private static final String PROPERTY_PHOTO = "photo";
    private static final String PROPERTY_LATITUDE = "latitude";
    private static final String PROPERTY_LONGITUDE = "longitude";

    private int _id;
    private String name;
    private String description;
    private String photo;
    private double latitude;
    private double longitude;

    public static Place fromBundle(Bundle bundle) {
        Place lugar = new Place();

        lugar._id = bundle.getInt(PROPERTY_ID);
        lugar.description = bundle.getString(PROPERTY_DESCRIPTION);
        lugar.photo = bundle.getString(PROPERTY_PHOTO);
        lugar.latitude = bundle.getDouble(PROPERTY_LATITUDE);
        lugar.longitude = bundle.getDouble(PROPERTY_LONGITUDE);
        lugar.name = bundle.getString(PROPOERTY_NAME);

        return lugar;
    }

    public static Place fromCursor(Cursor cursor) {
        Place lugar = new Place();

        lugar._id = cursor.getInt(cursor.getColumnIndex(PlacesDB.PlaceTable._ID));
        lugar.description = cursor.getString(cursor
                .getColumnIndex(PlacesDB.PlaceTable.COLUMN_DESCRIPTION));
        lugar.photo = cursor.getString(cursor.getColumnIndex(PlacesDB.PlaceTable.COLUMN_PHOTO));
        lugar.latitude = cursor
                .getDouble(cursor.getColumnIndex(PlacesDB.PlaceTable.COLUMN_LATITUDE));
        lugar.longitude = cursor.getDouble(cursor
                .getColumnIndex(PlacesDB.PlaceTable.COLUMN_LONGITUDE));
        lugar.name = cursor.getString(cursor.getColumnIndex(PlacesDB.PlaceTable.COLUMN_NAME));

        return lugar;
    }

    public static List<Place> lugaresFromCursor(Cursor cursor) {
        ArrayList<Place> lugares = new ArrayList<Place>();

        if (cursor.moveToFirst()) {
            do {
                lugares.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return lugares;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String ph) {
        photo = ph;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double l) {
        latitude = l;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double l) {
        longitude = l;
    }

    public boolean hasPhoto() {
        return photo != null && !TextUtils.isEmpty(photo);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putInt(PROPERTY_ID, _id);
        bundle.putString(PROPOERTY_NAME, name);
        bundle.putString(PROPERTY_DESCRIPTION, description);
        bundle.putString(PROPERTY_PHOTO, photo);
        bundle.putDouble(PROPERTY_LATITUDE, latitude);
        bundle.putDouble(PROPERTY_LONGITUDE, longitude);

        return bundle;
    }

    public long insert(Context context) {
        Uri providerUri = Uri.parse("content://" + PlacesProvider.AUTHORITY + "/"
                + PlacesDB.PlaceTable.TABLE_NAME);

        ContentValues insertValues = new ContentValues();

        insertValues.put(PlacesDB.PlaceTable.COLUMN_DESCRIPTION, description);
        insertValues.put(PlacesDB.PlaceTable.COLUMN_PHOTO, photo);
        insertValues.put(PlacesDB.PlaceTable.COLUMN_LATITUDE, latitude);
        insertValues.put(PlacesDB.PlaceTable.COLUMN_LONGITUDE, longitude);
        insertValues.put(PlacesDB.PlaceTable.COLUMN_NAME, name);

        Uri insertUri = context.getContentResolver().insert(providerUri, insertValues);

        return ContentUris.parseId(insertUri);
    }

    public int update(Context context) {
        Uri providerUri = Uri.parse("content://" + PlacesProvider.AUTHORITY + "/"
                + PlacesDB.PlaceTable.TABLE_NAME);

        ContentValues updateValues = new ContentValues();

        updateValues.put(PlacesDB.PlaceTable.COLUMN_DESCRIPTION, description);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_PHOTO, photo);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_LATITUDE, latitude);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_LONGITUDE, longitude);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_NAME, name);

        return context.getContentResolver().update(providerUri, updateValues,
                PlacesDB.PlaceTable._ID + " = " + _id, null);
    }

    public int delete(Context context) {
        Uri providerUri = Uri.parse("content://" + PlacesProvider.AUTHORITY + "/"
                + PlacesDB.PlaceTable.TABLE_NAME);

        ContentValues updateValues = new ContentValues();

        updateValues.put(PlacesDB.PlaceTable.COLUMN_DESCRIPTION, description);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_PHOTO, photo);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_LATITUDE, latitude);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_LONGITUDE, longitude);
        updateValues.put(PlacesDB.PlaceTable.COLUMN_NAME, name);

        return context.getContentResolver().delete(providerUri,
                PlacesDB.PlaceTable._ID + " = " + _id, null);
    }

    public long save(Context context) {
        long result = -1;

        if (_id > 0) {
            result = update(context);
        } else {
            result = insert(context);
        }

        return result;
    }

    public Uri getPhotoUri() {
        return Uri.parse(photo);
    }

    public boolean has_id() {
        return _id > 0;
    }
}
