package org.alexside.vaadin.misc;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Panel;
import org.alexside.utils.SpringUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class HeaderPanel extends Panel {

    public HeaderPanel() {
        super("<b>KIBI</b> - система управления базой знаний");
        setHeight("100%");
        setWidth("100%");
    }

    @PostConstruct
    public void onInit() {


    }
}
