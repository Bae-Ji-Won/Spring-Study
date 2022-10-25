package dao.Factory;


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

    public void add(User user) throws ClassNotFoundException, SQLException {

        // try-catch를 통해 null일때 예외처리

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = connectionMaker.makeConnection();

            ps = conn.prepareStatement("INSERT INTO users(id,name,password) VALUES(?,?,?)");
            ps.setString(1,user.getId());        // mysql 테이블로 값 insert
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            ps.executeUpdate();     // ctrl + enter 즉, mysql에서 번개모양을 눌러 최신화 한다는 느낌
            ps.close();
            conn.close();
            System.out.println("데이터가 insert 됬습니다.");
        }




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
            rs.close();
            ps.close();
            conn.close();
        }


    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = connectionMaker.makeConnection();
            ps = conn.prepareStatement("DELETE from users");
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            ps.close();
            conn.close();
        }

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
        }finally {
            result.close();
            ps.close();
            conn.close();
        }
    }

}
