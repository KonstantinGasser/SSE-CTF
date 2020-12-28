package com.sse.upgrade;

import com.sse.upgrade.model.Pruefung;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
public class Controller {

    @GetMapping("/studentNoten")
        public ModelAndView studentNoten() {
            ModelAndView mav = new ModelAndView("studentNoten");
            String[] studentArray= new String[]{"Fani", "Obla", "Tim"};
            mav.addObject("studentArray", studentArray);
            return mav;
        }


    @GetMapping("/studentAngemPruefungen")
        public ModelAndView studentAngemPruefungen() {
            ModelAndView mav = new ModelAndView("studentPruefungen");
            ArrayList<Pruefung> angemPruefungen = new ArrayList<>();
            com.sse.upgrade.model.Pruefung pr1 = new Pruefung("SSE", 21836, new Date());
            angemPruefungen.add(pr1);
            mav.addObject("angemPruefungen", angemPruefungen);
            return mav;
    }


}
