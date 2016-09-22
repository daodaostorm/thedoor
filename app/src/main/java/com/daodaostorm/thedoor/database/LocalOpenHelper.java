package com.daodaostorm.thedoor.database;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocalOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "shitou_thirdservice.db";
	private static final int DATABASE_VERSION = 6;

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
	
	/**
     * �ж�ĳ�ű��Ƿ����
     * @param tabName ����
     * @return
     */
    public boolean tabIsExist(String tabName){
            boolean result = false;
            if(tabName == null){
                    return false;
            }
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                    db = this.getReadableDatabase();//��this�Ǽ̳�SQLiteOpenHelper��õ���
                    //String sql = "select count(*) as c from sqlite_master where type ='table' and name ='+tabName.trim()+' ;";
                    String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
                    cursor = db.rawQuery(sql, null);
                    if(cursor.moveToNext()){
                            int count = cursor.getInt(0);
                            if(count>0){
                                    result = true;
                            }
                    }
                     
            } catch (Exception e) {
                    // TODO: handle exception
            }                
            return result;
    }
}
