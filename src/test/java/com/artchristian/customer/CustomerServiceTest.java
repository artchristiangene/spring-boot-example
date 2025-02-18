package com.artchristian.customer;

import com.artchristian.exception.BadRequestException;
import com.artchristian.exception.DuplicateResourceException;
import com.artchristian.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                "Customer with ID [%s] is not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        String email = "art@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            "Art", email, 19
        );

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowExceptionWhenEmailExistsWhileAddingCustomer() {
        //Given
        String email = "art@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Art", email, 19
        );

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        //Then

        verify(customerDao, never()).insertCustomer(any());

    }



    @Test
    void deleteCustomerByID() {
        //Given
        int id = 10;
        when(customerDao.existsPersonWithID(id)).thenReturn(true);

        //When
        underTest.deleteCustomerByID(id);
        //Then
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowExceptionIfIdIsNotFoundWhenDeleteCustomerByID() {
        //Given
        int id = 10;
        when(customerDao.existsPersonWithID(id)).thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteCustomerByID(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID [%s] is not found".formatted(id));
        //Then
        verify(customerDao, never()).deleteCustomer(any());
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        String newEmail = "christian@gmail.com";
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Christian", newEmail, 31);
        underTest.updateCustomerById(id, updateRequest);


        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowExceptionWhenEmailIsAlreadyTakenWhenUpdateAllCustomerProperties() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        String newEmail = "christian@gmail.com";
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Christian", newEmail, 31);

        assertThatThrownBy(() -> underTest.updateCustomerById(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        //Then

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        //String newEmail = "christian@gmail.com";
        String newName = "Christian";
        //when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName,null,null);
        underTest.updateCustomerById(id, updateRequest);


        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        String newEmail = "christian@gmail.com";

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null,newEmail,null);
        underTest.updateCustomerById(id, updateRequest);


        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        //String newEmail = "christian@gmail.com";
        int newAge = 32;
        //when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null,null,newAge);
        underTest.updateCustomerById(id, updateRequest);


        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowExceptionWhenNoChangeUpdateCustomer(){

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newName = "Art";
        String newEmail = "art@gmail.com";
        int newAge = 30;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName,newEmail, newAge);

        assertThatThrownBy(() -> underTest.updateCustomerById(id, updateRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("no data changes found");

        //Then

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowExceptionWhenNoDataUpdateCustomer(){

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Art",
                "art@gmail.com",
                30
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        //String newEmail = "christian@gmail.com";
        //when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null,null, null);

        assertThatThrownBy(() -> underTest.updateCustomerById(id, updateRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("no data changes found");

        //Then

        verify(customerDao, never()).updateCustomer(any());
    }
}