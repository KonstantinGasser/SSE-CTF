package com.sse.upgrade.example;

import com.sse.upgrade.security.annotation.Professor;
import com.sse.upgrade.security.annotation.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
public class ExampleController {
    @Autowired
    BusinessLogik businessLogik;

    @Professor
    @GetMapping("/greeting/{name}")
    public ModelAndView index(@PathVariable("name") String name) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("name", name);
        return mav;
    }

    /**
     * localhost:8080/example/SSE
     */
    @Student
    @GetMapping("/example/{name}")
    public List<Map<String, Object>> example(@PathVariable("name") String name) {
        return businessLogik.databaseZugriff(name);
    }

    /**
     * localhost:8080/example2?name=SSE
     */
    @GetMapping("/example2")
    public List<Map<String, Object>> example2(@RequestParam("name") String name) {
        return businessLogik.databaseZugriff(name);
    }
}
