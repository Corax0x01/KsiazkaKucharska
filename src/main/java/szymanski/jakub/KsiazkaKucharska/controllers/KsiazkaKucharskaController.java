package szymanski.jakub.KsiazkaKucharska.controllers;


import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

@Log
@Controller
public class KsiazkaKucharskaController {

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return "Hello, World!";
    }

}
