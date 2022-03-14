package org.bng;

//import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class GreetingController {

    @GetMapping("/greeting")
    public String hello() {
        return "Hello Spring";
    }

}
