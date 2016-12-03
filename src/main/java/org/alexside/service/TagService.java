package org.alexside.service;

import org.alexside.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by abalyshev on 30.11.16.
 */
@Service
public class TagService {

    private static Logger log = Logger.getLogger(TagService.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public Set<Tag> findAll() {
        Set<Tag> result = new HashSet<>();
        try {
            result = mongoTemplate.findAll(Tag.class).stream()
                    .collect(Collectors.toSet());
            return result;
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return result;
    }

    public void saveTags(Collection<Tag> tags) {
        try {
            tags.forEach(tag -> mongoTemplate.save(tag));
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public void saveTag(Tag tag) {
        try {
            mongoTemplate.save(tag);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
