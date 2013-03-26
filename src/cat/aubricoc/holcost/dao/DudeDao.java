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
		dude.setHolcostId(cursor.getLong(1));
		dude.setEmail(cursor.getString(2));
		dude.setIsUser(toBoolean(cursor.getInt(3)));
		dude.setServerId(cursor.getLong(4));
		dude.setPendingChanges(toBoolean(cursor.getInt(5)));
		dude.setRemoved(toBoolean(cursor.getInt(6)));
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

	public List<Dude> getByHolcostAndRemoved(Long holcostId, boolean removed) {
		String whereClause = "holcost=? and removed=?";
		String[] whereArgs = { holcostId.toString(),
				toInteger(removed).toString() };
		return getBy(whereClause, whereArgs);
	}

	@Override
	protected String getOrderBy() {
		return "name";
	}

}
