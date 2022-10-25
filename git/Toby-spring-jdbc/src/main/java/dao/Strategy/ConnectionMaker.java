package dao.Strategy;

import java.sql.Connection;
import java.sql.SQLException;

// DB별 설정을 분별하기 위한 인터페이스
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
