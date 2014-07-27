package cat.aubricoc.holcost.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.aubricoc.holcost.db.enums.Column;
import cat.aubricoc.holcost.db.enums.Entity;
import cat.aubricoc.holcost.db.enums.GeneratedValue;
import cat.aubricoc.holcost.db.enums.Id;
import cat.aubricoc.holcost.db.enums.OrderBy;
import cat.aubricoc.holcost.db.enums.Table;
import cat.aubricoc.holcost.db.enums.Transient;

public class EntityInfo {

	private static final Map<Class<?>, EntityInfo> ENTITIES_INFO = new HashMap<Class<?>, EntityInfo>();

	private Class<?> entityClass;

	private String tableName;

	private List<FieldInfo> fields;

	private Table tableAnnotation;

	private FieldInfo pk;

	private boolean isCompositePK;

	private List<FieldInfo> compositePK;

	private FieldInfo orderBy;

	private List<Field> buildLast = new ArrayList<Field>();

	public EntityInfo(Class<?> entityClass) {
		if (!entityClass.isAnnotationPresent(Entity.class)) {
			throw new IllegalArgumentException("Class "
					+ entityClass.getSimpleName() + " not have @Entity");
		}
		this.entityClass = entityClass;
		tableAnnotation = entityClass.getAnnotation(Table.class);
		setTableName();
		setFields();
	}

	private void setTableName() {
		if (tableAnnotation != null && tableAnnotation.name() != null
				&& !tableAnnotation.name().isEmpty()) {
			tableName = tableAnnotation.name();
		} else {
			tableName = convertToDatabaseName(entityClass.getSimpleName());
		}
	}

	private void setFields() {
		setFields(Arrays.asList(entityClass.getDeclaredFields()));
		while (!buildLast.isEmpty()) {
			List<Field> fieldsList = buildLast;
			buildLast = new ArrayList<Field>();
			setFields(fieldsList);
		}
	}

	private void setFields(List<Field> fieldsList) {
		fields = new ArrayList<FieldInfo>();
		for (Field field : fieldsList) {
			if (!Modifier.isAbstract(field.getModifiers())
					&& !Modifier.isFinal(field.getModifiers())
					&& !Modifier.isStatic(field.getModifiers())) {
				FieldInfo fieldInfo = new FieldInfo(field);
				if (!fieldInfo.ignore) {
					if (fieldInfo.rebuild) {
						buildLast.add(field);
					} else {
						fields.add(fieldInfo);
						if (fieldInfo.isPK()) {
							if (pk != null) {
								isCompositePK = true;
								if (compositePK == null) {
									compositePK = new ArrayList<FieldInfo>();
									compositePK.add(pk);
									pk = null;
								}
								compositePK.add(fieldInfo);
							} else {
								pk = fieldInfo;
							}
						}
						if (fieldInfo.isOrderBy()) {
							if (orderBy != null) {
								throw new IllegalStateException("Class "
										+ entityClass.getSimpleName()
										+ " have more than one @OrderBy");
							}
							orderBy = fieldInfo;
						}
					}
				}
			}
		}
		if (pk == null && !isCompositePK) {
			throw new IllegalStateException("Class "
					+ entityClass.getSimpleName() + " no have any @Id");
		}
	}

	private String convertToDatabaseName(String name) {
		String databaseName = "";
		boolean first = true;
		for (char c : name.toCharArray()) {
			if (Character.toUpperCase(c) == c) {
				if (!first) {
					databaseName += "_";
				}
				databaseName += Character.toLowerCase(c);
			} else {
				databaseName += c;
			}
			if (first) {
				first = false;
			}
		}
		return databaseName;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getTableName() {
		return tableName;
	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public FieldInfo getPK() {
		return pk;
	}

	public boolean isCompositePK() {
		return isCompositePK;
	}

	public List<FieldInfo> getCompositePK() {
		return compositePK;
	}

	public FieldInfo getOrderBy() {
		return orderBy;
	}

	public class FieldInfo {

		private Field field;

		private String columnName;

		private String databaseType;

		private Column columnAnnotation;

		private boolean ignore;

		private boolean rebuild;

		private boolean pk;

		private boolean nullable;

		private boolean fk;

		private boolean fkDeleteOnCascade;

		private boolean autoincremental;

		private boolean orderBy;

		private boolean orderByDescendant;

		private EntityInfo foreignKeyTo;

		private FieldInfo(Field field) {
			this.field = field;
			columnAnnotation = field.getAnnotation(Column.class);
			if (field.isAnnotationPresent(Transient.class)) {
				ignore = true;
			} else {
				setColumnName();
				setDatabaseType();
				pk = field.isAnnotationPresent(Id.class);
				autoincremental = field
						.isAnnotationPresent(GeneratedValue.class);
				orderBy = field.isAnnotationPresent(OrderBy.class);
				if (orderBy) {
					orderByDescendant = field.getAnnotation(OrderBy.class)
							.descendant();
				}
				nullable = (columnAnnotation == null || columnAnnotation
						.nullable());
			}
		}

		private void setColumnName() {
			if (columnAnnotation != null && columnAnnotation.name() != null
					&& !columnAnnotation.name().isEmpty()) {
				columnName = columnAnnotation.name();
			}
			columnName = convertToDatabaseName(field.getName());
		}

		private void setDatabaseType() {
			Class<?> type = field.getType();
			if (type.equals(String.class) || type.equals(Date.class)
					|| type.equals(Character.class) || type.isEnum()) {
				databaseType = "string";
			} else if (type.equals(Integer.class) || type.equals(Long.class)
					|| type.equals(Short.class) || type.equals(Byte.class)
					|| type.equals(Boolean.class)
					|| type.equals(BigInteger.class)) {
				databaseType = "integer";
			} else if (type.equals(BigDecimal.class)
					|| type.equals(Float.class) || type.equals(Double.class)) {
				databaseType = "real";
			} else if (type.equals(byte[].class)) {
				databaseType = "blob";
			} else if (type.equals(entityClass)) {
				fk = true;
				foreignKeyTo = EntityInfo.this;
				if (EntityInfo.this.pk != null) {
					databaseType = EntityInfo.this.pk.databaseType;
				} else {
					rebuild = true;
				}
			} else if (type.isAnnotationPresent(Entity.class)) {
				fk = true;
				foreignKeyTo = getEntityInfo(type);
				databaseType = foreignKeyTo.pk.databaseType;
				fkDeleteOnCascade = (columnAnnotation != null && columnAnnotation
						.deleteOnCascade());
			} else {
				throw new IllegalStateException("Field " + field.getName()
						+ " of Class" + entityClass.getSimpleName()
						+ " have invalid type");
			}
		}

		public Field getField() {
			return field;
		}

		public String getColumnName() {
			return columnName;
		}

		public String getDatabaseType() {
			return databaseType;
		}

		public boolean isPK() {
			return pk;
		}

		public boolean isFK() {
			return fk;
		}

		public boolean isFkDeleteOnCascade() {
			return fkDeleteOnCascade;
		}

		public boolean isNullable() {
			return nullable;
		}

		public EntityInfo getForeignKeyTo() {
			return foreignKeyTo;
		}

		public boolean isAutoincremental() {
			return autoincremental;
		}

		public boolean isOrderBy() {
			return orderBy;
		}

		public boolean isOrderByDescendant() {
			return orderByDescendant;
		}
	}

	public static EntityInfo getEntityInfo(Class<?> entity) {
		return getEntityInfo(entity, true);
	}

	public static EntityInfo getEntityInfo(Class<?> entity,
			boolean buildIfNotExist) {
		EntityInfo entityInfo = ENTITIES_INFO.get(entity);
		if (entityInfo == null && buildIfNotExist) {
			entityInfo = new EntityInfo(entity);
			ENTITIES_INFO.put(entity, entityInfo);
		}
		return entityInfo;
	}
}
