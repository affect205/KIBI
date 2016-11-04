package org.alexside.vaadin.desktop;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.alexside.entity.User;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.SpringUtils;
import org.alexside.vaadin.desktop.notice.ViewPanel;
import org.alexside.vaadin.misc.KibiTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class DesktopPanel extends VerticalLayout {

    @Autowired
    private KibiTree kibiTree;

    @Autowired
    private ViewPanel viewPanel;

    private Label content;

    @PostConstruct
    public void onInit() {
        setSizeFull();
        Button logoutButton = new Button("Выйти");
        logoutButton.addStyleName(BaseTheme.BUTTON_LINK);
        logoutButton.addClickListener(clickEvent -> logout());
        logoutButton.setWidth("80px");

        content = new Label("", ContentMode.HTML);
        content.setWidth("200px");

        HorizontalLayout hLayout1 = new HorizontalLayout(content, logoutButton);
        hLayout1.setSizeFull();
        hLayout1.setSpacing(true);
        hLayout1.setComponentAlignment(content, Alignment.MIDDLE_RIGHT);
        hLayout1.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
        hLayout1.setExpandRatio(content, 1);

        HorizontalLayout hLayout2 = new HorizontalLayout(kibiTree, viewPanel);
        hLayout2.setSizeFull();
        hLayout2.setMargin(true);
        hLayout2.setSpacing(true);
        hLayout2.setExpandRatio(kibiTree, 1);
        hLayout2.setExpandRatio(viewPanel, 4);

        addComponent(hLayout2);
        addComponent(hLayout1);

        setExpandRatio(hLayout1, 1);
        setExpandRatio(hLayout2, 7);
    }

    @Override
    public void attach() {
        super.attach();
        User user = AuthUtils.getUser();
        content.setValue(String.format("Пользователь: <b>%s</b>", user.getLogin()));
    }

    private void logout() {
        UI.getCurrent().getPage().reload();
        UI.getCurrent().getSession().close();
    }
}
