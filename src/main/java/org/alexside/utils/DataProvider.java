package org.alexside.utils;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.alexside.entity.TItem;
import org.alexside.entity.Tag;
import org.alexside.entity.User;
import org.alexside.service.TItemService;
import org.alexside.service.TagService;
import org.alexside.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Alex on 29.10.2016.
 */
@SpringComponent
@UIScope
public class DataProvider {
    private static Logger log = Logger.getLogger(DataProvider.class.getName());

    @Autowired
    private TItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    private Set<TItem> dataCache;
    private Set<Tag> tagCache;
    private User userCache;

    @PostConstruct
    public void onInit() {
        User sessionUser = AuthUtils.getUser();
        if (sessionUser != null) {
            log.info(String.format("[DataProvider::onInit] login = %s, password = %s",
                    sessionUser.getLogin(), sessionUser.getPassword()));
            userCache = userService.findUser(sessionUser.getLogin(), sessionUser.getPassword());
        }
        dataCache = itemService.findAll(userCache);
        tagCache = tagService.findAll();
    }

    @PreDestroy
    public void onDestroy() {
        userCache = null;
    }

    public User getUserCache() {
        if (userCache == null) {
            User sessionUser = AuthUtils.getUser();
            if (sessionUser != null) {
                userCache = userService.findUser(sessionUser.getLogin(), sessionUser.getPassword());
            }
        }
        return userCache;
    }

    public User getUserCache(String login, String password) {
        if (userCache == null) {
            userCache = userService.findUser(login, password);
        }
        return userCache;
    }

    public void saveUser(User user) {
        if (user == null) return;
        userService.saveUser(user);
        userCache = user;
    }

    public Set<TItem> getTreeData() {
        if (dataCache.isEmpty()) {
            dataCache = itemService.findAll(userCache);
        }
        return dataCache;
    }

    public void saveTItem(TItem ti) {
        if (ti == null || userCache == null) return;
        ti.setUser(userCache);
        itemService.saveTItem(ti);
        dataCache.add(ti);
        tagCache.addAll(ti.getTags());
    }

    public void saveTItems(Collection<TItem> items) {
        if (items == null) return;
        items.forEach(this::saveTItem);
    }

    public void removeTItem(TItem ti) {
        if (ti == null) return;
        itemService.removeTItem(ti);
        dataCache.remove(ti);
    }

    public Set<Tag> getTagCache() {
        if (tagCache.isEmpty()) {
            tagCache = tagService.findAll();
        }
        return tagCache;
    }

    public Set<Tag> getTags() {
        tagCache = tagService.findAll();
        return tagCache;
    }

    public Map<Tag, Long> getRatedTagCache() {
        return dataCache.stream()
                .filter(TItem::isNotice)
                .flatMap(TItem::getTagsAsStream)
                .collect(
                        Collectors.groupingBy(Function.identity(), Collectors.counting()
                ));
    }

    public void saveTag(Tag tag) { tagService.saveTag(tag); }

    public Tag uniqueTag(Tag tag) {
        String s1 = tag.getName().toLowerCase().replaceAll("\\s", "");
        for (Tag t : tagCache) {
            String s2 = t.getName().toLowerCase().replaceAll("\\s", "");
            if (Objects.equals(s1, s2)) return t;
        }
        return tag;
    }

    public Optional<Tag> findTagById(String id) {
        if (id == null) return null;
        return tagCache.stream().filter(tag -> id.equals(tag.getId())).findAny();
    }
}
