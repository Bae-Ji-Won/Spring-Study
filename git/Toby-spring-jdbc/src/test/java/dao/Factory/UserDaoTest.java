package dao.Factory;

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
@ContextConfiguration(classes = UserFactory.class)  // bean 파일 설정
class UserDaoTest {

    @Autowired
    ApplicationContext context;         // context = UserFactory 클래스임
    UserDao userDao;
    User user1;

    @BeforeEach
    void setUp() {
        userDao = context.getBean("awsConnection", UserDao.class);  // getBean(빈 이름, 타입(형변환))
        user1 = new User("1", "홍길동", "1234");
    }

    @Test
    @DisplayName("add,Select 검사")
    void addAndSelect() throws SQLException, ClassNotFoundException {

        userDao.deleteAll(); // 초기화
        String id = "10";
        User user = new User(id,"test2","1234");
        userDao.add(user);

        User selectUser = userDao.select(id);
        Assertions.assertEquals("test2",selectUser.getName());
    }

    @Test
    @DisplayName("count 검사")
    void count() throws SQLException, ClassNotFoundException {

        userDao.deleteAll();
        Assertions.assertEquals(0,userDao.getCount());

        userDao.add(user1);
        Assertions.assertEquals(1,userDao.getCount());
    }
}