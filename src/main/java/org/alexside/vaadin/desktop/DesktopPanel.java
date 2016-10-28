package org.alexside.vaadin.desktop;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.alexside.entity.User;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.SpringUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class DesktopPanel extends VerticalLayout {

    private Label content;
    private VerticalLayout layout;

    @PostConstruct
    public void onInit() {
        Button logoutButton = new Button("Выйти");
        logoutButton.addStyleName(BaseTheme.BUTTON_LINK);
        logoutButton.addClickListener(clickEvent -> {
            logout();
        });

        content = new Label("", ContentMode.HTML);

        layout = new VerticalLayout(content, logoutButton);
        layout.setSizeFull();

        addComponents(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void attach() {
        super.attach();
        User user = AuthUtils.getUser();
        String html = String .format("<h2>Рабочий стол...</h2>Пользователь: <b>%s</b>", user.getLogin());
        content.setValue(html);
    }

    private void logout() {
        UI.getCurrent().getPage().reload();
        UI.getCurrent().getSession().close();
    }
}
