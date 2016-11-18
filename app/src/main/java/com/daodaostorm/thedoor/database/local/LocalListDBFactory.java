package com.daodaostorm.thedoor.database.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daodaostorm.thedoor.common.ItemInfo;
import com.daodaostorm.thedoor.database.LocalFactoryBase;

public class LocalListDBFactory extends LocalFactoryBase<ItemInfo> {

	public final static String tableName = "localdbList";

	public LocalListDBFactory(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static void createDB(SQLiteDatabase db) {
		
		db.execSQL("create table if not exists localdbList("
				+ "id varchar primary key," + "text varchar,"
				+ "type varchar," + "title varchar," + "img varchar)");
	}

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}

	@Override
	protected String getprimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

	@Override
	protected String getOrderColumnName() {
		return "id";
	}

	@Override
	protected long getMaxCount() {
		return 3000;
	}

	@Override
	protected ItemInfo createModel(Cursor cursor) {
		// TODO Auto-generated method stub
		ItemInfo ObjectInfo = new ItemInfo();
		ObjectInfo.id = cursor.getString(cursor.getColumnIndex("id"));
		ObjectInfo.text = cursor.getString(cursor.getColumnIndex("text"));
		ObjectInfo.type = cursor.getString(cursor.getColumnIndex("type"));
		ObjectInfo.title = cursor.getString(cursor.getColumnIndex("title"));
		ObjectInfo.img = cursor.getString(cursor.getColumnIndex("img"));
		return ObjectInfo;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, ItemInfo record) {
	
		 db.execSQL("insert into blackList(id,text,type,title,img)" +
	                "values(?,?,?,?,?)",
	                new Object[]{record.id, record.text, record.type, record.title, record.img});

	}
	
	
	public void insertBlackObject(ItemInfo object){
		 insertRecord(object);
	}
	
	
	public ItemInfo QueryByPackageName(String property,String val){
		return findByProperty(property,val);
	}

	public void deletBLackListTable(){
		deletedRecords();
	}
	
}
