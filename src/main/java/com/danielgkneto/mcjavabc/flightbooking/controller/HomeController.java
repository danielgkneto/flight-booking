package com.danielgkneto.mcjavabc.flightbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
