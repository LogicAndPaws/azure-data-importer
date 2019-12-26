package com.epam.azuredataimporter.daoimporting;

import com.epam.azuredataimporter.entity.Entity;
import com.epam.azuredataimporter.entity.User;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostgresBase<T extends Entity> implements BaseImporter<T> {
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

    private String buildStatement(T object) {
        StringBuilder updateLine = new StringBuilder();
        updateLine.append("INSERT INTO ").append(object.getTableName()).append(" VALUES(");
        for (int i = 0; i < object.getFields().size(); i++) {
            updateLine.append("?");
            if (i == object.getFields().size() - 1) updateLine.append(")");
            else updateLine.append(", ");
        }
        return updateLine.toString();
    }

    @Override
    public void insertObject(T object) {
        Object[] args = object.getFields().toArray();
        int[] types = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Integer) types[i] = 4;
            else if (args[i] instanceof String) types[i] = 12;
            else if (args[i] instanceof Double) types[i] = 8;
        }
        try {
            template.update(buildStatement(object), args, types);
            observer.success();
        } catch (DuplicateKeyException ignore) {
            observer.failed("(Base) Line with id (" + object.getUniqueId() + ") already exists");
        } catch (DataIntegrityViolationException ignore) {
            observer.failed("(Base) Line with id (" + object.getUniqueId() + ") violates foreign key");
        }

    }
}
