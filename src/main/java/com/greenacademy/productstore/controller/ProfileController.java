package com.greenacademy.productstore.controller;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ProfileController {
    private UserServices userServices;

    public ProfileController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "pages/profile/index";
    }

    @GetMapping("/profile/edit")
    public String edit(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Integer id = user.getId();
        model.addAttribute("user", userServices.getById(id));
        return "pages/profile/edit";
    }

    @PostMapping("/profile/edit")
    public String update(@Valid @ModelAttribute("user") User user, HttpSession session,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            return "pages/profile/edit";
        }
        User userSession = (User) session.getAttribute("user");
        user.setId(userSession.getId());
        userServices.update(user);

        User userUpdated = userServices.getById(user.getId());
        session.setAttribute("user", userUpdated);
        return "redirect:/profile";
    }

    @GetMapping("/profile/password")
    public String changePassword(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "pages/profile/password";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            userServices.changePassword(user, currentPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully");
            return "redirect:/profile/password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/password";
        }
    }

}
