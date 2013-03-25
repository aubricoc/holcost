package cat.aubricoc.holcost.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;

public class CostDao extends GenericDao<Cost> {

	private static final String TABLE_NAME = "cost";
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	public CostDao(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "id", "name", "amount", "date", "payer", "holcost" };
	}

	@Override
	protected Cost cursorToObject(Cursor cursor) {
		Cost cost = new Cost();
		cost.setId(cursor.getLong(0));
		cost.setName(cursor.getString(1));
		cost.setAmount(cursor.getDouble(2));
		try {
			cost.setDate(DATE_FORMAT.parse(cursor.getString(3)));
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
		cost.setPayer(new Dude());
		cost.getPayer().setId(cursor.getLong(4));
		cost.setHolcostId(cursor.getLong(5));
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
		return new String[]{cost.getId().toString()};
	}

	@Override
	protected ContentValues getInsertValues(Cost cost) {
		ContentValues values = new ContentValues();

		values.put("name", cost.getName());
		values.put("amount", cost.getAmount());
		values.put("date", DATE_FORMAT.format(cost.getDate()));
		values.put("payer", cost.getPayer().getId());
		values.put("holcost", cost.getHolcostId());

		return values;
	}

	public List<Cost> getByPayer(Long payerId) {
		String whereClause = "payer=?";
		String[] whereArgs = {payerId.toString()};
		return getBy(whereClause, whereArgs);
	}
	
	public List<Cost> getByHolcost(Long holcostId) {
		String whereClause = "holcost=?";
		String[] whereArgs = {holcostId.toString()};
		return getBy(whereClause, whereArgs);
	}

	@Override
	protected String getOrderBy() {
		return "date";
	}
}
