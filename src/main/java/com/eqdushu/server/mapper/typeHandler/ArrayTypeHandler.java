package com.eqdushu.server.mapper.typeHandler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeException;

@MappedJdbcTypes(JdbcType.ARRAY)
public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {

	private static final String TYPE_NAME_VARCHAR = "varchar";
	private static final String TYPE_NAME_INTEGER = "integer";
	private static final String TYPE_NAME_LONG = "bigint";
	private static final String TYPE_NAME_BOOLEAN = "boolean";
	private static final String TYPE_NAME_NUMERIC = "numeric";

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Object[] parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setNull(i, Types.ARRAY);
		} else {
			String typeName = null;
			if (parameter instanceof String[]) {
				typeName = TYPE_NAME_VARCHAR;
			} else if (parameter instanceof Integer[]) {
				typeName = TYPE_NAME_INTEGER;
			} else if (parameter instanceof Long[]) {
				typeName = TYPE_NAME_LONG;
			} else if (parameter instanceof Boolean[]) {
				typeName = TYPE_NAME_BOOLEAN;
			} else if (parameter instanceof Double[]) {
				typeName = TYPE_NAME_NUMERIC;
			} 
			if (typeName == null) {
				throw new TypeException(
						"unsupport parameter type error, type is "
								+ parameter.getClass().getName());
			}
			Connection conn = ps.getConnection();
			Array array = conn.createArrayOf(typeName, (Object[]) parameter);
			ps.setArray(i, array);
		}
	}

	@Override
	public Object[] getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return getArray(rs.getArray(columnName));
	}

	@Override
	public Object[] getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return getArray(rs.getArray(columnIndex));
	}

	@Override
	public Object[] getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return getArray(cs.getArray(columnIndex));
	}

	private Object[] getArray(Array array) {
		if (array == null) {
			return null;
		}
		try {
			return (Object[]) array.getArray();
		} catch (Exception e) {
		}
		return null;
	}

}
