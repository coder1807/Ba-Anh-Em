package com.example.movietickets.demo.service;

import com.example.movietickets.demo.model.Purchase;
import com.example.movietickets.demo.model.Seat;
import com.example.movietickets.demo.repository.SeatRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Service
@SessionScope
public class PurchaseService {
    private List<Purchase> purchases = new ArrayList<>();

    public void addToBuy(String seatSymbols, String filmTitle, String poster, String category,
                         Long totalPrice, String cinemaName, String cinemaAddress, String startTime , String roomName) {
        purchases.add(new Purchase(seatSymbols, filmTitle, poster, category, totalPrice, cinemaAddress, cinemaName, startTime, roomName));
    }


    public void clearPurchase() {
        purchases.clear();
    }
}
