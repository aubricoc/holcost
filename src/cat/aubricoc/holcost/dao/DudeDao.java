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
		return new String[] {"id", "name", "holcost"};
	}

	@Override
	protected Dude cursorToObject(Cursor cursor) {
		Dude dude = new Dude();
		dude.setId(cursor.getLong(0));
		dude.setName(cursor.getString(1));
		dude.setHolcostId(cursor.getLong(1));
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
		return new String[]{dude.getId().toString()};
	}

	@Override
	protected ContentValues getInsertValues(Dude dude) {
		ContentValues values = new ContentValues();

		values.put("name", dude.getName());
		values.put("holcost", dude.getHolcostId());

		return values;
	}

	public List<Dude> getByHolcost(Long holcostId) {
		String whereClause = "holcost=?";
		String[] whereArgs = {holcostId.toString()};
		return getBy(whereClause, whereArgs);
	}

	@Override
	protected String getOrderBy() {
		return "name";
	}

}
