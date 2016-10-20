package org.alexside.vaadin;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.alexside.security.AuthManager;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.VaadinUtils;
import org.alexside.vaadin.desktop.DesktopView;
import org.alexside.vaadin.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 19.10.16.
 */
@Theme("valo")
@SpringUI(path = "")
//@PreserveOnRefresh
public class KibiUI extends UI {

    private static Logger log = Logger.getLogger(KibiUI.class.getName());

    @Autowired
    private LoginView loginView;

    @Autowired
    private DesktopView desktopView;

    @Autowired
    private AuthManager authManager;

    private Navigator navigator;

    @PostConstruct
    public void onInit() {
        navigator = new Navigator(this, this);
        navigator.addView(VaadinUtils.VIEW_LOGIN, loginView);
        navigator.addView(VaadinUtils.VIEW_DESKTOP, desktopView);
    }

    @Override
    protected void refresh(VaadinRequest request) {
        checkAuth(request);
    }

    @Override
    protected void init(VaadinRequest request) {
        checkAuth(request);
    }

    protected void checkAuth(VaadinRequest request) {
        Principal principal = VaadinSession.getCurrent().getAttribute(Principal.class);
        String token = (String) VaadinSession.getCurrent().getAttribute(AuthUtils.USER_WEB_TOKEN);
        if (principal == null) {
            navigator.navigateTo(VaadinUtils.VIEW_LOGIN);
        } else {
            log.info(String.format("user = %s, token = %s", principal.toString(),
                    VaadinSession.getCurrent().getAttribute(AuthUtils.USER_WEB_TOKEN)));
            navigator.navigateTo(VaadinUtils.VIEW_DESKTOP);
        }
    }
}
