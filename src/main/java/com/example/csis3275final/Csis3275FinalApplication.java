package com.example.csis3275final;

import com.example.csis3275final.model.Seat;
import com.example.csis3275final.repository.SeatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Csis3275FinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(Csis3275FinalApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(SeatRepository seatRepository) {
        return args -> {
            seatRepository.save(new Seat(1L, "1A", null, null, 0));
            seatRepository.save(new Seat(2L, "2A", null, null, 0));
            seatRepository.save(new Seat(3L, "3A", null, null, 0));
            seatRepository.save(new Seat(4L, "4A", null, null, 0));

            seatRepository.save(new Seat(5L, "1B", null, null, 0));
            seatRepository.save(new Seat(6L, "2B", null, null, 0));
            seatRepository.save(new Seat(7L, "3B", null, null, 0));
            seatRepository.save(new Seat(8L, "4B", null, null, 0));

            seatRepository.save(new Seat(9L, "1C", null, null, 0));
            seatRepository.save(new Seat(10L, "2C", null, null, 0));
            seatRepository.save(new Seat(11L, "3C", null, null, 0));
            seatRepository.save(new Seat(12L, "4C", null, null, 0));

            seatRepository.save(new Seat(13L, "1D", null, null, 0));
            seatRepository.save(new Seat(14L, "2D", null, null, 0));
            seatRepository.save(new Seat(15L, "3D", null, null, 0));
            seatRepository.save(new Seat(16L, "4D", null, null, 0));

            seatRepository.save(new Seat(17L, "1E", null, null, 0));
            seatRepository.save(new Seat(18L, "2E", null, null, 0));
            seatRepository.save(new Seat(19L, "3E", null, null, 0));
            seatRepository.save(new Seat(20L, "4E", null, null, 0));
        };
    }
}
