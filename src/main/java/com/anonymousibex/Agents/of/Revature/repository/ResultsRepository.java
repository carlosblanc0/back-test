package com.anonymousibex.Agents.of.Revature.repository;

import com.anonymousibex.Agents.of.Revature.model.Results;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ResultsRepository extends JpaRepository<Results, Long>{
    Optional<Results> findByCalamityId(long id);
    Optional<List<Results>>findByUserId(long id);

}
