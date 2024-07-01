package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    List<BookingDetail> findByScheduleId(Long scheduleId);

//    @Query("SELECT bd FROM BookingDetail bd WHERE bd.schedule.id = :scheduleId")
//    List<BookingDetail> findByScheduleId(@Param("scheduleId") Long scheduleId);
}
