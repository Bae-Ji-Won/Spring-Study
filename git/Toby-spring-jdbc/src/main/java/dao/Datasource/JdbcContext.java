package dao.Datasource;

import dao.Datasource.StatementStrategy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 다른 클래스에서도 사용할 수 있도록 Jdbc를 별로 클래스로 분리
public class JdbcContext {

    private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(String sql) throws SQLException{
        this.workWithStatementstrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makepreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement(sql);
            }
        });
    }
    public void workWithStatementstrategy(StatementStrategy st){
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();        // DB 설정을 위해 선언
            ps = st.makepreparedStatement(conn);    // 쿼리문을 가져오기 위해 선언
            ps.executeUpdate();                     // 쿼리문 동작
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {}

            try {
                conn.close();
            } catch (SQLException e) {}
        }
    }
}
