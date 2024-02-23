package szymanski.jakub.KsiazkaKucharska.controllers;


import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@Log
@RestController
public class KsiazkaKucharskaController {

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

}
