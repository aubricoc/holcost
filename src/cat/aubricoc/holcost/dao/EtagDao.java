package cat.aubricoc.holcost.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cat.aubricoc.holcost.model.Etag;

public class EtagDao extends GenericDao<Etag> {

	private static final String TABLE_NAME = "etag";

	public EtagDao(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "url", "etag" };
	}

	@Override
	protected void setIdentifier(Etag etag, long id) {

	}

	@Override
	protected String getIdentifierWhere() {
		return "id=?";
	}

	@Override
	protected String[] getIdentifierWhereValues(Etag etag) {
		return new String[] { etag.getUrl() };
	}

	@Override
	protected Etag cursorToObject(Cursor cursor) {
		Etag etag = new Etag();
		etag.setUrl(cursor.getString(0));
		etag.setEtag(cursor.getString(1));
		return etag;
	}

	@Override
	protected ContentValues getInsertValues(Etag etag) {
		ContentValues values = new ContentValues();

		values.put("url", etag.getUrl());
		values.put("etag", etag.getEtag());

		return values;
	}

	@Override
	protected String getOrderBy() {
		return "url";
	}
}
