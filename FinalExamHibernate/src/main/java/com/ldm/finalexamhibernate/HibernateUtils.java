package com.ldm.finalexamhibernate;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.ldm.pojo.AdditionalGrade;
import com.ldm.pojo.Comment;
import com.ldm.pojo.Course;
import com.ldm.pojo.CourseSession;
import com.ldm.pojo.Enrollment;
import com.ldm.pojo.ForumPost;
import com.ldm.pojo.Grade;
import com.ldm.pojo.Notification;
import com.ldm.pojo.User;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author admin
 */
public class HibernateUtils {
    private static final SessionFactory FACTORY;
    
    static {
        Configuration conf = new Configuration();
        Properties props = new Properties();
        props.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        props.setProperty(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
        props.setProperty(Environment.JAKARTA_JDBC_URL, "jdbc:mysql://localhost/wsddatabase");
        props.setProperty(Environment.JAKARTA_JDBC_USER, "root");
        props.setProperty(Environment.JAKARTA_JDBC_PASSWORD, "Admin@123");
        props.setProperty(Environment.SHOW_SQL, "true");
        
        conf.setProperties(props);
        
        conf.addAnnotatedClass(AdditionalGrade.class);
        conf.addAnnotatedClass(Comment.class);
        conf.addAnnotatedClass(Course.class);
        conf.addAnnotatedClass(CourseSession.class);
        conf.addAnnotatedClass(Enrollment.class);
        conf.addAnnotatedClass(ForumPost.class);
        conf.addAnnotatedClass(Grade.class);
        conf.addAnnotatedClass(Notification.class);
        conf.addAnnotatedClass(User.class);
        
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        
        FACTORY = conf.buildSessionFactory(serviceRegistry);
    }

    /**
     * @return the FACTORY
     */
    public static SessionFactory getFACTORY() {
        return FACTORY;
    }
}