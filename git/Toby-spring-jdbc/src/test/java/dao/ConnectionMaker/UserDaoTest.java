package dao.ConnectionMaker;

import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class UserDaoTest {

    @Test
    void addAndGet() throws ClassNotFoundException, SQLException {
        UserDao userDao = new UserDao();
        String id = "20";
        User user = new User(id,"test1","1234");
        userDao.add(user);

        System.out.println("데이터를 insert 했습니다.");

        User userget = userDao.select(id);

        Assertions.assertEquals("test1",userget.getName());
    }
}