package szymanski.jakub.KsiazkaKucharska;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class KsiazkaKucharskaController {

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }
}
