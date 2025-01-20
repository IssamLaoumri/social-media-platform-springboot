package com.laoumri.socialmediaplatformspringboot.controllers;

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

    @GetMapping("/public")
    public String publicContent(){
        return "publicContent";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin")
    public String createPost(@RequestBody String content){
        return "Post created with content : "+content;
    }
}
