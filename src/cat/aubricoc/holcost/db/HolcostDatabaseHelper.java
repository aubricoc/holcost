package cat.aubricoc.holcost.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HolcostDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "holcost.db";
	private static final int DATABASE_VERSION = 2;
	
	public HolcostDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		Log.i(HolcostDatabaseHelper.class.getName(), "CREATE HOLCOST DB");
		sqLiteDatabase.execSQL("create table holcost (" +
				"id integer primary key autoincrement, " +
				"name text not null, " +
				"active integer not null, " +
				"server_id integer, " +
				"pending_changes integer not null default 0, " +
				"removed integer not null default 0;");
		sqLiteDatabase.execSQL("create table dude (" +
				"id integer primary key autoincrement, " +
				"name text not null, " +
				"holcost integer not null, " +
				"email text, " +
				"is_user integer not null default 0, " +
				"server_id integer, " +
				"pending_changes integer not null default 0, " +
				"removed integer not null default 0, " +
				"FOREIGN KEY(holcost) REFERENCES holcost(id) ON DELETE CASCADE);");
		sqLiteDatabase.execSQL("create table cost (" +
				"id integer primary key autoincrement, " +
				"name text not null, " +
				"amount real not null, " +
				"date text not null, " +
				"payer integer not null, " +
				"holcost integer not null, " +
				"server_id integer, " +
				"pending_changes integer not null default 0, " +
				"removed integer not null default 0, " +
				"FOREIGN KEY(payer) REFERENCES dude(id) ON DELETE CASCADE, " +
				"FOREIGN KEY(holcost) REFERENCES holcost(id) ON DELETE CASCADE);");
		sqLiteDatabase.execSQL("create table dude_cost (" +
				"dude integer, " +
				"cost integer, " +
				"pending_changes integer not null default 0, " +
				"removed integer not null default 0, " +
				"PRIMARY KEY (dude, cost), " +
				"FOREIGN KEY(dude) REFERENCES dude(id) ON DELETE CASCADE, " +
				"FOREIGN KEY(cost) REFERENCES cost(id) ON DELETE CASCADE);");
		sqLiteDatabase.execSQL("create table etag (" +
				"url text primary key, " +
				"etag text not null);");
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		Log.i(HolcostDatabaseHelper.class.getName(), "UPGRADE HOLCOST DB FROM v" + oldVersion + " TO v" + newVersion);
		if (oldVersion == 1 && newVersion == 2) {
			
			sqLiteDatabase.execSQL("create table etag (" +
					"url text primary key, " +
					"etag text not null);");
			
			sqLiteDatabase.execSQL("alter table holcost add column server_id integer;");
			sqLiteDatabase.execSQL("alter table holcost add column pending_changes integer not null default 0;");
			sqLiteDatabase.execSQL("alter table holcost add column removed integer not null default 0;");
			
			sqLiteDatabase.execSQL("alter table dude add column email text;");
			sqLiteDatabase.execSQL("alter table dude add column is_user integer not null default 0;");
			sqLiteDatabase.execSQL("alter table dude add column server_id integer;");
			sqLiteDatabase.execSQL("alter table dude add column pending_changes integer not null default 0;");
			sqLiteDatabase.execSQL("alter table dude add column removed integer not null default 0;");
			
			sqLiteDatabase.execSQL("alter table cost add column server_id integer;");
			sqLiteDatabase.execSQL("alter table cost add column pending_changes integer not null default 0;");
			sqLiteDatabase.execSQL("alter table cost add column removed integer not null default 0;");
			
			sqLiteDatabase.execSQL("alter table dude_cost add column pending_changes integer not null default 0;");
			sqLiteDatabase.execSQL("alter table dude_cost add column removed integer not null default 0;");
			
			SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSZ");
			Map<Long, Date> costDates = new HashMap<Long, Date>();
			Cursor cursor = sqLiteDatabase.query("cost", new String[]{"id", "date"}, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Long id = cursor.getLong(0);
				String dateString = cursor.getString(1);
				if (dateString != null) {
					try {
						costDates.put(id, oldDateFormat.parse(dateString));
					} catch (ParseException e) {
						Log.e(HolcostDatabaseHelper.class.getName(), "Error upgrading date format: " + dateString, e);
					}
				}
				cursor.moveToNext();
			}
			cursor.close();
			
			for (Entry<Long, Date> entry : costDates.entrySet()) {
				String dateString = newDateFormat.format(entry.getValue());
				sqLiteDatabase.execSQL("update cost set date = ? where id = ?;", new Object[]{ dateString, entry.getKey() });
			}
		}
		
	}

}
