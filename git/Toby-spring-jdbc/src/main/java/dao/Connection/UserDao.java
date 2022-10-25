package dao.Connection;



import domain.User;

import java.sql.*;
import java.util.Map;

// 중복코드만 따로 빼내서 작성한 코드 (첫번째 방식)
public class UserDao {

    private Connection makeConnection() throws ClassNotFoundException, SQLException {
        Map<String,String> env = System.getenv();       // 아래의 값들은 해킹 위험이 있으므로 코드상에 데이터를 넣지말고
        // 왼쪽 위에 메뉴바에서 망치 옆에 프로젝트를 눌러 edit configurations를 누르고
        // environments variables를 눌러 값을 입력해 준다.
        String dbHost = env.get("DB_HOST");         // DB 호스트 번호(AWS 주소) 가져옴
        String dbUser = env.get("DB_USER");             // DB 호스트 이름  가져옴
        String dbPassword = env.get("DB_PASSWORD");     // DB 비밀번호  가져옴

        Class.forName("com.mysql.cj.jdbc.Driver");  // 만약 DB드라이버가 2개 이상일때 어떤 DB에 데이터를 삽입할 것인지 구분하기 위해 사용함
        // 지금은 mysql 1개만 사용하기 때문에 없어도 됨
        Connection conn = DriverManager.getConnection(dbHost,dbUser,dbPassword);

        return conn;
    }

    public void add(User user) throws ClassNotFoundException {

        try{
            // db 연결(호스트,이름,비밀번호)
            Connection conn = makeConnection();     // 설정들을 모아둔 메서드 호출

            PreparedStatement ps =  conn.prepareStatement("INSERT INTO users(id,name,password) VALUES(?,?,?)");
            ps.setString(1,user.getId());        // mysql 테이블로 값 insert
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());

            ps.executeUpdate();     // ctrl + enter 즉, mysql에서 번개모양을 눌러 최신화 한다는 느낌
            ps.close();
            conn.close();
            System.out.println("데이터가 insert 됬습니다.");

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public User select(String id) throws SQLException, ClassNotFoundException {

        try {
            Connection conn = makeConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id,name,password FROM users WHERE id = ?");
            ps.setString(1, id);     // id는 get(String id)로 받은 id
            ResultSet rs = ps.executeQuery();   // 쿼리문을 저장함 insert문과 달리 excuteQuery() 사용
            // rs에는 쿼리 실행 결과가 담겨져 있다. (select * from users where id = 1;)
            rs.next();

            // User 생성자를 통해 쿼리문에 id값을 넣어 찾은 id, name,password 값을 저장한다.
            User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
            rs.close();
            ps.close();
            conn.close();
            return user;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao userDao = new UserDao();
        userDao.add(new User("7","Ruru","1234qwer"));   // user로 값을 받아 DTO에 저장한 후 mysql로 데이터 보냄
        System.out.println(userDao.select("1"));
    } 
}
