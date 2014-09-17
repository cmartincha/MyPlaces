
package es.cmartincha.myplaces.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class PlacesProvider extends ContentProvider {
    public static final String AUTHORITY = "es.cmartincha.mislugares.provider";

    private static final int CODE_LIST_PLACES = 1;
    private static final int CODE_PLACE = 2;

    private static final String MIME_LIST_PLACES = "vnd.android.cursor.dir/vnd.es.cmartincha.provider."
            + PlacesDB.PlaceTable.TABLE_NAME;
    private static final String MIME_PLACE = "vnd.android.cursor.item/vnd.es.cmartincha.provider."
            + PlacesDB.PlaceTable.TABLE_NAME;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, PlacesDB.PlaceTable.TABLE_NAME, CODE_LIST_PLACES);
        sUriMatcher.addURI(AUTHORITY, PlacesDB.PlaceTable.TABLE_NAME + "/#",
                CODE_PLACE);
    }

    private PlacesDBHelper mPlacesDBHelper;

    @Override
    public boolean onCreate() {
        mPlacesDBHelper = new PlacesDBHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mPlacesDBHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case CODE_LIST_PLACES:
                cursor = createListPlacesCursor(selection, selectionArgs, sortOrder, db);
                break;
            case CODE_PLACE:
                cursor = createPlaceCursor(uri, selection, selectionArgs, sortOrder, db);
                break;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    private Cursor createPlaceCursor(Uri uri, String selection, String[] selectionArgs,
            String sortOrder, SQLiteDatabase db) {
        Cursor cursor;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(PlacesDB.PlaceTable.TABLE_NAME);
        queryBuilder.appendWhere(" _ID = " + uri.getLastPathSegment());

        cursor = queryBuilder.query(db, null, selection,
                selectionArgs, null, null, sortOrder);

        return cursor;
    }

    private Cursor createListPlacesCursor(String selection, String[] selectionArgs,
            String sortOrder, SQLiteDatabase db) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor cursor;

        queryBuilder.setTables(PlacesDB.PlaceTable.TABLE_NAME);

        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = "_ID ASC";
        }

        cursor = queryBuilder.query(db, null, selection,
                selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        String mime = null;

        switch (sUriMatcher.match(uri)) {
            case CODE_LIST_PLACES:
                mime = MIME_LIST_PLACES;
                break;
            case CODE_PLACE:
                mime = MIME_PLACE;
                break;
        }

        return mime;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mPlacesDBHelper.getWritableDatabase();
        long itemId = db.insert(PlacesDB.PlaceTable.TABLE_NAME, null, values);
        Uri itemUri = null;

        if (itemId != -1) {
            itemUri = Uri.withAppendedPath(uri, String.valueOf(itemId));
        }

        notifyChange(uri);

        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mPlacesDBHelper.getWritableDatabase();
        int numRowsAffected = db.delete(PlacesDB.PlaceTable.TABLE_NAME, selection, selectionArgs);

        notifyChange(uri);

        return numRowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mPlacesDBHelper.getWritableDatabase();
        int numRowsAffected = db.update(PlacesDB.PlaceTable.TABLE_NAME, values, selection,
                selectionArgs);

        notifyChange(uri);

        return numRowsAffected;
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
