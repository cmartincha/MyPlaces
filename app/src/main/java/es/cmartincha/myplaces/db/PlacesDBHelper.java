package es.cmartincha.myplaces.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlacesDBHelper extends SQLiteOpenHelper {

	public PlacesDBHelper(Context context) {
		super(context, PlacesDB.DATABASE_NAME, null,
				PlacesDB.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PlacesDB.PlaceTable.SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(PlacesDB.PlaceTable.SQL_DELETE);
		onCreate(db);
	}
}
