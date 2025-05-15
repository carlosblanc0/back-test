package com.anonymousibex.Agents.of.Revature.service;

import com.anonymousibex.Agents.of.Revature.model.Calamity;
import com.anonymousibex.Agents.of.Revature.repository.CalamityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalamityService {

    private final CalamityRepository calamityRepository;

    public List<Calamity> findAllCalamities(){
        return calamityRepository.findAll();
    }
}
