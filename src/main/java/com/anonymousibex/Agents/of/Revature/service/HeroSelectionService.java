package com.anonymousibex.Agents.of.Revature.service;

import java.util.List;
import java.util.Optional;

import com.anonymousibex.Agents.of.Revature.model.Calamity;
import com.anonymousibex.Agents.of.Revature.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anonymousibex.Agents.of.Revature.exception.CalamityNotFoundException;
import com.anonymousibex.Agents.of.Revature.exception.NoUserResultsFoundException;
import com.anonymousibex.Agents.of.Revature.model.HeroSelection;
import com.anonymousibex.Agents.of.Revature.repository.CalamityRepository;
import com.anonymousibex.Agents.of.Revature.repository.HeroSelectionRepository;
import com.anonymousibex.Agents.of.Revature.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeroSelectionService {
    private final HeroSelectionRepository selectionsRepository;
    private final UserRepository userRepository;
    private final CalamityRepository calamityRepository;

    public HeroSelection addHeroSelections (HeroSelection selection){

        Optional<User> user =  userRepository.findById(selection.getUser().getId());
        if(user.isPresent()){
            selection.setUser(user.get());

            Optional<Calamity> calamity = calamityRepository.findById(selection.getCalamity().getId());
            if(calamity.isPresent()){
                selection.setCalamity(calamity.get());

                return selectionsRepository.save(selection);
            }
            throw new CalamityNotFoundException("Calamity not found.");
        }
        throw new UsernameNotFoundException("User not found.");
    }

    public List<HeroSelection> getHeroSelectionsByUser(Long id){
         Optional<List<HeroSelection>> heroSelections = selectionsRepository.findByUserId(id);
         if(heroSelections.isPresent() && !heroSelections.get().isEmpty()){
             return heroSelections.get();
         }
         throw new NoUserResultsFoundException("There are not selections for this user");
    }

    public HeroSelection getCalamitySelections(Long id){
        Optional<HeroSelection> Oselections = selectionsRepository.findByCalamityId(id);
        if(Oselections.isPresent() && Oselections.get() != null){
            HeroSelection calamitySelections = Oselections.get();
            return calamitySelections;
        }
        throw new CalamityNotFoundException("Calamity not found.");
    }
}
