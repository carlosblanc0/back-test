package com.anonymousibex.Agents.of.Revature.service;

import java.util.List;
import java.util.Optional;

import com.anonymousibex.Agents.of.Revature.model.Scenario;
import com.anonymousibex.Agents.of.Revature.repository.ScenarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anonymousibex.Agents.of.Revature.exception.*;
import com.anonymousibex.Agents.of.Revature.model.Results;

import com.anonymousibex.Agents.of.Revature.repository.CalamityRepository;
import com.anonymousibex.Agents.of.Revature.repository.ResultsRepository;
import com.anonymousibex.Agents.of.Revature.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultsService {
    private final ResultsRepository resultsRepository;
    private final ScenarioRepository scenarioRepository;

    public List<Results> getAllResults(){
        List<Results> results = resultsRepository.findAll();
        return results;
    }

     public List<Results> getAllUserResults(Long userId){
        Optional<List<Results>> results = resultsRepository.findByUserId(userId);
        if(results.isPresent() && !results.get().isEmpty()){
            List<Results> userResults = results.get();
            return userResults;
        }
        throw new NoUserResultsFoundException("There are not results for this user");
    }

    public Results addResult(Scenario scenario, boolean didWin, int repGained) {
//        Scenario scenario = scenarioRepository.findById(scenarioId)
//                .orElseThrow(() -> new ScenarioNotFoundException(
//                        "Scenario not found: " + scenarioId));

        Results result = new Results();
        result.setScenario(scenario);
        result.setUser(scenario.getUser());
        result.setCalamity(scenario.getCalamity());
        result.setDidWin(didWin);
        result.setRepGained(repGained);

        return resultsRepository.save(result);
    }

    public Results UpdateResult(Results result){
        if(resultsRepository.findById(result.getId()).isPresent()){
            Results resultToUpdate = resultsRepository.findById(result.getId()).get();
            resultToUpdate.setRepGained(result.getRepGained());
            resultToUpdate.setDidWin(result.isDidWin());
            resultsRepository.save(resultToUpdate);
            return resultToUpdate;
        }
         throw new NoUserResultsFoundException("No result found.");
       
    }

}
