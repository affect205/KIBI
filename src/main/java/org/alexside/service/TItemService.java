package org.alexside.service;

import org.alexside.entity.TItem;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Alex on 29.10.2016.
 */
@Service
public class TItemService {

    private static Logger log = Logger.getLogger(TItemService.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Datastore datastore;

    public Set<TItem> findAll() {
        Set<TItem> result = new HashSet<>();
        try {
            result = mongoTemplate.findAll(TItem.class).stream().collect(Collectors.toSet());
            for (TItem ti : result) {
                if (ti.isCategory()) {
                    List<TItem> children = result.stream()
                            .filter(i -> i.getParent() != null && i.getParent().getId().equals(ti.getId()))
                            .collect(Collectors.toList());
                    ti.getChildren().addAll(children);
                }
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return result;
    }

    public void saveTItem(TItem tItem) {
        if (tItem == null) return;
        saveTItemRecursive(tItem);
    }

    private void saveTItemRecursive(TItem tItem) {
        if (tItem == null) return;
        try {
            if (tItem.isNotice()) {
                tItem.getTags().forEach(tag -> mongoTemplate.save(tag));
            }
            mongoTemplate.save(tItem);
            if (tItem.hasChildren()) {
                tItem.getChildren().forEach(child -> saveTItemRecursive(child));
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public void removeTItem(TItem tItem) {
        if (tItem == null) return;
        if (tItem.isCategory() && tItem.hasChildren()) {
            tItem.getChildren().forEach(ti -> {
                ti.setParent(tItem.getParent());
                mongoTemplate.save(ti);
            });
        }
        mongoTemplate.remove(tItem);
    }

    private void saveTItemMorphia(TItem tItem) {
        if (tItem == null) return;
        datastore.save(tItem);
        try {
            List<TItem> tItemList = datastore.createQuery(TItem.class).asList();
            if (tItemList != null) {
                tItemList.forEach(i -> {
                    log.info(i.toString());
                });
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
