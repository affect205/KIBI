package org.alexside.vaadin.desktop;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.alexside.entity.User;
import org.alexside.utils.AuthUtils;
import org.alexside.vaadin.desktop.display.DisplayPanel;
import org.alexside.vaadin.desktop.qa.CategoryQAPanel;
import org.alexside.vaadin.desktop.qa.NoticeQAPanel;
import org.alexside.vaadin.misc.KibiTree;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@ViewScope
public class DesktopPanel extends VerticalLayout {

    @Autowired
    private KibiTree kibiTree;

    @Autowired
    private DisplayPanel viewPanel;

    @Autowired
    private CategoryQAPanel categoryQAPanel;

    @Autowired
    private NoticeQAPanel noticeQAPanel;

    private Label content;

    @PostConstruct
    public void onInit() {
        setSizeFull();
        setMargin(true);

        Button logoutButton = new Button("Выйти");
        logoutButton.addStyleName(BaseTheme.BUTTON_LINK);
        logoutButton.addClickListener(clickEvent -> logout());
        logoutButton.setWidth("80px");

        content = new Label("", ContentMode.HTML);
        content.setWidth("200px");
        User user = AuthUtils.getUser();
        if (user != null) {
            content.setValue(String.format("%s: <b>%s</b>",
                    FontAwesome.USER.getHtml(), user.getLogin()));
        }

        HorizontalLayout footerLayout = new HorizontalLayout(content, logoutButton);
        footerLayout.setSizeFull();
        footerLayout.setSpacing(true);
        footerLayout.setComponentAlignment(content, Alignment.MIDDLE_RIGHT);
        footerLayout.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
        footerLayout.setExpandRatio(content, 1);

        HorizontalLayout qaLayout = new HorizontalLayout(categoryQAPanel, noticeQAPanel);
        qaLayout.setSizeFull();
        qaLayout.setSpacing(true);
        qaLayout.setMargin(new MarginInfo(true, false, false, false));

        VerticalLayout viewLayout = new VerticalLayout(viewPanel, qaLayout);
        viewLayout.setSizeFull();
        viewLayout.setExpandRatio(viewPanel, 7);
        viewLayout.setExpandRatio(qaLayout, 2);

        HorizontalLayout contentLayout = new HorizontalLayout(kibiTree, viewLayout);
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);
        contentLayout.setExpandRatio(kibiTree, 1);
        contentLayout.setExpandRatio(viewLayout, 4);

        addComponent(contentLayout);
        addComponent(footerLayout);

        setExpandRatio(contentLayout, 12);
        setExpandRatio(footerLayout, 1);
    }

    private void logout() {
        UI.getCurrent().getPage().reload();
        UI.getCurrent().getSession().close();
    }
}
