package com.eastwoo.springdockerec2.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : PageController
 * @since : 2024-08-24
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/post-list")
    public String postList() {
        return "signup";
    }
}