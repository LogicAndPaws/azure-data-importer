package com.epam.azuredataimporter.importing;

import com.epam.azuredataimporter.ResultsObserver;
import com.epam.azuredataimporter.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresDAO implements DaoImporter<User>{
    private JdbcTemplate template = null;
    private final static String driver = "org.postgresql.Driver";
//    private String url;
//    private String username;
//    private String password;
    private ResultsObserver observer;

    public PostgresDAO(ResultsObserver observer,String dbUrl,String dbUsername,String dbPassword){
        this.observer = observer;
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        template = new JdbcTemplate(dataSource);
    }

    //================================================================================

    private ResultSetExtractor<List<User>> userListExtractor = new ResultSetExtractor<List<User>>() {
        @Override
        public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List users = new ArrayList<User>();
            while (resultSet.next())
                users.add(new User(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)));
            return users;
        }
    };
    private ResultSetExtractor<User> userExtractor = new ResultSetExtractor<User>() {
        @Override
        public User extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            if(!resultSet.next())return null;
            return new User(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
        }
    };
    private User getUserById(int id){
        User user = template.query("SELECT * FROM users WHERE id= ?",userExtractor,id);
        return user;
    }

    public List<User> getUsers(){
        return template.query("SELECT * FROM users",userListExtractor);
    }

    @Override
    public void insertObject(User user) {
        if(getUserById(user.getId())!=null){
            observer.failed("(DataBase) User with id("+user.getId()+") already exist");
            return;
        }
        template.update("INSERT INTO users values(?, ?, ?)",user.getId(),user.getName(),user.getPassword());
        observer.success();
    }
}
