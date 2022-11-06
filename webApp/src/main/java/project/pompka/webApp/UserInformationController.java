package project.pompka.webApp;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/user_information")
public class UserInformationController {
    @PostMapping
    public ResponseEntity<UserInformation> postUserInformation(){
        boolean error = new Random().nextBoolean();
        if(error)
            return ResponseEntity.status(200).body(new UserInformation("tester"));
        return ResponseEntity.status(401).body(new UserInformation("tester"));
    }

    @GetMapping
    public ResponseEntity<UserInformation> getUserInformation(){
        boolean error = new Random().nextBoolean();
        if(error)
            return ResponseEntity.status(200).body(new UserInformation("tester"));
        return ResponseEntity.status(401).body(new UserInformation("tester"));
    }

    public record UserInformation(String username){};
}
