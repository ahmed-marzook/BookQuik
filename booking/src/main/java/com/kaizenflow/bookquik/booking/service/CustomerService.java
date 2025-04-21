package com.kaizenflow.bookquik.booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaizenflow.bookquik.booking.domain.entity.Customer;
import com.kaizenflow.bookquik.booking.domain.request.CreateCustomerRequest;
import com.kaizenflow.bookquik.booking.domain.response.CustomerResponse;
import com.kaizenflow.bookquik.booking.exception.ResourceAlreadyExistsException;
import com.kaizenflow.bookquik.booking.exception.ResourceNotFoundException;
import com.kaizenflow.bookquik.booking.mapper.CustomerMapper;
import com.kaizenflow.bookquik.booking.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer with email: {}", request.email());

        if (customerRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Customer", "email", request.email());
        }

        Customer customer = customerMapper.requestToEntity(request);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Created new customer with ID: {}", savedCustomer.getId());
        return customerMapper.entityToResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        return customerMapper.entityToResponse(customer);
    }

    public CustomerResponse getCustomerByEmail(String email) {
        log.info("Fetching customer with email: {}", email);

        Customer customer =
                customerRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        return customerMapper.entityToResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("Fetching all customers");

        return customerRepository.findAll().stream()
                .map(customerMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CreateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        // Check if email is being changed and if it's already taken
        if (!customer.getEmail().equals(request.email())
                && customerRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Customer", "email", request.email());
        }

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAddress(request.address());

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Updated customer with ID: {}", updatedCustomer.getId());

        return customerMapper.entityToResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer", "id", id);
        }

        customerRepository.deleteById(id);
        log.info("Deleted customer with ID: {}", id);
    }
}
