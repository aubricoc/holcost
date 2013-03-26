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
		return new String[] { "id", "name", "active", "server_id",
				"pending_changes", "removed" };
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
		holcost.setActive(toBoolean(cursor.getInt(2)));
		holcost.setServerId(cursor.getLong(3));
		holcost.setPendingChanges(toBoolean(cursor.getInt(4)));
		holcost.setRemoved(toBoolean(cursor.getInt(5)));
		return holcost;
	}

	@Override
	protected ContentValues getInsertValues(Holcost holcost) {
		ContentValues values = new ContentValues();

		values.put("name", holcost.getName());
		values.put("active", toInteger(holcost.getActive()));
		values.put("server_id", holcost.getServerId());
		values.put("pending_changes", toInteger(holcost.getPendingChanges()));
		values.put("removed", toInteger(holcost.getRemoved()));

		return values;
	}

	public List<Holcost> getByActive(boolean active) {
		String whereClause = "active=?";
		String[] whereArgs = { toInteger(active).toString() };
		return getBy(whereClause, whereArgs);
	}

	@Override
	protected String getOrderBy() {
		return "name";
	}

	public List<Holcost> getByRemoved(boolean removed) {
		String whereClause = "removed=?";
		String[] whereArgs = { toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}
}
