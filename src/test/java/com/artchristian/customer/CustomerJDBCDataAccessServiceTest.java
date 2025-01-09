package com.artchristian.customer;

import com.artchristian.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
            FAKER.name().fullName(),
            FAKER.internet().emailAddress()+ "-" + UUID.randomUUID(),
            20
        );

        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById(){
        //Given
        int id = -1;

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void deleteCustomer() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        underTest.deleteCustomer(id);


        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void insertCustomer() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        //When
        boolean actual = underTest.existsPersonWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void willReturnFalseWhenEmailDoesNotExists(){
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsPersonWithEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithID() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When

        boolean actual = underTest.existsPersonWithID(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void willReturnFalseWhenIdDoesNotExist() {
        //Given
        int id = -1;

        //When

        boolean actual = underTest.existsPersonWithID(id);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        String newName = "art";
        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);
        customerUpdate.setName(newName);

        underTest.updateCustomer(customerUpdate);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When

        Customer customerUpdate = new Customer();

        String newEmail = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        customerUpdate.setId(id);
        customerUpdate.setEmail(newEmail);

        underTest.updateCustomer(customerUpdate);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        int newAge = 0;
        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);
        customerUpdate.setAge(newAge);

        underTest.updateCustomer(customerUpdate);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void updateAllPropertiesCustomer() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        int newAge = 0;
        String newName = "art2";
        String newEmail = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);
        customerUpdate.setAge(newAge);
        customerUpdate.setEmail(newEmail);
        customerUpdate.setName(newName);

        underTest.updateCustomer(customerUpdate);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(customerUpdate);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);


        underTest.updateCustomer(customerUpdate);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}