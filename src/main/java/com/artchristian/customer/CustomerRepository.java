package com.artchristian.customer;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface CustomerRepository
        extends JpaRepository<Customer, Integer>{

    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
}
