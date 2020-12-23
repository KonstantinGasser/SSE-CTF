package com.sse.upgrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/example/{name}")
    public List<Map<String, Object>> example(@PathVariable("name") String name) {
        return businessLogik.databaseZugriff(name);
    }

    @GetMapping("/{studentArray}")
        public ModelAndView studentNoten(@PathVariable("studentArray") String[] studentArray) {
            ModelAndView mav = new ModelAndView("studentNoten");
            studentArray= new String[]{"Fani", "Obla", "Tim"};
            mav.addObject("studentArray", studentArray);
            return mav;
        }

}
