package com.daimontech.dsapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DsController {
    @GetMapping("/test")
    public String test() {
        return "Hello Deploy";
    }

    @GetMapping("/test1")
    public String test2() {
        return "Hello Deploy2";
    }
}
