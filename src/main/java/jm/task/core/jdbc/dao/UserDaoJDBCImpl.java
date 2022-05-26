package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;
import static jm.task.core.jdbc.util.Util.getQuery;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {
        try {
            connection = getConnection();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void createUsersTable() {
        String query = getQuery("createUsersTable");
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            statement.execute(query);
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    public void dropUsersTable() {
        String query = getQuery("dropUsersTable");
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            statement.execute(query);
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void cleanUsersTable() {
        String query = getQuery("cleanUsersTable");
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = getQuery("saveUser");
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
            connection.commit();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        String query = getQuery("removeUserById");
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            connection.commit();
        }
        catch (SQLException throwable) {
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        String query = getQuery("getAllUsers");
        List <User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)){
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                users.add(user);
            }
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally {
            return users;
        }
    }
}
