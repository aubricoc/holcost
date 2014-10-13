package cat.aubricoc.holcost.dao;

import java.util.List;

import cat.aubricoc.holcost.activity.Activity;
import cat.aubricoc.holcost.db.HolcostDatabaseHelper;
import cat.aubricoc.holcost.model.Cost;

import com.canteratech.apa.Dao;

public class CostDao extends Dao<Cost, Long> {

	private static final CostDao INSTANCE = new CostDao();

	private CostDao() {
		super(new HolcostDatabaseHelper(Activity.CURRENT_CONTEXT), Cost.class);
	}

	public static CostDao getInstance() {
		return INSTANCE;
	}

	public List<Cost> getByPayer(Long payerId) {
		String whereClause = "payer=?";
		String[] whereArgs = { payerId.toString() };
		return getBy(whereClause, whereArgs);
	}

	public List<Cost> getByHolcost(Long holcostId) {
		String whereClause = "holcost=?";
		String[] whereArgs = { holcostId.toString() };
		return getBy(whereClause, whereArgs);
	}
}
