package com.mark.csvtodb.service;

import com.mark.csvtodb.model.Profile;
import com.mark.csvtodb.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Optional<Profile> findByEmail(String email) {
        return profileRepository.findByEmail(email);
    }
}
