package com.sse.upgrade.example;

import com.sse.upgrade.security.annotation.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
public class ExampleController {
    @Autowired
    BusinessLogik businessLogik;

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
}
