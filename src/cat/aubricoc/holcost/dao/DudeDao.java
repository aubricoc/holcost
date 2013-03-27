package cat.aubricoc.holcost.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cat.aubricoc.holcost.model.Dude;

public class DudeDao extends GenericDao<Dude> {

	private static final String TABLE_NAME = "dude";

	public DudeDao(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "id", "name", "holcost", "email", "is_user",
				"server_id", "pending_changes", "removed" };
	}

	@Override
	protected Dude cursorToObject(Cursor cursor) {
		Dude dude = new Dude();
		dude.setId(cursor.getLong(0));
		dude.setName(cursor.getString(1));
		dude.setHolcostId(cursor.getLong(2));
		dude.setEmail(cursor.getString(3));
		dude.setIsUser(toBoolean(cursor.getInt(4)));
		dude.setServerId(cursor.getLong(5));
		dude.setPendingChanges(toBoolean(cursor.getInt(6)));
		dude.setRemoved(toBoolean(cursor.getInt(7)));
		return dude;
	}

	@Override
	protected void setIdentifier(Dude dude, long id) {
		dude.setId(id);
	}

	@Override
	protected String getIdentifierWhere() {
		return "id=?";
	}

	@Override
	protected String[] getIdentifierWhereValues(Dude dude) {
		return new String[] { dude.getId().toString() };
	}

	@Override
	protected ContentValues getInsertValues(Dude dude) {
		ContentValues values = new ContentValues();

		values.put("name", dude.getName());
		values.put("holcost", dude.getHolcostId());
		values.put("email", dude.getEmail());
		values.put("is_user", toInteger(dude.getIsUser()));
		values.put("server_id", dude.getServerId());
		values.put("pending_changes", toInteger(dude.getPendingChanges()));
		values.put("removed", toInteger(dude.getRemoved()));

		return values;
	}

	@Override
	protected String getOrderBy() {
		return "name";
	}

	public List<Dude> getByHolcostAndRemoved(Long holcostId, boolean removed) {
		String whereClause = "holcost=? and removed=?";
		String[] whereArgs = { holcostId.toString(),
				toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}

	public List<Dude> getByPendingChanges(boolean pendingChanges) {
		String whereClause = "pending_changes=?";
		String[] whereArgs = { toInteger(pendingChanges).toString() };
		return getBy(whereClause, whereArgs);
	}

	public List<Dude> getByPendingChangesAndRemovedAndServerIdNull(
			boolean pendingChanges, boolean removed, boolean serverIdNull) {
		String whereClause = "pending_changes=? and removed=? and server_id is "
				+ (serverIdNull ? "null" : "not null");
		String[] whereArgs = { toInteger(pendingChanges).toString(),
				toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}
}
