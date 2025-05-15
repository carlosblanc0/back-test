package com.anonymousibex.Agents.of.Revature.controller;

import com.anonymousibex.Agents.of.Revature.model.Calamity;
import com.anonymousibex.Agents.of.Revature.service.CalamityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calamity")
public class CalamityController {

    private final CalamityService calamityService;

    @GetMapping
    public ResponseEntity<List<Calamity>> getAllCalamities(HttpServletRequest request){
        return ResponseEntity.status(200).body(calamityService.findAllCalamities());

    }

}
