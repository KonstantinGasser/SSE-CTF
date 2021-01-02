package com.sse.upgrade.example;

import com.sse.upgrade.model.User;
import com.sse.upgrade.security.annotation.Professor;
import com.sse.upgrade.security.annotation.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
public class ExampleController {
    @Autowired
    BusinessLogik businessLogik;

    @Professor
    @GetMapping("/greeting")
    public ModelAndView index(Principal principal) {
        User user = (User) principal;
        // get current User
        User auch_user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("name", user.getUsername());
        return mav;
    }

    /**
     * localhost:8080/example/SSE
     */
    @Student
    @GetMapping("/example/{name}")
    @ResponseBody
    public List<Map<String, Object>> example(@PathVariable("name") String name) {
        return businessLogik.databaseZugriff(name);
    }

    /**
     * localhost:8080/example2?name=SSE
     */
    @GetMapping("/example2")
    @ResponseBody
    public List<Map<String, Object>> example2(@RequestParam("name") String name) {
        return businessLogik.databaseZugriff(name);
    }

    @PostMapping("/example3")
    public String example3(Model model) {
        model.addAttribute("name", "Peter");
        // verarneite upload
        return "redirect:/index";
    }
}
