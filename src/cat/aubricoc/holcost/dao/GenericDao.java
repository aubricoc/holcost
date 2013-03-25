package cat.aubricoc.holcost.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cat.aubricoc.holcost.db.HolcostDatabaseHelper;

public abstract class GenericDao<T> {

	protected SQLiteDatabase database;
	protected HolcostDatabaseHelper dbHelper;

	public GenericDao(Context context) {
		dbHelper = new HolcostDatabaseHelper(context);
	}

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		if (!database.isReadOnly()) {
			database.execSQL("PRAGMA foreign_keys=ON;");
	    }

	}

	private void close() {
		dbHelper.close();
	}

	public long create(T object) {
		open();
		try {
			ContentValues values = getInsertValues(object);

			long id = database.insert(getTableName(), null, values);

			setIdentifier(object, id);
			return id;
		} finally {
			close();
		}
	}

	public void update(T object) {
		open();
		try {
			ContentValues values = getInsertValues(object);
			database.update(getTableName(), values, getIdentifierWhere(),
					getIdentifierWhereValues(object));
		} finally {
			close();
		}
	}

	public void delete(T object) {
		open();
		try {
			database.delete(getTableName(), getIdentifierWhere(),
					getIdentifierWhereValues(object));
		} finally {
			close();
		}
	}

	public List<T> getAll() {
		open();
		try {
			List<T> list = new ArrayList<T>();

			Cursor cursor = database.query(getTableName(), getColumns(), null,
					null, null, null, getOrderBy());

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				T object = cursorToObject(cursor);
				list.add(object);
				cursor.moveToNext();
			}
			cursor.close();
			return list;
		} finally {
			close();
		}
	}

	protected List<T> getBy(String whereClause, String[] whereArgs) {
		open();
		try {
			List<T> list = new ArrayList<T>();

			Cursor cursor = database.query(getTableName(), getColumns(),
					whereClause, whereArgs, null, null, getOrderBy());

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				T object = cursorToObject(cursor);
				list.add(object);
				cursor.moveToNext();
			}
			cursor.close();
			return list;
		} finally {
			close();
		}
	}

	public T getById(T object) {
		open();
		try {
			Cursor cursor = database.query(getTableName(), getColumns(),
					getIdentifierWhere(), getIdentifierWhereValues(object),
					null, null, null);

			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				T rowObject = cursorToObject(cursor);
				cursor.close();
				return rowObject;
			}
			cursor.close();
			return null;

		} finally {
			close();
		}
	}

	protected abstract String getTableName();

	protected abstract String[] getColumns();

	protected abstract T cursorToObject(Cursor cursor);

	protected abstract void setIdentifier(T object, long id);

	protected abstract String getIdentifierWhere();

	protected abstract String[] getIdentifierWhereValues(T object);

	protected abstract ContentValues getInsertValues(T object);

	protected abstract String getOrderBy();
}
