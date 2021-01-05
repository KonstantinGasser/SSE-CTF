package com.sse.upgrade.controller;

import com.sse.upgrade.example.BusinessLogik;
import com.sse.upgrade.model.Note;
import com.sse.upgrade.model.Pruefung;
import com.sse.upgrade.model.User;
import com.sse.upgrade.security.CookieSecurityContextRepository;
import com.sse.upgrade.security.annotation.Professor;
import com.sse.upgrade.security.annotation.Pruefungsamt;
import com.sse.upgrade.security.annotation.Student;
import com.sse.upgrade.services.NotenService;
import com.sse.upgrade.services.PruefungService;
import com.sse.upgrade.services.UserService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class Controller {

    @Autowired
    UserService userService;
    @Autowired
    NotenService notenService;
    @Autowired
    PruefungService pruefungService;

    @Autowired
    CookieSecurityContextRepository cookieSecurityContextRepository;

    /*
    Index file:
        - redirect to after login
        - navigation to different function of the application
     */
    @GetMapping("/")
    public ModelAndView serveIndex() {
        User user = userService.getLoggedInUser();

        ModelAndView mav = new ModelAndView("template.home");
        mav.addObject("permission", user.getRoles().contains(User.Role.PROFESSOR) ? "prof": "student");
        mav.addObject("username", user.getUsername());
        return mav;
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mav = new ModelAndView("redirect:/login");
        this.cookieSecurityContextRepository.endSession(
                (String) SecurityContextHolder.getContext().getAuthentication().getCredentials()
        );
        return mav;
    }
    @Student
    @GetMapping("/noten")
    public ModelAndView serveNoten() {
        User user = userService.getLoggedInUser();
        ModelAndView mav = new ModelAndView("template.noten");

        mav.addObject("permission", user.getRoles().contains(User.Role.PROFESSOR) ? "prof": "student");
        // get noten of user
        // TODO: change hard coded ID to authed user id
        List<Note> noten = notenService.getUserNoten(String.valueOf(user.getId()));

        // set noten for given user
        mav.addObject("username", user.getUsername());
        mav.addObject("noten", noten);

        for (Note n : noten) {
            n.setComment("<input class=\"form-control\" name=\"comment\" value=\""+ n.getComment() +"\">");
        }
        // if user is prof set allowed kurs for adding
        if (user.getRoles().contains(User.Role.PROFESSOR)) {
            List<String> kurse = notenService.getNotenByProf("2");

            mav.addObject("canAddGrade", true);
            mav.addObject("allowedKurse", kurse);

        } else {
            mav.addObject("canAddGrade", false);
        }

        return mav;
    }


    /*
    Entry point to show logged in user actions for notes
    Actions:
        - Anzeigen von Noten
     */
    @Student
    @GetMapping("/pruefungen")
    public ModelAndView servePruefungen() {
        ModelAndView mav = new ModelAndView("template.pruefungen");

        User user = userService.getLoggedInUser();
        mav.addObject("username", user.getUsername());
        List<Map<String, Object>> liste = notenService.getPruefungAndAngemelded(user.getId());
//        System.out.println(liste);
        mav.addObject("pruefungen", liste);

        return mav;
    }

    @GetMapping("/pruefung/search")
    public ModelAndView serveSearchResults(@RequestParam("query") String query) {
        ModelAndView mav = new ModelAndView("template.pruefungen");

        User user = userService.getLoggedInUser();
        mav.addObject("username", user.getUsername());
        List<Map<String, Object>> liste = new ArrayList<>();
        try {
            liste = notenService.getQuery(query, user.getId());
        } catch(Exception e) {
            mav.addObject("statusCode", 500);
            mav.addObject("statusMessage", e);
            return mav;
        }
        System.out.println(liste);
        mav.addObject("pruefungen", liste);

        return mav;
    }


    @Professor
    @GetMapping("/pruefungen/prof")
    public ModelAndView servePruefungProf() {
        User user = userService.getLoggedInUser();
        ModelAndView mav = new ModelAndView("template.pruefung.prof");
        mav.addObject("username", user.getUsername());

        List<Map<String, Object>> liste = notenService.getPruefungProf(user.getId());
        mav.addObject("results", liste);
        return mav;
    }

    @Professor
    @GetMapping("/pruefung/show/{id}")
    public ModelAndView serveSinglePruefung(@PathVariable("id") String id) {

        User user = userService.getLoggedInUser();
        ModelAndView mav = new ModelAndView("template.pruefung.xy");
        mav.addObject("_for", id);
        List<Map<String, Object>> liste = notenService.getPruefunAndStuden(id);

        for (Map<String, Object> m : liste) {
            if(m.get("c") != null)
                m.put("c", "<input class=\"form-control\" name=\"comment\" value=\""+ m.get("c") +"\">");
        }
        mav.addObject("results", liste);
        mav.addObject("username", user.getUsername());

        return mav;
    }

    @Professor
    @PostMapping("/noten/add")
    public ModelAndView addNote(@RequestParam("uuid")String uuid, @RequestParam("kurs") String kurs, @RequestParam("note") double note, @RequestParam("comment") String comment) {
        User user =  userService.getLoggedInUser();
        String redirect = "redirect:/pruefung/show/"+kurs;
        ModelAndView mav = new ModelAndView(redirect);
        System.out.println(comment);
        boolean success = notenService.updateNote(uuid, comment, note, kurs);

        return mav;

    }

    /*
    Serve plain HTML File für hidden Registration-Page
    Soll nur für uns Devs sein, um Account anlegen zu können.
    Um die Route zu sehen muss man eingeloggt sein.
    returns -> hidden_registration.html PostMapping onclick of submit form to createUser
 */
    @GetMapping("/admin/registration")
    public ModelAndView serveHiddenRegistration() {
        ModelAndView mav = new ModelAndView("hidden_registration");
        return mav;
    }

    /*
        Processing request um neuen User anzulegen
        Soll nur für uns Devs sein, um Account anlegen zu können.
        Um die Route zu sehen muss man eingeloggt sein.
        returns -> hidden_registration.html PostMapping onclick of submit form to createUser
     */
    @PostMapping("/users/create")
    public ModelAndView createUser(@RequestParam("username") String username, @RequestParam("hs_id") String hs_id,@RequestParam("role") String role, @RequestParam("password") String password) {
        ModelAndView mv = new ModelAndView("global_msg");

        if (username == null || hs_id == null || role == null || password == null || username.equals("") || hs_id.equals("") || role.equals("") || password.equals("")) {
            mv.addObject("statusCode", 400);
            mv.addObject("statusMessage", "400 - This request stinks bad dude!");
            return mv;
        }

        if (!userService.register(username, hs_id, role, password)) {
            mv.addObject("statusCode", 500);
            mv.addObject("statusMessage", "500 - Yes the backend team messed up ~ sry");
            return mv;
        }

        mv.addObject("statusCode", 200);
        mv.addObject("statusMessage", "200 - Welcome to the dark side " + username);
        return mv;
    }

    @GetMapping("/users/all")
    public ModelAndView getAllUser() {
        ModelAndView mv = new ModelAndView("list.user");
        return mv.addObject("users", userService.getAll());
    }



    @GetMapping("/studentAnmelden/{PRid}")
        public ModelAndView studentAnmelden(@PathVariable("PRid") int pruefungsID) {
            ModelAndView mav = new ModelAndView("redirect:/pruefungen");
            User user= userService.getLoggedInUser();
            pruefungService.anmelden(pruefungsID,user.getId());

            System.out.println("Student mit ID " + user.getId() + " zu Pruefung " + pruefungsID + " angemeldet");

            return mav;
    }

    @GetMapping("/studentAbmelden/{PRid}")
    public ModelAndView studentAbmelden(@PathVariable("PRid") int pruefungsID) {
        ModelAndView mav = new ModelAndView("redirect:/pruefungen");
        User user= userService.getLoggedInUser();
        pruefungService.abmelden(pruefungsID,user.getId());
        System.out.println("Student mit ID " + user.getId() + " von Pruefung" + pruefungsID + " abgemeldet");

        return mav;
    }



}
