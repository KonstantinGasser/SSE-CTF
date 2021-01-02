package com.sse.upgrade.controller;

import com.sse.upgrade.example.BusinessLogik;
import com.sse.upgrade.model.Pruefung;
import com.sse.upgrade.security.annotation.Pruefungsamt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    /*
        Server plain HTML File für hidden Registration-Page
        Access only with Admin login
        Soll nur für uns Devs sein, um Account anlegen zu können.
        Um die Route zu sehen muss man eingeloggt sein.
        returns -> hidden_registration.html PostMapping onclick of submit form to createUser
     */
//    @Pruefungsamt
    @GetMapping("/dev/only/registration")
    public ModelAndView serveHiddenRegistration() {
        ModelAndView mav = new ModelAndView("hidden_registration");
        return mav;
    }

    /*
        Processing request um neuen User anzulegen
        Access only with Admin login
        Soll nur für uns Devs sein, um Account anlegen zu können.
        Um die Route zu sehen muss man eingeloggt sein.
        returns -> hidden_registration.html PostMapping onclick of submit form to createUser
     */
    class user_dev {
        private String username;
        private long hs_id;
        private String role;
    }
//    @Pruefungsamt
    @PostMapping("/devonlyuserscreate")
    public ModelAndView createUser(user_dev user) {
//        ModelAndView mav = new ModelAndView();
        System.out.println(user);
        return null;
    }

}
