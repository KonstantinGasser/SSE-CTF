package com.sse.upgrade.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Profile("dev")
public class DevController {

    /*
    Serve plain HTML File für hidden Registration-Page
    Soll nur für uns Devs sein, um Account anlegen zu können.
    Um die Route zu sehen muss man eingeloggt sein.
    returns -> hidden_registration.html PostMapping onclick of submit form to createUser
    */
    @GetMapping("/admin")
    public ModelAndView serveHiddenRegistration() {
        ModelAndView mav = new ModelAndView("hidden_registration");
        return mav;
    }

}
