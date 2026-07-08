package com.YouTubeTools.controller;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/","home"})
    public String home(){
        return "home";
    }

    //@RequestMapping("/youtube")
    @GetMapping("/video-details")
    public String videoDetails(){
        return "video-details";
    }


}