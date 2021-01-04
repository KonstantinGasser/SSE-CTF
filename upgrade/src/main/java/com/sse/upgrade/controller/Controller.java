package com.sse.upgrade.controller;

import com.sse.upgrade.example.BusinessLogik;
import com.sse.upgrade.model.Note;
import com.sse.upgrade.model.Pruefung;
import com.sse.upgrade.model.User;
import com.sse.upgrade.security.annotation.Professor;
import com.sse.upgrade.security.annotation.Pruefungsamt;
import com.sse.upgrade.security.annotation.Student;
import com.sse.upgrade.services.NotenService;
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
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;
    @Autowired
    NotenService notenService;

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
    add noten nimmt die request form und leitet diese weiter an den NotenService um die Note
    einzutragen. Wenn der user falsche Eingaben macht wird eine error template returned
    TODO: call NotenService (write function in NotenService to process data)
     */
    @Professor
    @PostMapping("/noten/add")
    public ModelAndView addNewGrade(@RequestParam("kurs") String k, @RequestParam("for_id") String forID, @RequestParam("note") double note, @RequestParam("comment") String c) {
        if (k == null || k.equals("") || forID == null || forID.equals("") ||  note <= 0.0 || note >5.0) {
            ModelAndView mav = new ModelAndView("global_msg");
            mav.addObject("statusCode", 400);
            mav.addObject("statusMessage", "Fields are not correct");
            return mav;
        }
        System.out.println(" "+k+" "+forID+" "+note+" "+c);
        return new ModelAndView("redirect:/noten");
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

        List<Map<String, String>> liste = notenService.getPruefungAndAngemelded(user.getId());
        mav.addObject("pruefungen", liste);
        return mav;
    }



    @Professor
    @GetMapping("/pruegunf/prof")
    public ModelAndView servePruefungProf() {
        User user = userService.getLoggedInUser();
        ModelAndView mav = new ModelAndView("template.pruefung.prof");
        mav.addObject("username", user.getUsername());

        List<Map<String, Object>> liste = notenService.getPruefungProf(user.getId());
        mav.addObject("results", liste);
        return mav;
    }

    /*
    Serve plain HTML File für hidden Registration-Page
    Soll nur für uns Devs sein, um Account anlegen zu können.
    Um die Route zu sehen muss man eingeloggt sein.
    returns -> hidden_registration.html PostMapping onclick of submit form to createUser
 */
    @GetMapping("/users/registration")
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

    // FRAGE? von wo wird die SQL-Injection getriggered?
    @GetMapping("/studentNoten")
        public ModelAndView studentNoten() {
            ModelAndView mav = new ModelAndView("studentNoten");
            List<Object> studentArray= new LinkedList<>();
            //noch nicht fertig
            studentArray.add(dbGibNoten(" "));
            mav.addObject("studentArray", studentArray);
            return mav;
        }
    public List<Map<String, Object>> dbGibNoten(String name) {
        // VORSICHT! hier ist eine SQL Injection möglich
        String sql = "select us.username as \"Name\",te.note as \"Note\",pr.kurs as \"Fach\" from(( teilnehmer te JOIN  pruefung pr ON pr.id = te.pruefung_id)JOIN hs_user us ON us.id = te.user_id )where us.username= 'Niklas'";
        System.out.println(sql);
        //System.out.println(jdbcTemplate.queryForList(sql));
        return jdbcTemplate.queryForList(sql);
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
