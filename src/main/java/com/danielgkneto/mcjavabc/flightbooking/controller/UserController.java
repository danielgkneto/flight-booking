package com.danielgkneto.mcjavabc.flightbooking.controller;

import com.danielgkneto.mcjavabc.flightbooking.dao.IFlightRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IPassengerRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IReservationRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IUserRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.Flight;
import com.danielgkneto.mcjavabc.flightbooking.entity.Passenger;
import com.danielgkneto.mcjavabc.flightbooking.entity.Reservation;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import com.danielgkneto.mcjavabc.flightbooking.service.FlightService;
import com.danielgkneto.mcjavabc.flightbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@SessionAttributes("reservation")
public class UserController {

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IFlightRepository flightRepository;

    @Autowired
    private UserService userService;

    @Autowired
    IPassengerRepository passengerRepository;

    @Autowired
    FlightService flightService;

    @RequestMapping("/myreservations")
    public String showMyReservations(Model model){
        User user = userService.getUser();
        model.addAttribute("reservations", reservationRepository.findAllByUser(user));
        return "myreservations";
    }

    @GetMapping("/flightsearch")
    public String showFlightSearchForm(Model model){
        model.addAttribute("reservation", new Reservation());
        Set<String> airports = flightService.getAirportList();
        model.addAttribute("airports", airports);
        return "flightsearch";
    }

    @PostMapping("/flightsearch")
    public String processFlightSearch(Model model, @ModelAttribute("reservation") Reservation reservation,
                                      @RequestParam(name="triptype") String tripType,
                                      @RequestParam(name="numberpassengers") String numberPassengers,
                                      @RequestParam(name = "departurelocation") String departureLocation,
                                      @RequestParam(name = "arrivallocation") String arrivalLocation) {

        if (tripType.equals("oneway")) {
            reservation.setReturnDate(null);
            reservation.setReturnFlight(null);
        }

        reservation.setNumberPassengers(Integer.parseInt(numberPassengers));

        model.addAttribute("depflights", flightRepository.findAllByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(departureLocation, arrivalLocation));
        model.addAttribute("retflights", flightRepository.findAllByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(arrivalLocation, departureLocation));

        model.addAttribute("depflight", new Flight());
        model.addAttribute("retflight", new Flight());

        return "flightsearchresults";
    }

    @PostMapping("/flightsearchresults")
    public String processFlightSearchResults(Model model, @ModelAttribute("reservation") Reservation reservation,
                                             @ModelAttribute("depflight") Flight depFlight,
                                             @ModelAttribute("retflight") Flight retFlight) {

        System.out.println(reservation.getDepartureFlight().getBasePrice());
//        reservation.setDepartureFlight(depFlight);
        System.out.println(reservation.getDepartureFlight() == null);
        if(reservation.getReturnDate() != null) {
            reservation.setReturnFlight(retFlight);
        }

        Set<Passenger> passengers = new HashSet<>();
        reservation.setPassengers(passengers);

        return "redirect:/passengerform";
    }

    @GetMapping("/passengerform")
    public String showPassengerForm(Model model, @ModelAttribute("reservation") Reservation reservation) {

        System.out.println(reservation.getPassengers().size());
        System.out.println(reservation.getNumberPassengers());
        if (reservation.getPassengers().size() < reservation.getNumberPassengers()) {
            model.addAttribute("passenger", new Passenger());
            return "passengerform";
        }
        else {
            return "redirect:/confirmreservation";
        }
    }

    @PostMapping("/passengerform")
    public String processPassengerForm(Model model, @ModelAttribute("reservation") Reservation reservation,
                               @ModelAttribute("passenger") Passenger passenger) {

        char seatType = passenger.getSeatNumber().charAt(passenger.getSeatNumber().length() - 1);
        if (seatType == 'A' | seatType == 'F')
            passenger.setWindow(true);

        reservation.getPassengers().add(passenger);

        return "redirect:/passengerform";
    }

    @GetMapping("/confirmreservation")
    public String showConfirmReservation(@ModelAttribute("reservation") Reservation reservation) {

        return "confirmreservation";
    }
    @PostMapping("/confirmreservation")
    public String confirmReservation(@ModelAttribute("reservation") Reservation reservation) {

        User user = userService.getUser();
        reservation.setUser(user);
        reservationRepository.save(reservation);

        for (Passenger p : reservation.getPassengers()) {
            p.setReservation(reservation);
            passengerRepository.save(p);
        }

        return "index";
    }

/*
    @RequestMapping("/showboardingpass")
    public String showBoardingPass(Model model,
                                   @ModelAttribute("reservation") Reservation reservation){

//        code here


        return "/boardingpass";
    }


    public double getTotalTripPrice(Reservation reservation){
        Flight departureFlight = reservation.getDepartureFlight();
        double pricePerPassDep = departureFlight.getPricePerPassenger(reservation.getFlightClass(),departureFlight.getBasePrice());
        double windowPrice = 5.00;
        int numPass = reservation.getNumberPassengers();
        double totalTripPrice = pricePerPassDep * numPass;
        if (reservation.getIsRoundTrip()==true) {
            Flight returnFlight = reservation.getReturnFlight();
            double pricePerPassRet = returnFlight.getPricePerPassenger(reservation.getFlightClass(), returnFlight.getBasePrice());
            windowPrice = 10.00;
            totalTripPrice += pricePerPassRet * numPass;
        }
        Collection<Passenger> passengers = reservation.getPassengers();
        for(Passenger passenger : passengers){
            if(passenger.getIsWindow()==true){
                totalTripPrice += windowPrice;
            }
        }
        return totalTripPrice;
    }


    @RequestMapping("/createQRCodeURL")
    public String createQRCodeURL(Model model, @RequestParam("user_id") String userId, @RequestParam("reservation_id") String reservationId)
            throws WriterException, IOException {
        System.out.println("Entered createQRCodeURL");
        String fileType = "png";
        int size = 125;
        String filePath = "QRcode.png";
        File qrFile = new File(filePath);

        StringBuilder qrCodeText = new StringBuilder();
        qrCodeText.append("http://localhost:8080?user_id=");
        qrCodeText.append(userId);
        qrCodeText.append("&reservation_id=");
        qrCodeText.append(reservationId);

        createQRImage(qrFile, qrCodeText.toString(), size, fileType);

        model.addAttribute("qrCodeURL", qrCodeText.toString());

        return "passengerlist";
    }

    private static BufferedImage createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {

        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
//        ImageIO.write(image, fileType, qrFile);
    }
//                              PLEASE DON'T DELETE THESE COMMENTS BELOW vvvvvvvvvvv
/*    @RequestMapping("/test/{id}")
    public String test(Model model, @PathVariable("id") long id){

        Reservation rTest = reservationRepository.findById(id).get();

*//*        String filePath = "QRcode.png";
        File qrFile = new File(filePath);
        try{
            BufferedImage image = createQRImage(qrFile, rTest.getUser().getFirstName(), 125, "png");
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bao);
            model.addAttribute("image_dgkn", bao.toByteArray());
        }
        catch (Exception e) {

        }*//*
        model.addAttribute("rtest", rTest);

        return "test";
    }

    @GetMapping(value = "/image_dgkn/{id}")
    public @ResponseBody byte[] getImage(@PathVariable("id") long id) throws WriterException, IOException {
        Reservation rTest = reservationRepository.findById(id).get();
        String filePath = "QRcode.png";
        File qrFile = new File(filePath);
            BufferedImage image = createQRImage(qrFile, rTest.getUser().getFirstName(), 125, "png");
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bao);

            return bao.toByteArray();
        }*/

}
