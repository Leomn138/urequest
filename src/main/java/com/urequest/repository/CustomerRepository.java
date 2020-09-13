package com.urequest.repository;

import com.urequest.domain.Customer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    @Cacheable("customer")
    Optional<Customer> findById(Integer id);
}
