package cat.aubricoc.holcost.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.util.Constants;

public class HolcostDatabaseHelper extends SQLiteOpenHelper {

	public HolcostDatabaseHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		Log.i(HolcostDatabaseHelper.class.getName(), "CREATE HOLCOST DB");
//		sqLiteDatabase.execSQL("create table holcost (id integer primary key autoincrement, name text not null, active integer not null);");
//		sqLiteDatabase.execSQL("create table dude (id integer primary key autoincrement, name text not null, holcost integer not null, FOREIGN KEY(holcost) REFERENCES holcost(id) ON DELETE CASCADE);");
//		sqLiteDatabase.execSQL("create table cost (id integer primary key autoincrement, name text not null, amount real not null, date text not null, payer integer not null, holcost integer not null, FOREIGN KEY(payer) REFERENCES dude(id) ON DELETE CASCADE, FOREIGN KEY(holcost) REFERENCES holcost(id) ON DELETE CASCADE);");
//		sqLiteDatabase.execSQL("create table dude_cost (dude integer, cost integer, PRIMARY KEY (dude, cost), FOREIGN KEY(dude) REFERENCES dude(id) ON DELETE CASCADE, FOREIGN KEY(cost) REFERENCES cost(id) ON DELETE CASCADE);");
		
		Log.i(Constants.PROJECT_NAME, "Create DB...");

		List<String> createTables = DatabaseReflection.getInstance()
				.prepareCreateTables(Holcost.class, Dude.class, Cost.class, DudeCost.class);

		for (String sql : createTables) {
			db.execSQL(sql);
		}

		Log.i(Constants.PROJECT_NAME, "Create DB...OK");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(Constants.PROJECT_NAME, "Upgrade DB from " + oldVersion + " to "
				+ newVersion + "...");

		Log.i(Constants.PROJECT_NAME, "Upgrade DB from " + oldVersion + " to "
				+ newVersion + "...OK");
	}
}
