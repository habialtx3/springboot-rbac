package com.habialtx3.rbac_sringboot.controller;

import com.habialtx3.rbac_sringboot.model.WebResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/test")
public class DemoController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public WebResponse<String> userAccess() {

        return WebResponse.<String>builder()
                .data("Sukses! Kamu bisa baca ini karena kamu sudah login.")
                .build();

    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public WebResponse<String> adminAccess() {

        return WebResponse.<String>builder()
                .data("Halo Admin! Ini adalah area rahasia.")
                .build();
    }
}