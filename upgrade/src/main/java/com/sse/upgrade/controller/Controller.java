package com.sse.upgrade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {

    @GetMapping("/studentNoten")
        public ModelAndView studentNoten() {
            ModelAndView mav = new ModelAndView("studentNoten");
            String[] studentArray= new String[]{"Fani", "Obla", "Tim"};
            mav.addObject("studentArray", studentArray);
            return mav;
        }

}
