package dao.Strategy;


import domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 중복코드만 따로 빼내서 작성한 코드 (첫번째 방식)
public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao() {
        this.connectionMaker = new AWSConnectionMaker();
    }   // 기본 생성자

    public UserDao(ConnectionMaker connectionMaker){        // Factory에서 가공한 값 받아옴
        this.connectionMaker = connectionMaker;
    }


    // 중복되는 코드들을 하나로 묶음(add,delete는 반환형이 없으므로 같이 묶어서 Strategy(쿼리문)만 바꿔서 사용하면 됨)
    public void jdbcContextWithStatementstrategy(StatementStrategy st){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = connectionMaker.makeConnection();        // DB 설정을 위해 선언
            ps = st.makepreparedStatement(conn);    // 쿼리문을 가져오기 위해 선언
            ps.executeUpdate();                     // 쿼리문 동작
        } catch (ClassNotFoundException | SQLException e) {
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

    public void add(User user) throws ClassNotFoundException, SQLException {

        // try-catch를 통해 null일때 예외처리
        AddStrategy addStrategy = new AddStrategy(user);
        jdbcContextWithStatementstrategy(addStrategy);
    }

    public User select(String id) throws SQLException, ClassNotFoundException {


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;   // 쿼리문을 저장함 insert문과 달리 excuteQuery() 사용
        User user = null;
        try {
            conn = connectionMaker.makeConnection();
            ps = conn.prepareStatement("SELECT id,name,password FROM users WHERE id = ?");
            ps.setString(1, id);     // id는 get(String id)로 받은 id
            rs = ps.executeQuery();
            // rs에는 쿼리 실행 결과가 담겨져 있다. (select * from users where id = 1;)
            rs.next();  // next() 하기 전까지는 지정하고 있는 행이 없기 때문에 next를 하여 행을 지정해야 값을 불러올 수 있음

            // User 생성자를 통해 쿼리문에 id값을 넣어 찾은 id, name,password 값을 저장한다.
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
        } catch (ClassNotFoundException | SQLException  e) {
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

    public void deleteAll() throws SQLException, ClassNotFoundException {
        jdbcContextWithStatementstrategy(new DeleteStrategy());
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        ResultSet result = null;        // 쿼리문 결과 저장할 변수
        PreparedStatement ps = null;
        try {
            conn = connectionMaker.makeConnection();
            result = null;

            ps = conn.prepareStatement("select count(*) from users");
            result = ps.executeQuery();     // 쿼리문 결과를 객체에 담아서 저장
            result.next();          // result를 문자열로 변환
            return result.getInt(1);
        } catch (ClassNotFoundException | SQLException e) {
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
