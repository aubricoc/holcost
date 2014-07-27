package cat.aubricoc.holcost.dao;

import java.util.List;

import cat.aubricoc.holcost.model.Dude;

public class DudeDao extends Dao<Dude, Long> {

	private static final DudeDao INSTANCE = new DudeDao();

	private DudeDao() {
		super(Dude.class);
	}

	public static DudeDao getInstance() {
		return INSTANCE;
	}

	public List<Dude> getByHolcost(Long holcostId) {
		String whereClause = "holcost=?";
		String[] whereArgs = { holcostId.toString() };
		return getBy(whereClause, whereArgs);
	}
}
