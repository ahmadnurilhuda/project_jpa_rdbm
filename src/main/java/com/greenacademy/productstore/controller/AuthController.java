package com.greenacademy.productstore.controller;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String login(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "pages/auth/login";
    }

    @PostMapping("/login")
    public String signIn(@RequestParam("email") String email, @RequestParam("password") String password,
            HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            User user = userServices.login(email, password);

            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("userRole", user.getRole().toString());

                System.out.println("\n\n\nUser role: " + user.getRole() + "\n\n\n");
                System.out.println("\n\n\nUser username: " + user.getUsername() + "\n\n\n");

                String prevPage = (String) session.getAttribute("prevPage");
                String prevMethod = (String) session.getAttribute("prevMethod");
                if (prevPage != null && prevMethod != null && prevMethod.equals("GET")) {
                    return "redirect:" + prevPage;
                }
            }

            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register(HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }
        return "pages/auth/register";
    }

    @PostMapping("/register")
    public String signUp(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {

        System.out.println("\n\n\nRegistering user with phone FROM postmapping: " + user.getPhone() + "\n\n\n");
        System.out.println("\n\n\nRegistering user with password FROM postmapping: " + user.getPassword() + "\n\n\n");
        System.out.println("\n\n\nRegistering user with username FROM postmapping: " + user.getUsername() + "\n\n\n");

        userServices.register(user);

        redirectAttributes.addFlashAttribute("success",
                "Registration successful! Please check Your Email to Verification");
        return "redirect:/register";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/verify/{token}")
    public String verify(@PathVariable("token") String token, RedirectAttributes redirectAttributes) {

        try {
            userServices.verifyAccount(token);
            redirectAttributes.addFlashAttribute("success", "Account verified successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "pages/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            userServices.forgotPassword(email);
            redirectAttributes.addFlashAttribute("success", "Link to reset password has been sent to your email");
            return "redirect:/forgot-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable("token") String token, Model model, RedirectAttributes redirectAttributes) {

        try {
            userServices.verificationResetPassword(token);
            model.addAttribute("token", token);
            redirectAttributes.addFlashAttribute("success", "Verification token has been reset successfully!");
            return "pages/auth/reset-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "pages/auth/reset-password";
        }
    }

    @PostMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable("token") String token, @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
        try {
            userServices.resetPassword(token, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Password has been reset successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reset-password/" + token;
        }
    }
}
