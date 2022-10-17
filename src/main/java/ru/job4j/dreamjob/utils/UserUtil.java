package ru.job4j.dreamjob.utils;

import ru.job4j.dreamjob.model.User;
import javax.servlet.http.HttpSession;

public class UserUtil {

    public static User getUserFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail("Гость");
        }
        return user;
    }
}
