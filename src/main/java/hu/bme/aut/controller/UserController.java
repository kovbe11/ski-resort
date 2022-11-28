package hu.bme.aut.controller;

import hu.bme.aut.model.User;
import hu.bme.aut.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find user"));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id){userRepository.deleteById(id);}

}
