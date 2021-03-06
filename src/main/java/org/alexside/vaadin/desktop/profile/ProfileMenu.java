package org.alexside.vaadin.desktop.profile;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.alexside.entity.User;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static org.alexside.utils.ThemeUtils.MENU_PROFILE;

/**
 * Created by Alex on 27.11.2016.
 */
@SpringComponent
@ViewScope
public class ProfileMenu extends HorizontalLayout {

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private ProfilePanel profilePanel;

    @PostConstruct
    public void onInit() {
        setSizeFull();
        setSpacing(true);
        addStyleName(MENU_PROFILE);

        MenuBar menuBar = new MenuBar();
        menuBar.setHtmlContentAllowed(true);
        menuBar.setCaptionAsHtml(true);

        Optional<User> opt = Optional.ofNullable(AuthUtils.getUser());

        String text = String.format("<b>%s</b>", opt.isPresent() ?
                opt.get().getLogin() : "??");

        MenuBar.MenuItem rootItem = menuBar.addItem(text, FontAwesome.USER, null);
        rootItem.addItem("Профиль", (MenuBar.Command) selectedItem -> {
            profilePanel.initData();
            profilePanel.addSaveCallback(user -> {
                dataProvider.saveUser(user);
                Notification.show("Профиль сохранен");
            });
        });
        rootItem.addItem("Выйти", (MenuBar.Command) selectedItem -> {
            UI.getCurrent().getPage().reload();
            UI.getCurrent().getSession().close();
        });

        addComponents(menuBar);
        setComponentAlignment(menuBar, Alignment.TOP_RIGHT);
        setVisible(AuthUtils.isLoggedIn());
    }
}
