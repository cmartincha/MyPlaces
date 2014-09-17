
package es.cmartincha.myplaces.db;

import android.provider.BaseColumns;

public final class PlacesDB {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MisLugares.db";

    public static abstract class PlaceTable implements BaseColumns {
        public static final String TABLE_NAME = "place";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_PHOTO = "photo";

        public static final String SQL_CREATE = "CREATE TABLE "
                + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
                + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_LATITUDE
                + " REAL, " + COLUMN_LONGITUDE + " REAL, " + COLUMN_PHOTO
                + " TEXT" + ")";

        public static final String SQL_DELETE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;
    }
}
