package com.danielgkneto.mcjavabc.flightbooking.controller;

import com.danielgkneto.mcjavabc.flightbooking.dao.IFlightRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IPassengerRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IReservationRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IUserRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.Reservation;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import com.danielgkneto.mcjavabc.flightbooking.service.FlightService;
import com.danielgkneto.mcjavabc.flightbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "/myreservations";
    }

    @GetMapping("/flightsearch")
    public String showFlightSearchForm(Model model){
        model.addAttribute("reservation", new Reservation());
        Set<String> airports = flightService.getAirportList();
        model.addAttribute("airports", airports);
        return "flightsearchform";
    }

    /*
    @PostMapping("/processflightsearch")
    public String processFlightSearch(Model model, @ModelAttribute("reservation") Reservation reservation,
                                      @RequestParam(name="SearchSelectorNumPass") int numPass,
                                      @RequestParam(name="SearchSelectorRT") String rtrip,
                                      @RequestParam(name="SearchSelectorPassClass") String passClass,
                                      @RequestParam(name = "SearchSelectorDepApt") String depApt,
                                      @RequestParam(name = "SearchSelectorArrApt") String arrApt,
                                      @RequestParam(name = "depDate") String depDate,
                                      @RequestParam(name = "retDate") String retDate,
                                      HttpServletRequest request) {

        request.setAttribute("depFlights", flightRepository.findAllByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(depApt, arrApt));
        request.setAttribute("retFlights", flightRepository.findAllByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(arrApt, depApt));
        if (!rtrip.equals("RoundTrip")) {
            reservation.setReturnDate(null);
        }

        String pattern = "yyyy-MM-dd";
        try {
            String formattedDepDate = depDate.substring(1);
            SimpleDateFormat simpleDepDateFormat = new SimpleDateFormat(pattern);
            Date realDepDate = simpleDepDateFormat.parse(formattedDepDate);
//            model.addAttribute("depDate", realDepDate);
            reservation.setDepartureDate(realDepDate);

        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        if (reservation.getIsRoundTrip()) {
            try {
                String formattedRetDate = retDate.substring(1);
                SimpleDateFormat simpleRetDateFormat = new SimpleDateFormat(pattern);
                Date realRetDate = simpleRetDateFormat.parse(formattedRetDate);
//            model.addAttribute("retDate", realRetDate);
                reservation.setReturnDate(realRetDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        reservation.setNumberPassengers(numPass);
        reservation.setFlightClass(passClass);
//        model.addAttribute(reservation);
        request.setAttribute("reservation", reservation);

        model.addAttribute("numPass", numPass);
        model.addAttribute("passClass", passClass);
        model.addAttribute("depApt", depApt);
        model.addAttribute("arrApt", arrApt);

        return "forward:/listSearchResults";
    }

    @RequestMapping("/listSearchResults")
    public String showSearchResultsForm(HttpServletRequest request, Model model){

        Reservation r = (Reservation) request.getAttribute("reservation");
        model.addAttribute("depFlights", request.getAttribute("depFlights"));

        if (r.getIsRoundTrip() == true) {
            model.addAttribute("retFlights", request.getAttribute("retFlights"));
        }
        else {
            model.addAttribute("retFlights", null);
        }
        Passenger passenger = new Passenger();
        model.addAttribute("passenger", passenger);
//        Collection<Passenger> passengers = new ArrayList<>();
//        for (int i = 0; i < r.getNumberPassengers(); i++) {
//            passengers.add(new Passenger());
//        }
//        r.setPassengers(passengers);
//        model.addAttribute("passengers", passengers);


        model.addAttribute("reservation", r);

        return "listsearchresults";
    }

    @PostMapping("/processpickflights")
    public String processPickFlights(@ModelAttribute("reservation") Reservation reservation,
                                     Model model,
                                     HttpServletRequest request,
                                     @RequestParam(name="depFlightRadio") Flight depFlight,
                                     @RequestParam(name="retFlightRadio") Flight retFlight) {
        reservation.setDepartureFlight(depFlight);
        if(reservation.getIsRoundTrip()==true) {
            reservation.setReturnFlight(retFlight);
        }
        Set<Passenger> passengers = new HashSet<>();
        reservation.setPassengers(passengers);
        request.setAttribute("reservation", reservation);

        return "forward:/passengerform";
    }

    @RequestMapping("/passengerform")
    public String passengerForm(@ModelAttribute("reservation") Reservation reservation, Model model) {
        model.addAttribute("reservation", reservation);

        Passenger p = null;
        if (reservation.getPassengers().size() < reservation.getNumberPassengers()) {
            p = new Passenger();
        }
        model.addAttribute("passenger", p);

        return "passengerform";
    }

    @PostMapping("/addpassenger")
    public String addPassenger(@ModelAttribute("reservation") Reservation reservation,
                               @ModelAttribute("passenger") Passenger passenger,
                               Model model,
                               HttpServletRequest request) {
        reservation.getPassengers().add(passenger);

        request.setAttribute("reservation", reservation);

        return "forward:/passengerform";
    }

    @PostMapping("/confirm")
    public String confirm(@ModelAttribute("reservation") Reservation reservation) {
        reservationRepository.save(reservation);
        return "index";
    }

    @PostMapping("/confirmReservation")
    public String confirmReservation(@ModelAttribute("reservation") Reservation reservation,
                                     Model model,
                                     @RequestParam(name="depFlightRadio") Flight depFlight,
                                     @RequestParam(name="retFlightRadio") Flight retFlight){
//                                     @RequestParam(name="p1id") Long p1id,
//                                     @RequestParam(name = "p1firstName") String p1firstName,
//                                     @RequestParam(name = "p1lastName") String p1lastName,
//                                     @RequestParam(name = "p1seatNumber") String p1seatNumber,
//                                     @RequestParam(name="p2id") Long p2id,
//                                     @RequestParam(name = "p2firstName") String p2firstName,
//                                     @RequestParam(name = "p2lastName") String p2lastName,
//                                     @RequestParam(name = "p2seatNumber") String p2seatNumber,
//                                     @RequestParam(name="p3id") Long p3id,
//                                     @RequestParam(name = "p3firstName") String p3firstName,
//                                     @RequestParam(name = "p3lastName") String p3lastName,
//                                     @RequestParam(name = "p3seatNumber") String p3seatNumber,
//                                     @RequestParam(name="p4id") Long p4id,
//                                     @RequestParam(name = "p4firstName") String p4firstName,
//                                     @RequestParam(name = "p4lastName") String p4lastName,
//                                     @RequestParam(name = "p4seatNumber") String p4seatNumber,
//                                     @RequestParam(name="p5id") Long p5id,
//                                     @RequestParam(name = "p5firstName") String p5firstName,
//                                     @RequestParam(name = "p5lastName") String p5lastName,
//                                     @RequestParam(name = "p5seatNumber") String p5seatNumber) {
        reservation.setDepartureFlight(depFlight);
        if(reservation.getIsRoundTrip()==true) {
            reservation.setReturnFlight(retFlight);
        }
        Collection<Passenger> passengers = new ArrayList<>();
//        Passenger passenger1 = new Passenger();
////        passenger1.setId(p1id);
//        passenger1.setFirstName(p1firstName);
//        passenger1.setLastName(p1lastName);
//        passenger1.setSeatNumber(p1seatNumber);
//        if(p1seatNumber.endsWith("A") | p1seatNumber.endsWith("F")){
//            passenger1.setIsWindow(true);
//        } else {
//            passenger1.setIsWindow(false);
//        }
//        passengers.add(passenger1);
//        if(reservation.getNumberPassengers()>1) {
//            Passenger passenger2 = new Passenger();
////            passenger2.setId(p2id);
//            passenger2.setFirstName(p2firstName);
//            passenger2.setLastName(p2lastName);
//            passenger2.setSeatNumber(p2seatNumber);
//            if(p2seatNumber.endsWith("A") | p2seatNumber.endsWith("F")){
//                passenger2.setIsWindow(true);
//            } else {
//                passenger2.setIsWindow(false);
//            }
//            passengers.add(passenger2);
//        }
//        if(reservation.getNumberPassengers()>2) {
//            Passenger passenger3 = new Passenger();
////            passenger3.setId(p3id);
//            passenger3.setFirstName(p3firstName);
//            passenger3.setLastName(p3lastName);
//            passenger3.setSeatNumber(p3seatNumber);
//            if(p3seatNumber.endsWith("A") | p3seatNumber.endsWith("F")){
//                passenger3.setIsWindow(true);
//            } else {
//                passenger3.setIsWindow(false);
//            }
//            passengers.add(passenger3);
//        }
//        if(reservation.getNumberPassengers()>3) {
//            Passenger passenger4 = new Passenger();
////            passenger4.setId(p4id);
//            passenger4.setFirstName(p4firstName);
//            passenger4.setLastName(p4lastName);
//            passenger4.setSeatNumber(p4seatNumber);
//            if(p4seatNumber.endsWith("A") | p4seatNumber.endsWith("F")){
//                passenger4.setIsWindow(true);
//            } else {
//                passenger4.setIsWindow(false);
//            }
//            passengers.add(passenger4);
//        }
//        if(reservation.getNumberPassengers()>4) {
//            Passenger passenger5 = new Passenger();
////            passenger5.setId(p5id);
//            passenger5.setFirstName(p5firstName);
//            passenger5.setLastName(p5lastName);
//            passenger5.setSeatNumber(p5seatNumber);
//            if(p5seatNumber.endsWith("A") | p5seatNumber.endsWith("F")){
//                passenger5.setIsWindow(true);
//            } else {
//                passenger5.setIsWindow(false);
//            }
//            passengers.add(passenger5);
//        }
//        reservation.setPassengers(passengers);
//        for(Passenger passenger : passengers){
//            passengerRepository.save(passenger);
//        }
        User user = userService.getUser();
        reservation.setUser(user);
//        reservationRepository.save(reservation);
        model.addAttribute("reservation", reservation);
        model.addAttribute("passengers", passengers);
        return "/passengerform";
    }

    @GetMapping("/passengerform")
    public String getPassengerForm(Model model,
                                   @ModelAttribute("reservation") Reservation reservation,
                                   @ModelAttribute("passengers") Collection<Passenger> passengers,
                                   HttpServletRequest request){
//        Collection<Passenger> passengers = new ArrayList<>();
        model.addAttribute("passengers", passengers);
        model.addAttribute("reservation", reservation);
        return "/passengerform";
    }

    @PostMapping("/processpassenger")
    public String processForm(@Valid Passenger passenger,
                              BindingResult result,
                              Model model,
                              @ModelAttribute("reservation") Reservation reservation,
                              @ModelAttribute("passengers") Collection<Passenger> passengers,
                              @RequestParam(name = "seatNumber") String seatNumber,
                              HttpServletRequest request){
        if(result.hasErrors()){
            return "passengerform";
        }
        if(seatNumber.endsWith("A") | seatNumber.endsWith("F")){
            passenger.setIsWindow(true);
        } else {
            passenger.setIsWindow(false);
        }
        passengers.add(passenger);
        passengerRepository.save(passenger);
        if(passengers.size() < reservation.getNumberPassengers()) {
            model.addAttribute("reservation", reservation);
            model.addAttribute("passengers", passengers);
            return "redirect:/passengerform";
        }else{
            reservation.setPassengers(passengers);
            model.addAttribute("reservation", reservation);
            return "/showboardingpass";
        }
    }

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
