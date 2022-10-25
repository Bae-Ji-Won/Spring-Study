package dao.Datasource;

import dao.Strategy.UserDao;
import dao.Strategy.UserFactory;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = dao.Strategy.UserFactory.class)
class UserDaoTest {

    @Autowired
    ApplicationContext context;

    UserDao userDao;
    User user1;
    User user2;

    @BeforeEach
    void setup(){
        userDao = new UserFactory().awsConnection();
        user1 = new User("1", "홍길동", "1234");
        user2 = new User("2", "이순신", "1234");
    }

    @Test
    @DisplayName("add/select/delete/count 테스트")
    void addAndSelect() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();    // 초기화

        Assertions.assertEquals(0,userDao.getCount());

        userDao.add(user1);
        Assertions.assertEquals(1,userDao.getCount());

        userDao.add(user2);
        Assertions.assertEquals(2,userDao.getCount());

        User selectuser = userDao.select("1");
        Assertions.assertEquals("홍길동",selectuser.getName());
    }

}