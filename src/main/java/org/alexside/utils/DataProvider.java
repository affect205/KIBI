package org.alexside.utils;

import com.vaadin.spring.annotation.SpringComponent;
import org.alexside.entity.TItem;
import org.alexside.service.TItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

import static org.alexside.utils.SpringUtils.SCOPE_SINGLETON;

/**
 * Created by Alex on 29.10.2016.
 */
@SpringComponent
@Scope(SCOPE_SINGLETON)
public class DataProvider {

    @Autowired
    private TItemService itemService;

    private List<TItem> dataCache;

    @PostConstruct
    public void onInit() {
        dataCache = new LinkedList<>();
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
    }

    public void removeTItem(TItem ti) {
        if (ti == null) return;
        itemService.saveTItem(ti);
        dataCache.remove(ti);
    }
}
