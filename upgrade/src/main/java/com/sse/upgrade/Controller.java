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
            Pruefung pr1 = new Pruefung("SSE", 21836, new GregorianCalendar(2021, 1, 27, 16, 16, 47));
            Pruefung pr2 = new Pruefung("SSE", 218355, new GregorianCalendar(2021, 1, 27, 19, 16, 47));

            angemPruefungen.add(pr1);
            angemPruefungen.add(pr2);
            mav.addObject("angemPruefungen", angemPruefungen);
            return mav;
    }


}
