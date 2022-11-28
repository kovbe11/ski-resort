package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @GetMapping("/login")
    public ModelAndView getMyLogin() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mylogin.html");
        return modelAndView;
    }

    @GetMapping("/loginUser")
    public ModelAndView getLoginUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("loginUser.html");
        return modelAndView;
    }

    @GetMapping("/success")
    public ModelAndView getSuccess() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success.html");
        return modelAndView;
    }

    @GetMapping("/loginError")
    public ModelAndView getLoginError() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error.html");
        return modelAndView;
    }
}
