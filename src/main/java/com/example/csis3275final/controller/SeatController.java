package com.example.csis3275final.controller;
import com.example.csis3275final.model.Seat;
import com.example.csis3275final.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class SeatController {
    private final SeatRepository seatRepository;

    @Autowired
    public SeatController(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @GetMapping("/")
    public String showSeatReservation(Model model) {
        List<Seat> seats = seatRepository.findAll();
        List<List<String>> rows = new ArrayList<>();
        List<String> row1 = new ArrayList<>();
        List<String> row2 = new ArrayList<>();
        List<String> row3 = new ArrayList<>();
        List<String> row4 = new ArrayList<>();
        int availableSeats = 20;
        for (Seat seat : seats) {
            if (seat.getSeatCode().startsWith("1")) {
                if (seat.getCustomerName() != null) {
                    row1.add(seat.getCustomerName());
                    availableSeats -= 1;
                } else {
                    row1.add(seat.getSeatCode());
                }
            } else if (seat.getSeatCode().startsWith("2")) {
                if (seat.getCustomerName() != null) {
                    row2.add(seat.getCustomerName());
                    availableSeats -= 1;
                } else {
                    row2.add(seat.getSeatCode());
                }
            } else if (seat.getSeatCode().startsWith("3")) {
                if (seat.getCustomerName() != null) {
                    availableSeats -= 1;
                    row3.add(seat.getCustomerName());
                } else {
                    row3.add(seat.getSeatCode());
                }
            } else if (seat.getSeatCode().startsWith("4")) {
                if (seat.getCustomerName() != null) {
                    availableSeats -= 1;
                    row4.add(seat.getCustomerName());
                } else {
                    row4.add(seat.getSeatCode());
                }
            }
        }
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        model.addAttribute("seats", seats);
        model.addAttribute("rows", rows);
        model.addAttribute("availableSeats", availableSeats);
        return "reservation";
    }

    @PostMapping("/reserve")
    public String reserveSeat(@RequestParam String seatCode,
                              @RequestParam String customerName,
                              @RequestParam String transactionDate) throws Exception {
        Seat seat = seatRepository.findBySeatCode(seatCode)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        if (seat.getCustomerName() != null) {
            throw new Exception("This seat is reserved!");
        }

        seat.setCustomerName(customerName);
        seat.setTransactionDate(LocalDate.parse(transactionDate));

        List<Seat> seats = seatRepository.findAll();
        int maxCode = 0;
        for (Seat s: seats) {
            if (s.getTransactionCode() > maxCode) {
                maxCode = s.getTransactionCode();
            }
        }
        seat.setTransactionCode(maxCode + 1);
        seatRepository.save(seat);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seat Id:" + id));
        model.addAttribute("seat", seat);
        return "edit-seat";
    }

    @PostMapping("/save-edit")
    public String editTransaction(@ModelAttribute Seat seat) {
        Seat newSeat = seatRepository.findBySeatCode(seat.getSeatCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid seat Id:" + seat.getId()));

        List<Seat> seats = seatRepository.findAll();
        int maxCode = 0;
        for (Seat s: seats) {
            if (s.getTransactionCode() > maxCode) {
                maxCode = s.getTransactionCode();
            }
        }
        newSeat.setTransactionCode(maxCode + 1);

        newSeat.setCustomerName(seat.getCustomerName());
        newSeat.setTransactionDate(seat.getTransactionDate());
        newSeat.setSeatCode(seat.getSeatCode());
        seatRepository.save(newSeat);

        if (!newSeat.getId().equals(seat.getId())) {
            seat.setCustomerName(null);
            seat.setTransactionDate(null);
            seatRepository.save(seat);
        }

        return "redirect:/";
    }

}
