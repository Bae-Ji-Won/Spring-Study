package dao.Strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteStrategy implements StatementStrategy{
    @Override
    public PreparedStatement makepreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("DELETE from users");
    }
}
