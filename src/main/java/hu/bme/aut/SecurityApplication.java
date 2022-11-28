package hu.bme.aut;

import hu.bme.aut.model.User;
import hu.bme.aut.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            userRepository.save(new User("admin", encoder.encode("adminpass"), "ADMIN,USER"));
            userRepository.save(new User("user", encoder.encode("user"), "USER"));
            //userRepository.save(new User("Gabi", encoder.encode("password"), "USER, ADMIN"));
        };
    }
}