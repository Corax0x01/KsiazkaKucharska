package szymanski.jakub.KsiazkaKucharska.controllers;


import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Log
@Controller
public class KsiazkaKucharskaController {

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return "Hello, World!";
    }

}
