package com.artchristian.customer;

import com.artchristian.exception.DuplicateResourceException;
import com.artchristian.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Customer with ID [%s] is not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //check if email exists
        String email  = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException(
                    "email already taken");
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Integer id){
        if(customerDao.existsPersonWithID(id)){
            customerDao.deleteCustomer(id);
        }
        else {
            throw new ResourceNotFoundException("Customer with ID [%s] is not found".formatted(id));
        }
    }
}

