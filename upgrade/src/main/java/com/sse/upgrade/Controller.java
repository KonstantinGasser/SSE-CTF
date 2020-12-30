package com.sse.upgrade;

import com.sse.upgrade.example.BusinessLogik;
import com.sse.upgrade.model.Pruefung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class Controller {
    @Autowired
    BusinessLogik businessLogik;

    @GetMapping("/studentNoten")
        public ModelAndView studentNoten() {
            ModelAndView mav = new ModelAndView("studentNoten");
            List<Object> studentArray= new LinkedList<>();
            studentArray.add(businessLogik.databaseZugriff("SSE"));
            mav.addObject("studentArray", studentArray);
            return mav;
        }


    @GetMapping("/studentAngemPruefungen")
        public ModelAndView studentAngemPruefungen() {
            ModelAndView mav = new ModelAndView("studentPruefungen");
            ArrayList<Pruefung> angemPruefungen = new ArrayList<>();

            Pruefung pr1 = new Pruefung("SSE", 21836, new Timestamp(1490161712000L));
            Pruefung pr2 = new Pruefung("SSE", 218355, new Timestamp(44444444000L));

            angemPruefungen.add(pr1);
            angemPruefungen.add(pr2);
            mav.addObject("angemPruefungen", angemPruefungen);
            return mav;
    }


}
