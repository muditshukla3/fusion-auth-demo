package com.ms.service_app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms.service_app.entity.Change;

@RestController
@RequestMapping("/make-change")
public class ChangeController {
    
    @GetMapping
    public Change get(@RequestParam(required = false) BigDecimal total) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        System.out.println(email);
        var change = new Change();
        change.setTotal(total);
        change.setNickels(total.divide(new BigDecimal("0.05"), RoundingMode.HALF_DOWN).intValue());
        change.setPennies(total.subtract(new BigDecimal("0.05")
                        .multiply(new BigDecimal(change.getNickels())))
                .multiply(new BigDecimal(100))
                .intValue());
        return change;
    }
}
