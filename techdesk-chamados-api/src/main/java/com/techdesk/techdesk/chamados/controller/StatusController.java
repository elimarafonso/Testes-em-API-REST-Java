package com.techdesk.techdesk.chamados.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
	public String status(){
        return "TechDesk Chamados api no ar";
    }


}
