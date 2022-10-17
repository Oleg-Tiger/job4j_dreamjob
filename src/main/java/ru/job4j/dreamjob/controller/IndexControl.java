package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.utils.UserUtil;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class IndexControl {

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", UserUtil.getUserFromSession(session));
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}