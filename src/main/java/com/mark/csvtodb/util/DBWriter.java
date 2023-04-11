package com.mark.csvtodb.util;

import com.mark.csvtodb.model.Profile;
import com.mark.csvtodb.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBWriter implements ItemWriter<Profile> {
    private final ProfileRepository profileRepository;

    @Override
    public void write(Chunk<? extends Profile> chunk) throws Exception {
        profileRepository.saveAll(chunk);
    }
}
