package jm.task.core.jdbc;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;


import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserDao userService = new UserDaoHibernateImpl();

        userService.createUsersTable();

        userService.saveUser("James", "Harrison", (byte) 25);
        userService.saveUser("Nick", "Murray", (byte) 20);
        userService.saveUser("Naomi", "Osaka", (byte) 22);
        userService.saveUser("Petra", "Kvitova", (byte) 28);

        for (User user : userService.getAllUsers()){
            System.out.println(user);
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }



}
