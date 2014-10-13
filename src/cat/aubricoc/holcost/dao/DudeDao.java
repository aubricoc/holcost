package cat.aubricoc.holcost.dao;

import java.util.List;

import cat.aubricoc.holcost.activity.Activity;
import cat.aubricoc.holcost.db.HolcostDatabaseHelper;
import cat.aubricoc.holcost.model.Dude;

import com.canteratech.apa.Dao;

public class DudeDao extends Dao<Dude, Long> {

	private static final DudeDao INSTANCE = new DudeDao();

	private DudeDao() {
		super(new HolcostDatabaseHelper(Activity.CURRENT_CONTEXT), Dude.class);
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
