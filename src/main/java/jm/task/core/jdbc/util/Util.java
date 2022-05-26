package jm.task.core.jdbc.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class Util {

    private static Connection connection;
    private static Properties settings = new Properties();
    private static SessionFactory sessionFactory;

    static {
        try (InputStream input = Util.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties tmpSettings = new Properties();
            tmpSettings.load(input);

            for(Object obj : tmpSettings.keySet()) {
                String key = obj.toString();
                if(!key.startsWith("query")) continue;
                settings.put(key, tmpSettings.get(key));
            }

            String url = tmpSettings.getProperty("db.Url");
            String userName = tmpSettings.getProperty("db.UserName");
            String password = tmpSettings.getProperty("db.Password" );

            settings.put(Environment.URL,      url);
            settings.put(Environment.USER,     userName);
            settings.put(Environment.PASS,     password);
            settings.put(Environment.DRIVER,   tmpSettings.getProperty("db.Driver"));
            settings.put(Environment.DIALECT,  tmpSettings.getProperty("db.Dialect" ));
            settings.put(Environment.SHOW_SQL, tmpSettings.getProperty("db.ShowSql" ));
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, tmpSettings.getProperty("db.CurrentSessionContextClass"));
            settings.put(Environment.HBM2DDL_AUTO, tmpSettings.getProperty("db.Hbm2DllAuto" ));

            connection = DriverManager.getConnection(url, userName, password);

            Configuration configuration = new Configuration();
            configuration.setProperties(settings);

            sessionFactory = new org.hibernate.cfg.Configuration()
                    .addProperties(settings)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static String getQuery(String key) {
        return settings.getProperty("query." + key);
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }
}
