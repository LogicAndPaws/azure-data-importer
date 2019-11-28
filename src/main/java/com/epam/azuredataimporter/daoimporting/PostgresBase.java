package com.epam.azuredataimporter.daoimporting;

import com.epam.azuredataimporter.entity.User;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostgresBase implements BaseImporter<User> {
    private JdbcTemplate template = null;
    @Autowired
    private ResultsObserver observer;
    private ResultSetExtractor<List<User>> userListExtractor = new ResultSetExtractor<List<User>>() {
        @Override
        public List<User> extractData(ResultSet resultSet) throws SQLException {
            List users = new ArrayList<User>();
            while (resultSet.next())
                users.add(new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            return users;
        }
    };

    //================================================================================
    private ResultSetExtractor<User> userExtractor = new ResultSetExtractor<User>() {
        @Override
        public User extractData(ResultSet resultSet) throws SQLException {
            if (!resultSet.next()) return null;
            return new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
        }
    };

    public PostgresBase(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    private User getUserById(int id) {
        return template.query("SELECT * FROM users WHERE id= ?", userExtractor, id);
    }

    public List<User> getUsers() {
        return template.query("SELECT * FROM users", userListExtractor);
    }

    @Override
    public void insertObject(User user) {
        if (getUserById(user.getId()) != null) {
            observer.failed("(DataBase) User with id(" + user.getId() + ") already exist");
            return;
        }
        template.update("INSERT INTO users values(?, ?, ?)", user.getId(), user.getName(), user.getPassword());
        observer.success();
    }
}
