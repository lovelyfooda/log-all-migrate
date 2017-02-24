package org.apache.ibatis.type;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * For override the StringTypeHandler, will be removed later.
 * @author Jun Chen
 *
 */
public class StringTypeHandler extends BaseTypeHandler<String> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
      throws SQLException {
    try {
		ps.setString(i, new String(parameter.getBytes("UTF-8"), "latin1"));
	} catch (UnsupportedEncodingException e) {
		throw new SQLException(e);
	}
  }

  @Override
  public String getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
		try {
		    if(rs.getBytes(columnName) == null){
		        return null;
		    }
			return new String(rs.getBytes(columnName), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(e);
		}
  }

  @Override
  public String getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
		try {
		    if(rs.getBytes(columnIndex) == null){
                return null;
            }
			return new String(rs.getBytes(columnIndex), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(e);
		}
  }

  @Override
  public String getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
		try {
		    if(cs.getBytes(columnIndex) == null){
                return null;
            }
			return new String(cs.getBytes(columnIndex), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(e);
		}
  }
}
