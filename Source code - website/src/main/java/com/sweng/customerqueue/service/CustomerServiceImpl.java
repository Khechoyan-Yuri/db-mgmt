package com.sweng.customerqueue.service;


import com.sweng.customerqueue.model.Customer;
import com.sweng.customerqueue.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZAnwar on 2/28/2017.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;


    @Override
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public List<Customer> findNotHandled() { return customerDao.findNotHandled(); }

    @Override
    public Customer findById(Long id) {
        return customerDao.findById(id);
    }

    @Override
    public void save(Customer customer) { customerDao.save(customer); }

    @Override
    public void delete(Customer customer) {

    }
}
