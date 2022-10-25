package dao.Datasource;


import domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 중복코드만 따로 빼내서 작성한 코드 (첫번째 방식)
public class UserDao {

    private DataSource dataSource;  // Connection -> DataSource 사용

    private JdbcContext jdbcContext;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource);
    }


    // 중복되는 코드들을 하나로 묶음(add,delete는 반환형이 없으므로 같이 묶어서 Strategy(쿼리문)만 바꿔서 사용하면 됨)


    public void add(final User user) throws SQLException {

        // AddStrategy 클래스를 따로 안만들고 여기서 처리
        this.jdbcContext.workWithStatementstrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makepreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO users(id,name,password) VALUES(?,?,?)");
                ps.setString(1,user.getId());        // DTO에 있는값을 mysql 테이블로 값 insert
                ps.setString(2,user.getName());
                ps.setString(3,user.getPassword());
                return ps;
            }
        });
        /* jdcbContextWithStatementstrategy(stmt); 인데 여기서 stmt에는 add에 해당하는 쿼리문이 들어가야 하므로
           원래는 따로 Strategy클래스에서 만들거나 현재 메서드에서 선언을 할 수 있는데 코드를 줄이기 위해
           참조변수자리에 바로 new를 통해 객체를 생성하고 추가한다.
           그리고 현재 user를 가져다 사용하고 있으므로 따로 User 객체를 생성해서 사용할 필요 없이
           참조변수를 통해 가져다 사용할 수 있음 다만, 이때는 final을 붙여줘야함

           이것을 익명의 내부클래스라고 함
         */
    }

    public User select(String id) throws SQLException, ClassNotFoundException {


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;   // 쿼리문을 저장함 insert문과 달리 excuteQuery() 사용
        User user = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("SELECT id,name,password FROM users WHERE id = ?");
            ps.setString(1, id);     // id는 get(String id)로 받은 id
            rs = ps.executeQuery();
            // rs에는 쿼리 실행 결과가 담겨져 있다. (select * from users where id = 1;)
            rs.next();  // next() 하기 전까지는 지정하고 있는 행이 없기 때문에 next를 하여 행을 지정해야 값을 불러올 수 있음

            // User 생성자를 통해 쿼리문에 id값을 넣어 찾은 id, name,password 값을 저장한다.
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
        } catch (SQLException  e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {}
            try {
                ps.close();
            } catch (SQLException e) {}

            try {
                conn.close();
            } catch (SQLException e) {}
        }


    }

    public void deleteAll() throws SQLException{
        // DeleteStrategy 클래스를 따로 안만들고 여기서 처리
        this.jdbcContext.workWithStatementstrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makepreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("delete  from users");
            }
        });
        /* jdcbContextWithStatementstrategy(stmt); 인데 여기서 stmt에는 delete에 해당하는 쿼리문이 들어가야 하므로
           원래는 따로 Strategy클래스에서 만들거나 현재 메서드에서 선언을 할 수 있는데 코드를 줄이기 위해
           참조변수자리에 바로 new를 통해 객체를 생성하고 추가한다.
           
           이것을 익명의 내부클래스라고 함
         */
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        ResultSet result = null;        // 쿼리문 결과 저장할 변수
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            result = null;

            ps = conn.prepareStatement("select count(*) from users");
            result = ps.executeQuery();     // 쿼리문 결과를 객체에 담아서 저장
            result.next();          // result를 문자열로 변환
            return result.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                result.close();
            } catch (SQLException e) {}
            try {
                ps.close();
            } catch (SQLException e) {}

            try {
                conn.close();
            } catch (SQLException e) {}
        }
    }

}
