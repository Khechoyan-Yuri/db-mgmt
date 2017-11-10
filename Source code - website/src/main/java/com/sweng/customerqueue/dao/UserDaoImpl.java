package com.sweng.customerqueue.dao;

import com.sweng.customerqueue.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zahid on 4/11/2017.
 */
@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private SessionFactory sessionFactory;

    public User findByUsername(String username){
        User user = null;
        Session session = sessionFactory.openSession();
        Criteria cr = session.createCriteria(User.class);
        cr.add(Restrictions.eq("username", username));

        Object result = cr.uniqueResult();
        if (result != null) {
            user = (User) result;
        }
        session.close();
        return user;
    }
}
