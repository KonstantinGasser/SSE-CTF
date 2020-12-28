package com.sse.upgrade;

import model.Pruefung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        model.Pruefung pr1 = new Pruefung("SSE", 21836, new Date(15012020));
        angemPruefungen.add(pr1);
        mav.addObject("angemPruefungen", angemPruefungen);
        return mav;
    }



}
