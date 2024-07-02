package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.DTO.PaymentResDTO;
import com.example.movietickets.demo.config.Config;
import com.example.movietickets.demo.model.*;
import com.example.movietickets.demo.repository.RoomRepository;
import com.example.movietickets.demo.repository.SeatRepository;
import com.example.movietickets.demo.repository.UserRepository;
import com.example.movietickets.demo.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private ComboFoodService comboFoodService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String showPurchase(Model model, @RequestParam(required = false) Long scheduleId) {
        if (purchaseService.IsExist()) {
            Purchase purchase = purchaseService.Get();
            List<Category> categories = categoryService.getAllCategories();

            model.addAttribute("categories", categories);
            System.out.println("scheduleId: " + scheduleId);
            System.out.println("selectedSeats: " + purchase.getSeats());
            model.addAttribute("selectedSeats", purchase.getSeats());
            model.addAttribute("filmTitle", purchase.getFilmTitle());
            model.addAttribute("category", purchase.getCategory());
            model.addAttribute("cinemaName", purchase.getCinemaName());
            model.addAttribute("cinemaAddress", purchase.getCinemaAddress());
            model.addAttribute("startTime", purchase.getStartTime());
            model.addAttribute("roomName", purchase.getRoomName());
            model.addAttribute("poster", purchase.getPoster());
            //format Currency VND
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormat.format(purchase.getTotalPrice());
            model.addAttribute("totalPrice", formattedTotalPrice);

            Room room = roomRepository.findByName(purchase.getRoomName());
            List<Seat> seats = seatRepository.findByRoom(room);
            //lấy ra các seat booked
            model.addAttribute("purchase", purchase);
            model.addAttribute("seats", seats);
            model.addAttribute("scheduleId", scheduleId);
        }
        return "/purchase/purchase";
    }



    @GetMapping("/clear")
    public String clearPurchase() {
        purchaseService.clearPurchase();
        return "redirect:/purchase";
    }

    @GetMapping("/purchase/error")
    public String errorPurchase() {

        return "/purchase/purchase-error";
    }

    @PostMapping("/add")
    public String addPurchase(
            @RequestParam("seatSymbol") String seatSymbols,
            @RequestParam("totalPrice") Long totalPrice,
            @RequestParam("startTime") String startTime,
            @RequestParam("filmTitle") String filmTitle,
            @RequestParam("poster") String poster,
            @RequestParam("category") String category,
            @RequestParam("cinemaName") String cinemaName,
            @RequestParam("cinemaAddress") String cinemaAddress,
            @RequestParam("roomName") String roomName,
            @RequestParam("scheduleId") Long scheduleId,
            Model model
    ) {
        System.out.println("scheduleId in addPurchase: " + scheduleId); // Debugging
        purchaseService.addToBuy(seatSymbols, filmTitle, poster, category, totalPrice, cinemaAddress, cinemaName, startTime, roomName);
        model.addAttribute("scheduleId", scheduleId);
        return "redirect:/purchase?scheduleId=" + scheduleId;
    }

    @GetMapping("/history")
    public String showPurchaseHistory(Model model) {
        List<Booking> bookings = bookingService.getBookingsByCurrentUser(); // phương thức này để lấy các booking của người dùng hiện tại

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("bookings", bookings);
        return "/purchase/history";
    }

    @PostMapping("/checkout")
    public String checkout(
            @RequestParam("payment") String payment,
            @RequestParam Long scheduleId,
            RedirectAttributes redirectAttributes
    ) throws UnsupportedEncodingException {
        if (purchaseService.IsExist()) {
            Purchase purchase = purchaseService.Get();
            List<String> seatSymbols = new ArrayList<>();
            for (Purchase.Seat2 seat : purchase.getSeatsList()) {
                seatSymbols.add(seat.getSymbol());
            }

            // Kiểm tra phương thức thanh toán
            if ("vnpay".equalsIgnoreCase(payment)) {
                //return "redirect:/api/payment/create_payment?amount=" + purchase.getTotalPrice();
                return "redirect:/api/payment/create_payment?scheduleId=" + scheduleId + "&amount=" + purchase.getTotalPrice();
            }

            Room room = roomRepository.findByName(purchase.getRoomName());
            List<Seat> seats = bookingService.getSeatsFromSymbolsAndRoom(seatSymbols, room);

            // Lấy schedule từ scheduleId
            Schedule schedule = scheduleService.getScheduleById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id"));

            Booking booking = new Booking();
            booking.setFilmName(purchase.getFilmTitle());
            booking.setPoster(purchase.getPoster());
            booking.setCinemaName(purchase.getCinemaName());
            booking.setCinemaAddress(purchase.getCinemaAddress());
            booking.setStartTime(parseDate(purchase.getStartTime()));
            booking.setPrice(purchase.getTotalPrice());
            booking.setSeatName(purchase.getSeats());
            booking.setRoomName(purchase.getRoomName());
            booking.setPayment(payment);
            booking.setStatus(true); // Hoặc giá trị khác tùy vào logic của bạn
            booking.setCreateAt(new Date());

            // Lấy thông tin người dùng hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            booking.setUser(user);


            bookingService.saveBooking(booking, seats, schedule);

            redirectAttributes.addFlashAttribute("message", "Đặt vé thành công!");

        } else {
            redirectAttributes.addFlashAttribute("message", "Không có thông tin đặt vé.");
        }
        return "redirect:/purchase/history"; // Chuyển hướng đến trang lịch sử mua vé
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /*@GetMapping("create_payment")
    public String createPayment(long amount ) throws UnsupportedEncodingException {


        //String orderType = "other";
        //long amount = Integer.parseInt(req.getParameter("amount"))*100;
        //String bankCode = req.getParameter("bankCode");

        //long amount = 10000;
        String amountValue = String.valueOf(amount*100);
        String vnp_TxnRef = Config.getRandomNumber(8);
        //String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amountValue);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "vn");
        //vnp_Params.put("vnp_TransactionNo", "NCB");
        vnp_Params.put("vnp_IpAddr", Config.vnp_IpAddr);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other" );
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 5);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("OK");
        paymentResDTO.setMessage("Successfully");
        paymentResDTO.setUrl(paymentUrl);

        //return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);

        return paymentUrl;

    }

    @GetMapping("/payment_infor")
    public String transaction(@RequestParam(value = "vnp_amount") String amount,
                                         @RequestParam(value = "vnp_BankCode") String bankcode,
                                         @RequestParam(value = "vnp_OrderInfo") String order,
                                         @RequestParam(value = "vnp_ResponseCode") String responseCode){

        Purchase purchase = purchaseService.Get();
        List<String> seatSymbols = new ArrayList<>();
        for (Purchase.Seat2 seat : purchase.getSeatsList()) {
            seatSymbols.add(seat.getSymbol());
        }

        Room room = roomRepository.findByName(purchase.getRoomName());
        List<Seat> seats = bookingService.getSeatsFromSymbolsAndRoom(seatSymbols, room);

        // Lấy schedule từ scheduleId
        //Schedule schedule = scheduleService.getScheduleById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id"));
        if(responseCode == "00"){
            return  "/transaction/transaction-success";
        }

        return "/transaction/transaction-error";
    }*/
}
