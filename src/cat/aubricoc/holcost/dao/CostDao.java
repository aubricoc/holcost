package cat.aubricoc.holcost.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;

public class CostDao extends GenericDao<Cost> {

	private static final String TABLE_NAME = "cost";

	public CostDao(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "id", "name", "amount", "date", "payer",
				"holcost", "server_id", "pending_changes", "removed" };
	}

	@Override
	protected Cost cursorToObject(Cursor cursor) {
		Cost cost = new Cost();
		cost.setId(cursor.getLong(0));
		cost.setName(cursor.getString(1));
		cost.setAmount(cursor.getDouble(2));
		cost.setDate(toDate(cursor.getString(3)));
		cost.setPayer(new Dude());
		cost.getPayer().setId(cursor.getLong(4));
		cost.setHolcostId(cursor.getLong(5));
		cost.setServerId(cursor.getLong(6));
		cost.setPendingChanges(toBoolean(cursor.getInt(7)));
		cost.setRemoved(toBoolean(cursor.getInt(8)));
		return cost;
	}

	@Override
	protected void setIdentifier(Cost cost, long id) {
		cost.setId(id);
	}

	@Override
	protected String getIdentifierWhere() {
		return "id=?";
	}

	@Override
	protected String[] getIdentifierWhereValues(Cost cost) {
		return new String[] { cost.getId().toString() };
	}

	@Override
	protected ContentValues getInsertValues(Cost cost) {
		ContentValues values = new ContentValues();

		values.put("name", cost.getName());
		values.put("amount", cost.getAmount());
		values.put("date", toString(cost.getDate()));
		values.put("payer", cost.getPayer().getId());
		values.put("holcost", cost.getHolcostId());
		values.put("server_id", cost.getServerId());
		values.put("pending_changes", toInteger(cost.getPendingChanges()));
		values.put("removed", toInteger(cost.getRemoved()));

		return values;
	}

	public List<Cost> getByPayerAndRemoved(Long payerId, boolean removed) {
		String whereClause = "payer=? and removed=?";
		String[] whereArgs = { payerId.toString(),
				toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}

	public List<Cost> getByHolcostAndRemoved(Long holcostId, boolean removed) {
		String whereClause = "holcost=? and removed=?";
		String[] whereArgs = { holcostId.toString(), toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}

	@Override
	protected String getOrderBy() {
		return "date";
	}
}
