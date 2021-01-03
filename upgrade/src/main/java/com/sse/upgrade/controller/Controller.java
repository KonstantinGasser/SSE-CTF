package com.sse.upgrade.controller;

import com.sse.upgrade.example.BusinessLogik;
import com.sse.upgrade.model.Note;
import com.sse.upgrade.model.Pruefung;
import com.sse.upgrade.model.User;
import com.sse.upgrade.security.annotation.Pruefungsamt;
import com.sse.upgrade.services.NotenService;
import com.sse.upgrade.services.UserService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ModelAndView mav = new ModelAndView("template.home");
        mav.addObject("username", "Konstantin");
        return mav;
    }

    @GetMapping("/noten")
    public ModelAndView serveNoten() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView mav = new ModelAndView("template.noten");

        // get noten of user
        // TODO: change hard coded ID to authed user id
        List<Note> noten = notenService.getUserNoten("1");

        mav.addObject("username", "Konstantin");
        mav.addObject("noten", noten);

        if (user.getRoles().contains(User.Role.PROFESSOR)) {
            mav.addObject("canAddGrade", true);
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
    @PostMapping("/noten/add")
    public ModelAndView addNewGrade(@RequestParam("kurs") String k, @RequestParam("for_id") String forID, @RequestParam("note") double note) {
        if (k == null || k == "" || forID == null || forID == "" ||  note <= 0.0 || note >5.0) {
            ModelAndView mav = new ModelAndView("global_msg");
            mav.addObject("statusCode", 400);
            mav.addObject("statusMessage", "Fields are not correct");
            return mav;
        }
        System.out.println(" "+k+" "+forID+" "+note);
        return new ModelAndView("redirect:/noten");
    }

    /*
    Entry point to show logged in user actions for notes
    Actions:
        - Anzeigen von Noten
     */

    @GetMapping("/pruefungen")
    public ModelAndView servePruefungen() {
        ModelAndView mav = new ModelAndView("template.pruefungen.html");
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

        if (username == null || hs_id == null || role == null || password == null || username == "" || hs_id == "" || role == "" || password == "") {
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
