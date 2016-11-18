package com.daodaostorm.thedoor.database;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocalOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "storm_door.db";
	private static final int DATABASE_VERSION = 1;

	public LocalOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		//
		//HistoryModifyListFactory.createDB(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		//HistoryModifyListFactory.dropTable(db,DACLocalFactory.tableName);
		onCreate(db);
	}

}
