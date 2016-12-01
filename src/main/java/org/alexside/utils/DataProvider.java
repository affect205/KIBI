package org.alexside.utils;

import com.vaadin.spring.annotation.SpringComponent;
import org.alexside.entity.TItem;
import org.alexside.entity.Tag;
import org.alexside.service.TItemService;
import org.alexside.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.alexside.utils.SpringUtils.SCOPE_SINGLETON;

/**
 * Created by Alex on 29.10.2016.
 */
@SpringComponent
@Scope(SCOPE_SINGLETON)
public class DataProvider {

    @Autowired
    private TItemService itemService;

    @Autowired
    private TagService tagService;

    private List<TItem> dataCache;
    private Set<Tag> tagCache;

    @PostConstruct
    public void onInit() {
        dataCache = itemService.findAll();
        tagCache = tagService.findAll();
    }

    public List<TItem> getTreeData() {
        if (dataCache.isEmpty()) {
            dataCache = itemService.findAll();
        }
        return dataCache;
    }

    public void saveTItem(TItem ti) {
        if (ti == null) return;
        itemService.saveTItem(ti);
        dataCache.add(ti);
        tagCache.addAll(ti.getTags());
    }

    public void removeTItem(TItem ti) {
        if (ti == null) return;
        itemService.saveTItem(ti);
        dataCache.remove(ti);
    }

    public Set<Tag> getTagCache() {
        if (tagCache.isEmpty()) {
            tagCache = tagService.findAll();
        }
        return tagCache;
    }

    public void saveTag(Tag tag) { tagService.saveTag(tag); }

    public Tag getUniqueTag(Tag tag) {
        String s1 = tag.getName().toLowerCase().replaceAll("\\s", "");
        for (Tag t : tagCache) {
            String s2 = t.getName().toLowerCase().replaceAll("\\s", "");
            if (Objects.equals(s1, s2)) return t;
        }
        return tag;
    }
}
