package com.anonymousibex.Agents.of.Revature.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anonymousibex.Agents.of.Revature.model.HeroSelection;


public interface HeroSelectionRepository extends JpaRepository<HeroSelection, Long>{
    Optional<HeroSelection> findByCalamityId(long id);
    Optional<List<HeroSelection>> findByUserId(long id);
}
