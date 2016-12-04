package org.alexside.vaadin;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.alexside.entity.User;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.EventUtils;
import org.alexside.utils.VaadinUtils;
import org.alexside.vaadin.desktop.DesktopView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 19.10.16.
 */
@Theme("kibi")
@SpringUI
@Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)
//@PreserveOnRefresh
public class KibiUI extends UI {

    private static Logger log = Logger.getLogger(KibiUI.class.getName());

    @Autowired
    private SpringViewProvider viewProvider;

    private Navigator navigator;

    @PostConstruct
    public void onInit() {
        EventUtils.initEventBusInstance();

        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        final Panel container = new Panel();
        container.setSizeFull();
        root.addComponent(container);
        root.setExpandRatio(container, 1.0f);

        navigator = new Navigator(this, container);
        navigator.addProvider(viewProvider);
        //navigator.addView("", DesktopView.class);
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
            User user = AuthUtils.getUser();
            log.info(String.format("[KibiUI::checkAuth]login = %s, password = %s", user == null ? "unknown" : user.getLogin(),
                    user == null ? "unknown" : user.getPassword()));
            navigator.navigateTo(viewProvider.getViewName(VaadinUtils.VIEW_DESKTOP));
        } else {
            navigator.navigateTo(viewProvider.getViewName(VaadinUtils.VIEW_LOGIN));
        }
    }

    @Override
    public void detach() {
        super.detach();
    }
}
