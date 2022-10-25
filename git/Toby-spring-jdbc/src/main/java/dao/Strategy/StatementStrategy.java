package dao.Strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 쿼리문 구분을 위한 인터페이스
public interface StatementStrategy {
    PreparedStatement makepreparedStatement(Connection connection) throws SQLException;
}
