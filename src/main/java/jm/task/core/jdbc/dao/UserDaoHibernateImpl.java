package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getQuery;
import static jm.task.core.jdbc.util.Util.getSessionFactory;


public class UserDaoHibernateImpl extends UserDaoJDBCImpl {

    private SessionFactory sessionFactory;
    private Transaction transaction;

    public UserDaoHibernateImpl() {
        super();
        this.sessionFactory = getSessionFactory();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
        catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
                throw he;
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User();
            user.setId(id);
            session.delete(user);
            transaction.commit();
        }
        catch (HibernateException he) {
         if (transaction != null) {
             transaction.rollback();
             throw he;
         }
        }

    }

    @Override
    public List<User> getAllUsers() {
        List <User> users =  new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            users = session.createQuery("FROM User").list();
        }
        catch (HibernateException he) {
            he.printStackTrace();
        }
        finally {
            return users;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
                throw ex;
            }
        }
    }
}
