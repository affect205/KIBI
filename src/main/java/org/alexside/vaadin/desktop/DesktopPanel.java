package org.alexside.vaadin.desktop;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.alexside.utils.SpringUtils;
import org.alexside.utils.VaadinUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class DesktopPanel extends VerticalLayout {

    @PostConstruct
    public void onInit() {
        Button logoutButton = new Button("Выйти");
        logoutButton.addStyleName(BaseTheme.BUTTON_LINK);
        logoutButton.addClickListener(clickEvent -> {
            UI.getCurrent().getNavigator().navigateTo(VaadinUtils.VIEW_LOGIN);
        });

        VerticalLayout layout = new VerticalLayout(new Label("Рабочий стол..."), logoutButton);
        layout.setSizeFull();
        addComponents(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
    }
}
