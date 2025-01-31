package com.laoumri.socialmediaplatformspringboot.controllers;

import com.laoumri.socialmediaplatformspringboot.dto.requests.PostContent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 20/01/2025
 * Version: 0.0.1
 */
@RestController
@RequestMapping("/api/v1/resources")
public class TestController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin")
    public String createPost(@RequestBody PostContent content){
        return "Post created with content : "+content;
    }

    @GetMapping("/public")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
