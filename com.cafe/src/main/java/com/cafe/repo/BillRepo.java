package com.cafe.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.entities.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer> {

}
