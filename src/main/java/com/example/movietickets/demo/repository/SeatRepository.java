package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.Room;
import com.example.movietickets.demo.model.Schedule;
import com.example.movietickets.demo.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByRoomId(Long roomId);
   // List<Seat> findByRoomIdAndScheduleId(Long roomId, Long scheduleId);
}
