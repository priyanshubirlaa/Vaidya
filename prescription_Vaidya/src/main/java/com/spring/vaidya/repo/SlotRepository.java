package com.spring.vaidya.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.Slot;


public interface SlotRepository extends JpaRepository<Slot, Long> {

}
