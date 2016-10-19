package org.alexside.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.alexside.utils.VaadinUtils;
import org.alexside.vaadin.desktop.DesktopView;
import org.alexside.vaadin.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by abalyshev on 19.10.16.
 */
@Theme("valo")
@SpringUI(path = "")
public class KibiUI extends UI {

    @Autowired
    private LoginView loginView;

    @Autowired
    private DesktopView desktopView;

    @Override
    protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this, this);
        navigator.addView(VaadinUtils.VIEW_LOGIN, loginView);
        navigator.addView(VaadinUtils.VIEW_DESKTOP, desktopView);
        navigator.navigateTo(VaadinUtils.VIEW_LOGIN);
    }
}
