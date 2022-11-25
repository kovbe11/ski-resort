package hu.bme.aut.controllerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControllerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControllerServiceApplication.class, args);
    }

}
