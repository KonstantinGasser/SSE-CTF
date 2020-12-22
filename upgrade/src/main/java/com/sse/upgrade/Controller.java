package com.sse.upgrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {
    @Autowired
    BusinessLogik businessLogik;

    @GetMapping("/greeting/{name}")
    public ModelAndView index(@PathVariable("name") String name) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("name", name);
        return mav;
    }

    @GetMapping("/rest")
    public Person rest() {
        return new Person("Franz", 32);
    }
}
