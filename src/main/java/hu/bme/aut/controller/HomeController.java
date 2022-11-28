package hu.bme.aut.controller;

import hu.bme.aut.model.User;
import hu.bme.aut.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Transactional
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String showAdmin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @GetMapping("/login")
    public String getMyLogin() {
        return "login";
    }

    @GetMapping("/loginError")
    public String getLoginError() {
        return "loginError";
    }


    @GetMapping("/admin/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/createUser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user",user);
        return "createUser";
    }

    @PostMapping("/admin/createUser")
    public String createUserSubmit(User user) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = new User(user.getUsername(), encoder.encode(user.getPassword()), user.getRoles());
        userRepository.save(newUser);
        return "redirect:/admin";
    }


    @GetMapping("admin/edit/{id}")
    public String datasheet(Model model, @PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find user"));
        model.addAttribute("user",user);
        return "edit";
    }

    @PostMapping("admin/edit/{id}")
    public String datasheetSubmit(User editedUser, @PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find user"));
        user.setUsername(editedUser.getUsername());
        user.setRoles(editedUser.getRoles());
        return "redirect:/admin";
    }
}
