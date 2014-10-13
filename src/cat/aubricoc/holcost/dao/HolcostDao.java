package cat.aubricoc.holcost.dao;

import cat.aubricoc.holcost.activity.Activity;
import cat.aubricoc.holcost.db.HolcostDatabaseHelper;
import cat.aubricoc.holcost.model.Holcost;

import com.canteratech.apa.Dao;

public class HolcostDao extends Dao<Holcost, Long> {

	private static final HolcostDao INSTANCE = new HolcostDao();

	private HolcostDao() {
		super(new HolcostDatabaseHelper(Activity.CURRENT_CONTEXT), Holcost.class);
	}

	public static HolcostDao getInstance() {
		return INSTANCE;
	}

	public Holcost getActiveHolcost() {
		String whereClause = "active=?";
		String[] whereArgs = { "1" };
		return getSingleResultBy(whereClause, whereArgs);
	}
}
