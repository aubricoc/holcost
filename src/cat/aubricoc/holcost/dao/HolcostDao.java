package cat.aubricoc.holcost.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cat.aubricoc.holcost.model.Holcost;

public class HolcostDao extends GenericDao<Holcost> {

	private static final String TABLE_NAME = "holcost";

	public HolcostDao(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "id", "name", "active" };
	}

	@Override
	protected void setIdentifier(Holcost holcost, long id) {
		holcost.setId(id);
	}

	@Override
	protected String getIdentifierWhere() {
		return "id=?";
	}
	
	@Override
	protected String[] getIdentifierWhereValues(Holcost holcost) {
		return new String[] { holcost.getId().toString() };
	}

	@Override
	protected Holcost cursorToObject(Cursor cursor) {
		Holcost holcost = new Holcost();
		holcost.setId(cursor.getLong(0));
		holcost.setName(cursor.getString(1));
		holcost.setActive(cursor.getInt(2) == 1);
		return holcost;
	}

	@Override
	protected ContentValues getInsertValues(Holcost holcost) {
		ContentValues values = new ContentValues();

		values.put("name", holcost.getName());
		values.put("active", holcost.getActive() != null && holcost.getActive() ? 1 : 0);

		return values;
	}
	
	public Holcost getActiveHolcost() {
		String whereClause = "active=?";
		String[] whereArgs = {"1"};
		List<Holcost> holcosts = getBy(whereClause, whereArgs);
		if (holcosts.isEmpty()) {
			return null;
		}
		return holcosts.get(0);
	}

	@Override
	protected String getOrderBy() {
		return "name";
	}
}
