package com.ms.service_app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.service_app.entity.PanicResponse;

@RestController
@RequestMapping("/panic")
public class PanicController {

    @PostMapping
    public PanicResponse postPanic() {
        return new PanicResponse("Panic Triggered!");
    }
}
