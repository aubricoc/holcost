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
