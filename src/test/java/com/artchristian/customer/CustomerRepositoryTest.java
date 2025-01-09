package com.artchristian.customer;

import com.artchristian.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {

    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.save(customer);

        //When
        boolean actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.save(customer);
        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When

        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdDoesNotExists() {
        //Given
        int id = -1;

        //When

        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();
    }
}