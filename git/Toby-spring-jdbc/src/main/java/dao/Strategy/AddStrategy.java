package dao.Strategy;

import domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStrategy implements StatementStrategy{

    User user;

    public AddStrategy(User user){  // add메서드 사용할때 입력값을 받아서 DTO에 저장함
        this.user = user;
    }
    @Override
    public PreparedStatement makepreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users(id,name,password) VALUES(?,?,?)");
        ps.setString(1,user.getId());        // DTO에 있는값을 mysql 테이블로 값 insert
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        return ps;
    }
}
