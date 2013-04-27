package com.mac.smartcontrol.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private Context context;
	private SQLiteDatabase db;

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("CREATE TABLE IF NOT EXISTS appl(idx integer,rgn_idx integer,name VARCHAR(32),type integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS cama(idx integer,rgn_idx integer,name VARCHAR(32),ipaddr,integer,port integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS cmd(idx integer,dev_type integer,dev_idx integer,name VARCHAR(32),voice VARCHAR(64),usCtrlIdx integer,code integer,para integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS ctrl(idx integer,name VARCHAR(32),addr integer,status integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS mode(idx integer,rgn_idx integer,name VARCHAR(32),voice VARCHAR(32))");
		db.execSQL("CREATE TABLE IF NOT EXISTS modecmd(idx integer,mode_idx integer,cmd_idx integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS rgn(idx integer,name VARCHAR(32)");
		db.execSQL("CREATE TABLE IF NOT EXISTS sens(idx integer,rgn_idx integer,name VARCHAR(32),type integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS user(idx integer,name VARCHAR(32),pass VARCHAR(32),type integer)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion != newVersion) {
			db.execSQL("drop table if exists hat_area");
			onCreate(db);
		}
	}

//	public List<HatArea> selectAllAreaData(int fatherID) {
//		db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(
//				"select areaID,area from hat_area where father=" + fatherID,
//				null);
//		List<HatArea> list = new ArrayList<HatArea>();
//		while (cursor.moveToNext()) {
//			HatArea area = new HatArea();
//			area.setAreaId(cursor.getInt(0));
//			area.setArea(cursor.getString(1));
//			list.add(area);
//		}
//		cursor.close();
//		db.close();
//		return list;
//	}
//
//	public String selectAreaData(String areaID) {
//		db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery("select area from hat_area where areaID="
//				+ areaID, null);
//		String area = null;
//		while (cursor.moveToNext()) {
//			area = cursor.getString(0);
//		}
//		cursor.close();
//		db.close();
//		return area;
//	}
//
//	public boolean addHatArea(List<HatArea> list,int fatherID) {
//		boolean b = false;
//		db = getWritableDatabase();
//		try {
//			db.beginTransaction();
//			for (int i = 0; i < list.size(); i++) {
//				HatArea hatArea = list.get(i);
//				db.execSQL("insert into hat_area(areaID,area,father) values('"
//						+ hatArea.getAreaId() + "','" + hatArea.getArea()
//						+ "','" + fatherID + "')");
//			}
//			db.setTransactionSuccessful();
//			b = true;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			db.endTransaction();
//			db.close();
//		}
//		return b;
//	}

}
