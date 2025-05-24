package com.greenacademy.productstore.controller;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private UserServices userServices;

    public AuthController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        User user = (User)session.getAttribute("user");
        if(user != null) {
            return "redirect:/";
        }

        return "pages/auth/login";
    }
    @PostMapping("/login")
    public String signIn(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            User user = userServices.login(email, password);
            session.setAttribute("user", user);
            session.setAttribute("userRole", user.getRole().toString());

            System.out.println("\n\n\nUser role: " + user.getRole()+"\n\n\n");
            System.out.println("\n\n\nUser username: " + user.getUsername()+"\n\n\n");

            String prevPage = (String) session.getAttribute("prevPage");
            if(prevPage != null) {
                return "redirect:" + prevPage;
            }
            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Email or Password is incorrect");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register(HttpSession session) {

        User user = (User) session.getAttribute("user");
        if(user != null) {
            return "redirect:/";
        }
        return "pages/auth/register";
    }
    @PostMapping("/register")
    public String signUp(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {

        System.out.println("\n\n\nRegistering user with phone FROM postmapping: " + user.getPhone()+"\n\n\n");
        System.out.println("\n\n\nRegistering user with password FROM postmapping: "+user.getPassword()+"\n\n\n");
        System.out.println("\n\n\nRegistering user with username FROM postmapping: "+user.getUsername()+"\n\n\n");

        userServices.register(user);

        redirectAttributes.addFlashAttribute("success", "Registration successful!");
        return "redirect:/register";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
