package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.Purchase;
import com.example.movietickets.demo.service.PurchaseService;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public String showPurchase(Model model) {
        if (!purchaseService.getPurchases().isEmpty()) {
            Purchase purchase = purchaseService.getPurchases().get(0); // Lấy Purchase đầu tiên để hiển thị
            model.addAttribute("filmTitle", purchase.getFilmTitle());
            model.addAttribute("category", purchase.getCategory());
            model.addAttribute("cinemaName", purchase.getCinemaName());
            model.addAttribute("cinemaAddress", purchase.getCinemaAddress());
            model.addAttribute("startTime", purchase.getStartTime());
            model.addAttribute("roomName", purchase.getRoomName());
            model.addAttribute("selectedSeats", purchase.getSeatSymbols());
            //format Currency VND
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormat.format(purchase.getTotalPrice());
            model.addAttribute("totalPrice",formattedTotalPrice);
        }
        return "/purchase/purchase";
    }

    @GetMapping("/clear")
    public String clearPurchase() {
        purchaseService.clearPurchase();
        return "redirect:/purchase";
    }

    @PostMapping("/add")
    public String addPurchase(
            @RequestParam("seatSymbol") String seatSymbol,
            @RequestParam("totalPrice") Long totalPrice,
            @RequestParam("startTime") String startTime,
            @RequestParam("filmTitle") String filmTitle,
            @RequestParam("poster") String poster,
            @RequestParam("category") String category,
            @RequestParam("cinemaName") String cinemaName,
            @RequestParam("cinemaAddress") String cinemaAddress,
            @RequestParam("roomName") String roomName,
            Model model
    ) {
//        System.out.println("seatSymbol: " + seatSymbol);
//        System.out.println("totalPrice: " + totalPrice);
//        System.out.println("startTime: " + startTime);
//        System.out.println("filmTitle: " + filmTitle);
//        System.out.println("poster: " + poster);
//        System.out.println("category: " + category);
//        System.out.println("cinemaName: " + cinemaName);
//        System.out.println("cinemaAddress: " + cinemaAddress);
//        System.out.println("roomName: " + roomName);

        List<String> selectedSeats = new Gson().fromJson(seatSymbol, new TypeToken<List<String>>() {}.getType());
        String selectedSeatsString = String.join(", ", selectedSeats);
        purchaseService.addToBuy(selectedSeatsString, filmTitle, poster, category, totalPrice, cinemaName, cinemaAddress, startTime, roomName);

        model.addAttribute("selectedSeats", selectedSeatsString);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("startTime", startTime);
        model.addAttribute("filmTitle", filmTitle);
        model.addAttribute("poster", poster);
        model.addAttribute("category", category);
        model.addAttribute("cinemaName", cinemaName);
        model.addAttribute("cinemaAddress", cinemaAddress);
        model.addAttribute("roomName", roomName);

        return "redirect:/purchase";
    }
}
