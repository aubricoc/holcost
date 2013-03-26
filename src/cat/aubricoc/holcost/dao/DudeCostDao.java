package cat.aubricoc.holcost.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;

public class DudeCostDao extends GenericDao<DudeCost> {

	private static final String TABLE_NAME = "dude_cost";

	public DudeCostDao(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "dude", "cost", "pending_changes", "removed" };
	}

	@Override
	protected DudeCost cursorToObject(Cursor cursor) {
		DudeCost dudeCost = new DudeCost();
		dudeCost.setDude(new Dude());
		dudeCost.getDude().setId(cursor.getLong(0));
		dudeCost.setCost(new Cost());
		dudeCost.getCost().setId(cursor.getLong(1));
		dudeCost.setPendingChanges(toBoolean(cursor.getInt(2)));
		dudeCost.setRemoved(toBoolean(cursor.getInt(3)));
		return dudeCost;
	}

	@Override
	protected void setIdentifier(DudeCost dudeCost, long id) {

	}

	@Override
	protected String getIdentifierWhere() {
		return "dude=? and cost=?";
	}

	@Override
	protected String[] getIdentifierWhereValues(DudeCost dudeCost) {
		return new String[] { dudeCost.getDude().getId().toString(),
				dudeCost.getCost().getId().toString() };
	}

	@Override
	protected ContentValues getInsertValues(DudeCost dudeCost) {
		ContentValues values = new ContentValues();

		values.put("dude", dudeCost.getDude().getId());
		values.put("cost", dudeCost.getCost().getId());
		values.put("pending_changes", toInteger(dudeCost.getPendingChanges()));
		values.put("removed", toInteger(dudeCost.getRemoved()));

		return values;
	}

	public List<DudeCost> getByDudeAndRemoved(Long dudeId, boolean removed) {
		String whereClause = "dude=? and removed =?";
		String[] whereArgs = { dudeId.toString(), toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}

	public List<DudeCost> getByCostAndRemoved(Long costId, boolean removed) {
		String whereClause = "cost=? and removed=?";
		String[] whereArgs = { costId.toString(), toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}

	@Override
	protected String getOrderBy() {
		return null;
	}
}
