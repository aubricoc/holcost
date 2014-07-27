package cat.aubricoc.holcost.dao;

import cat.aubricoc.holcost.model.Holcost;

public class HolcostDao extends Dao<Holcost, Long> {

	private static final HolcostDao INSTANCE = new HolcostDao();

	private HolcostDao() {
		super(Holcost.class);
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
