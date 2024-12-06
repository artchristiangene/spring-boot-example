package com.artchristian.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    private static List<Customer> customers;

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteCustomer(Integer id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().ifPresent(customers::remove);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithID(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(id));
    }


    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Art",
                "art@gmail.com",
                33
        );
        customers.add(alex);
        Customer mina = new Customer(
                2,
                "Mina",
                "mina@gmail.com",
                32
        );
        customers.add(mina);
    }
}
