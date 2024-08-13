package com.example.csis3275final.repository;

import com.example.csis3275final.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySeatCode(String seatCode);
}
