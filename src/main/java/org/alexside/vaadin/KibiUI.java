package org.alexside.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.alexside.security.AuthManager;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.VaadinUtils;
import org.alexside.vaadin.desktop.DesktopView;
import org.alexside.vaadin.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 19.10.16.
 */
@Theme("kibi")
@SpringUI(path = "")
//@Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)
//@Push(value = PushMode.AUTOMATIC)
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
        if (AuthUtils.isLoggedIn()) {
            Authentication auth = AuthUtils.getUser();
            log.info(String.format("[KibiUI::checkAuth]user = %s, token = %s", auth == null ? "unknown" : auth.getPrincipal(),
                    auth == null ? "unknown" : auth.getCredentials()));
            navigator.navigateTo(VaadinUtils.VIEW_DESKTOP);
        } else {
            navigator.navigateTo(VaadinUtils.VIEW_LOGIN);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }
}
