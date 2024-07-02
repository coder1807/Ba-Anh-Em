package com.example.movietickets.demo.service;

import com.example.movietickets.demo.model.*;
import com.example.movietickets.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@SessionScope
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private SeatRepository seatRepository;

    private final ComboFoodRepository comboFoodRepository;

    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, ComboFoodRepository comboFoodRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.comboFoodRepository = comboFoodRepository;
        this.userRepository = userRepository;
    }

    public List<Seat> getSeatsFromSymbolsAndRoom(List<String> seatSymbols, Room room) {
        return seatRepository.findBySymbolInAndRoom(seatSymbols, room);
    }

    public List<Booking> getAllBookings() {
        // Thay đổi phương thức để lấy các booking của người dùng hiện tại
        return bookingRepository.findAll();
    }
    public List<Booking> getBookingsByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy User"));
        return bookingRepository.findByUser(user);
    }

    public Booking saveBookingWithCombo(Long userId, Long comboFoodId, Booking booking) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ComboFood comboFood = comboFoodRepository.findById(comboFoodId).orElseThrow(() -> new EntityNotFoundException("Combo not found"));

        booking.setUser(user);
        booking.setComboFood(comboFood);
        booking.setPrice(booking.getPrice() + comboFood.getPrice()); // Tính tổng giá mới

        return bookingRepository.save(booking);
    }


    @Transactional
    public void saveBooking(Booking booking, List<Seat> seats, Schedule schedule) {
        bookingRepository.save(booking);
        for (Seat seat : seats) {
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setBooking(booking);
            bookingDetail.setSeat(seat);
            bookingDetail.setPrice(seat.getSeattype().getPrice());
            bookingDetail.setSchedule(schedule);
            bookingDetailRepository.save(bookingDetail);
        }
        //cập nhật trạng thái ghé đã đặt
        for(Seat seat: seats){
            seat.setStatus("booked");
            seatRepository.save(seat);
        }
    }

}

