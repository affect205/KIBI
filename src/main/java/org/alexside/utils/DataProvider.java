package org.alexside.utils;

import com.vaadin.spring.annotation.SpringComponent;
import org.alexside.entity.TItem;
import org.alexside.service.TItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void onInit() {

    }

    public List<TItem> getTreeData() {
        List<TItem> data = itemService.findAll();
        return data;
    }
}
