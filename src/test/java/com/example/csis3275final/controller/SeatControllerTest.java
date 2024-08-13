package com.example.csis3275final.controller;

import com.example.csis3275final.model.Seat;
import com.example.csis3275final.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatController seatController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(seatController).build();
    }

    @Test
    void testShowSeatReservation() throws Exception {
        // Given
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(1L, "1A", null, null, 0));
        seats.add(new Seat(2L, "2A", "John Doe", LocalDate.now(), 1));
        when(seatRepository.findAll()).thenReturn(seats);

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("seats"))
                .andExpect(model().attributeExists("rows"))
                .andExpect(model().attributeExists("availableSeats"));

        verify(seatRepository, times(1)).findAll();
    }

    @Test
    void testReserveSeat_Success() throws Exception {
        // Given
        Seat seat = new Seat(1L, "1A", null, null, 0);
        when(seatRepository.findBySeatCode("1A")).thenReturn(Optional.of(seat));

        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(2L, "2A", "John Doe", LocalDate.now(), 1));
        when(seatRepository.findAll()).thenReturn(seats);

        // When & Then
        mockMvc.perform(post("/reserve")
                        .param("seatCode", "1A")
                        .param("customerName", "Jane Doe")
                        .param("transactionDate", "2024-08-12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(seatRepository, times(1)).findBySeatCode("1A");
        verify(seatRepository, times(1)).findAll();
        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    void testReserveSeat_SeatAlreadyReserved() throws Exception {
        // Given
        Seat seat = new Seat(1L, "1A", "John Doe", LocalDate.now(), 0);
        when(seatRepository.findBySeatCode("1A")).thenReturn(Optional.of(seat));

        // When & Then
        mockMvc.perform(post("/reserve")
                        .param("seatCode", "1A")
                        .param("customerName", "Jane Doe")
                        .param("transactionDate", "2024-08-12"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(seatRepository, times(1)).findBySeatCode("1A");
        verify(seatRepository, never()).save(any());
    }

    @Test
    void testShowEditForm() throws Exception {
        // Given
        Seat seat = new Seat(1L, "1A", "John Doe", LocalDate.now(), 1);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // When & Then
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-seat"))
                .andExpect(model().attributeExists("seat"));

        verify(seatRepository, times(1)).findById(1L);
    }

    @Test
    void testEditTransaction() throws Exception {
        // Given
        Seat existingSeat = new Seat(1L, "1A", "John Doe", LocalDate.now(), 1);
        when(seatRepository.findBySeatCode("1A")).thenReturn(Optional.of(existingSeat));

        Seat newSeat = new Seat(1L, "1A", "Jane Doe", LocalDate.parse("2024-08-12"), 2);

        // When & Then
        mockMvc.perform(post("/save-edit")
                        .flashAttr("seat", newSeat))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(seatRepository, times(1)).findBySeatCode("1A");
        verify(seatRepository, times(1)).save(existingSeat);
    }
}
