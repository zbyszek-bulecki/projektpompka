package project.pompka.webApp;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/list")
public class ListExampleController {
    @GetMapping
    public ResponseEntity<List<ExampleValue>> getUserInformation(){
        if(new Random().nextInt(4) == 2){
            return ResponseEntity.status(403).body(List.of());
        }

        return ResponseEntity.ok(List.of(
                ExampleValue.of("Jan", "Kowalski", 23),
                ExampleValue.of("Karol", "Nowak", 23),
                ExampleValue.of("Tadeusz", "Malinowski", 23)
        ));
    }

    public record ExampleValue(String name, String surname, int age){
        public static ExampleValue of(String name, String surname, int age){
            return new ExampleValue(name, surname, age);
        }
    };
}
