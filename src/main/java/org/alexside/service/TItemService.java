package org.alexside.service;

import org.alexside.entity.TItem;
import org.alexside.enums.TreeKind;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

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

    public void saveTItem(TItem tItem) {
        if (tItem == null) return;
        saveTItemRecursive(tItem);
    }

    public List<TItem> findAll() {
        List<TItem> tItemList = new LinkedList<>();
        try {
            tItemList = mongoTemplate.findAll(TItem.class);
            for (TItem ti : tItemList) {
                if (ti.getKind() == TreeKind.CATEGORY) {
                    tItemList.forEach(ti2 -> {
                        if (ti.equals(ti2.getParent()))
                            ti.getChildren().add(ti2);
                    });
                }
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return tItemList;
    }

    private void saveTItemRecursive(TItem tItem) {
        if (tItem == null) return;
        try {
            mongoTemplate.save(tItem);
            if (tItem.hasChildren()) {
                tItem.getChildren().forEach(child -> saveTItemRecursive(child));
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
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
