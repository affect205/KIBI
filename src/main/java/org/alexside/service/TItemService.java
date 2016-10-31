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
            mongoTemplate.insert(tItem);
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

    public TItem find(final String id) {
        final TItem tItem = mongoTemplate.findOne(
                Query.query(new Criteria("id").is(id)), TItem.class
        );
        if (tItem == null) return tItem;
        return build(
                tItem,
                mongoTemplate.find(
                        Query.query(new Criteria("path").regex("^" + tItem.getPath() + "[.]")),
                        TItem.class
                )
        );
    }

    private TItem build(final TItem root, final Collection<TItem> documents) {
        final Map<String, TItem> map = new HashMap<>();
        for (final TItem document : documents) {
            map.put(document.getPath(), document);
        }
        for (final TItem document : documents) {
            map.put(document.getPath(), document);
            final String path = document
                    .getPath()
                    .substring(0, document.getPath().lastIndexOf(TItem.PATH_SEPARATOR));
            if (path.equals(root.getPath())) {
                root.getChildren().add(document);
            } else {
                final TItem parent = map.get(path);
                if (parent != null) {
                    parent.getChildren().add(document);
                }
            }
        }
        return root;
    }

}
