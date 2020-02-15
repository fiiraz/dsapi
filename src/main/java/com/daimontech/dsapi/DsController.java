package com.daimontech.dsapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DsController {
    @GetMapping("/api")
    public String test() {
        return "Hello Deploy";
    }

    @GetMapping("/api2")
    public String test2() {
        return "Hello Deploy2";
    }
}
