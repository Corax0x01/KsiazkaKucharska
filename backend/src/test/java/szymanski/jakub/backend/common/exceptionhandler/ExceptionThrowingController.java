package szymanski.jakub.backend.common.exceptionhandler;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
@RequestMapping("test/exceptions")
public class ExceptionThrowingController {

    @GetMapping
    void throwException(
            @RequestParam String type,
            @RequestParam(required = false) String message) throws Throwable {
        Class<?> exceptionClass = Class.forName(type);
        if(Throwable.class.isAssignableFrom(exceptionClass)) {
            throw (Throwable) exceptionClass
                    .getConstructor(String.class)
                    .newInstance(message);
        } else {
            throw new IllegalArgumentException("Provided class type is not throwable");
        }
    }
}
