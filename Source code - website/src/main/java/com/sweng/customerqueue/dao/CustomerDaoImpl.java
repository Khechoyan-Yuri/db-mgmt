package com.sweng.customerqueue.dao;

import com.sweng.customerqueue.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ZAnwar on 2/27/2017.
 */

@Repository
public class CustomerDaoImpl implements CustomerDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> findAll() {
        Session session = sessionFactory.openSession();
        List<Customer> customers = session.createCriteria(Customer.class).list();
        session.close();
        return customers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> findNotHandled() {
        Session session = sessionFactory.openSession();
        List<Customer> customers = session.createCriteria(Customer.class)
                .add(Restrictions.isNull("checkOutTime")).list();
        session.close();
        return customers;
    }


    @Override
    public Customer findById(Long id) {
        Session session = sessionFactory.openSession();
        Customer customer = session.get(Customer.class,id);
        session.close();
        return customer;
    }

    @Override
    public void save(Customer customer) {
        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Save the customer
        session.saveOrUpdate(customer);

        //Commit the transaction
        session.getTransaction().commit();

        //Close the session
        session.close();
    }

    @Override
    public void delete(Customer customer) {

    }
}
