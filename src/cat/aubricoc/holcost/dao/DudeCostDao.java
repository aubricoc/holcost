package cat.aubricoc.holcost.dao;

import java.util.List;

import cat.aubricoc.holcost.activity.Activity;
import cat.aubricoc.holcost.db.HolcostDatabaseHelper;
import cat.aubricoc.holcost.model.DudeCost;

import com.canteratech.apa.Dao;

public class DudeCostDao extends Dao<DudeCost, DudeCost> {

	private static final DudeCostDao INSTANCE = new DudeCostDao();

	private DudeCostDao() {
		super(new HolcostDatabaseHelper(Activity.CURRENT_CONTEXT), DudeCost.class);
	}

	public static DudeCostDao getInstance() {
		return INSTANCE;
	}

	public List<DudeCost> getByDude(Long dudeId) {
		String whereClause = "dude=?";
		String[] whereArgs = { dudeId.toString() };
		return getBy(whereClause, whereArgs);
	}

	public List<DudeCost> getByCost(Long costId) {
		String whereClause = "cost=?";
		String[] whereArgs = { costId.toString() };
		return getBy(whereClause, whereArgs);
	}
}
