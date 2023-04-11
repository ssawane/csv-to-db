package com.mark.csvtodb.util;

import com.mark.csvtodb.model.Profile;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProfileItemProcessor implements ItemProcessor<Profile, Profile> {
    @Override
    public Profile process(Profile item) throws Exception {
        return item;
    }
}
