package com.artchristian.customer;

import com.artchristian.exception.BadRequestException;
import com.artchristian.exception.DuplicateResourceException;
import com.artchristian.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
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

    public void deleteCustomerByID(Integer id){
        if(customerDao.existsPersonWithID(id)){
            customerDao.deleteCustomer(id);
        }
        else {
            throw new ResourceNotFoundException("Customer with ID [%s] is not found".formatted(id));
        }
    }

    public void updateCustomerById(Integer id,
                                   CustomerUpdateRequest customerUpdateRequest){
        Customer customerData = getCustomer(id);

        boolean isChanged = false;

        if(customerUpdateRequest.name()!= null && !customerUpdateRequest.name().equals(customerData.getName())){
            customerData.setName(customerUpdateRequest.name());
            isChanged = true;
        }

        if(customerUpdateRequest.email()!= null && !customerUpdateRequest.email().equals(customerData.getEmail())){
            if(customerDao.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException(
                        "email already taken");
            }
            customerData.setEmail(customerUpdateRequest.email());
            isChanged = true;
        }

        if(customerUpdateRequest.age()!= null && !customerUpdateRequest.age().equals(customerData.getAge())){
            customerData.setAge(customerUpdateRequest.age());
            isChanged = true;
        }

        if(!isChanged){
            throw new BadRequestException("no data changes found");
        }

        customerDao.updateCustomer(customerData);
    }
}

