package com.danielgkneto.mcjavabc.flightbooking.controller;

import com.danielgkneto.mcjavabc.flightbooking.dao.IFlightRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

public class AdminController {

    @Autowired
    IFlightRepository flightRepository;

    @GetMapping("/addflight")
    public String addFlight(Model model) {
        model.addAttribute("flight", new Flight());
        return "flightform";
    }

    @PostMapping("/processflight")
    public String processFlight(Model model, @Valid Flight flight, BindingResult result) {
        if (result.hasErrors()) {
            return "flightform";
        }
        else {
            flightRepository.save(flight);
            model.addAttribute("message", "Flight added.");
            return "flightlist";
        }
    }

    @RequestMapping("flightlist")
    public String flightList(Model model) {
        model.addAttribute("flights", flightRepository.findAll());
        return "flightlist";
    }
}
