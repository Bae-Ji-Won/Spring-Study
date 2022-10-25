package dao.JdbcTemplate;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class UserFactory {

    @Bean           // aws 설정값을 반환해줌
    public UserDao awsConnection(){
        UserFactory uf = new UserFactory();
        UserDao userDao = new UserDao(uf.awsDataSource());

        return userDao;
    }

    // 현재 클래스에서만 사용할거기 때문에 Bean은 붙여도 되고 안붙여도 됨 하지만, 다른 클래스에서 사용시 붙여야함
    // ConnectionMaker를 따로 빼지 않고 Bean폴더 안에서 DataSource를 통해 한번에 작업함

    public DataSource awsDataSource(){
        Map<String,String> env = System.getenv();
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl(env.get("DB_HOST"));          // DB 호스트 번호(AWS 주소) 가져옴
        dataSource.setUsername(env.get("DB_USER"));          // DB 호스트 이름  가져옴
        dataSource.setPassword(env.get("DB_PASSWORD"));      // DB 비밀번호  가져옴
        return dataSource;
    }

}
