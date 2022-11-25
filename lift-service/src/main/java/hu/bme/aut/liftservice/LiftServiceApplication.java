package hu.bme.aut.liftservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LiftServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiftServiceApplication.class, args);
    }

}
