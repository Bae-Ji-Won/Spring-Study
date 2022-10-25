package dao.Strategy;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserFactory {

    @Bean           // aws 설정값을 반환해줌
    public UserDao awsConnection(){
        AWSConnectionMaker awsConnectionMaker = new AWSConnectionMaker();
        UserDao userDao = new UserDao(awsConnectionMaker);      // UserDao 생성자에 넣어줌
        return userDao;
    }

}
