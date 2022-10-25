package dao.JdbcTemplate;


import domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 중복코드만 따로 빼내서 작성한 코드 (첫번째 방식)
public class UserDao {


    private JdbcTemplate jdbcTemplate;      // jdbcTemplate 선언

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); // 구체적인 클래스 이름이 들어가는 구간
    }

    // 중복되는 rowMapper를 밖으로 빼서 중복 코드제거함
    RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getString("id"),rs.getString("name"),rs.getString("password"));

            return user;
        }
    };

    public void add(final User user){
            this.jdbcTemplate.update("insert into users(id,name,password) values (?,?,?);",
            user.getId(),user.getName(),user.getPassword());
    }

    public User select(String id){
        String sql = "select *from users where id = ?";
        // RowMapper를 사용하면, 원하는 형태의 결과값을 반환할 수 있다
        // select로 나온 여러개의 값을 반환할 수 있을 뿐 아니라 사용자가 원하는 형태로도 얼마든지 가능하다.

        return this.jdbcTemplate.queryForObject(sql,rowMapper,id);
    }

    public List<User> getall(){

        String sql = "select *from users order by id";
        // RowMapper를 사용하면, 원하는 형태의 결과값을 반환할 수 있다
        // select로 나온 여러개의 값을 반환할 수 있을 뿐 아니라 사용자가 원하는 형태로도 얼마든지 가능하다.

        return this.jdbcTemplate.query(sql,rowMapper);
    }


    public void deleteAll(){
        this.jdbcTemplate.update("delete from users");   // 쿼리만 넘겨줌
        /* 
        jdbcTemplate 사용하여 구현(지금까지 만든 jdbcContext 필요없음)
         */
    }

    public int getCount(){
        return this.jdbcTemplate.queryForObject("select count(*) from users;",Integer.class);
    }
}
