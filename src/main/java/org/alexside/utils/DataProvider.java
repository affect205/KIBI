package org.alexside.utils;

import com.vaadin.spring.annotation.SpringComponent;
import org.alexside.entity.Category;
import org.alexside.entity.Notice;
import org.alexside.entity.TItem;
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

    @PostConstruct
    public void onInit() {

    }

    public List<TItem> getAllData() {
        List<TItem> data = new LinkedList<>();
        Category c1 = new Category(1, "Программирование");
        c1.addChild(new Notice(1, "Основы программирования", "Основы программирования...", c1));

        Category c2 = new Category(2, "Java", c1);
        c2.addChild(new Notice(2, "Основы Java", "Основы Java...", c2));

        Category c3 = new Category(3, "C++", c1);
        c3.addChild(new Notice(3, "Основы C++", "Основы C++...", c3));

        c1.addChild(c2);
        c1.addChild(c3);

        Category c5 = new Category(5, "Смартфоны");
        c5.addChild(new Notice(4, "Sony Xperia XA Ultra", "Sony Xperia XA Ultra обзор", c5));
        c5.addChild(new Notice(5, "Sony Xperia X ", "Sony Xperia X обзор", c5));

        data.add(c1);
        data.add(c5);

        return data;
    }
}
