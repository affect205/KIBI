package org.alexside.vaadin.desktop.profile;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.alexside.entity.User;

import javax.annotation.PostConstruct;

import static org.alexside.utils.ThemeUtils.PANEL_SCROLLABLE;

/**
 * Created by Alex on 11.12.2016.
 */
@SpringComponent
@UIScope
public class ProfilePanel extends Window{

    private TextField loginField;
    private PasswordField passFieldOne;
    private PasswordField passFieldTwo;
    private CheckBox passCheckbox;
    private TextField emailField;
    private Upload avatarUpload;
    private User user;

    public ProfilePanel() {
        setWidth("70%");
        setHeight("70%");
        setModal(true);
    }

    @PostConstruct
    public void onInit() {
        loginField = new TextField("Логин");
        loginField.setSizeFull();

        passCheckbox = new CheckBox("Изменить пароль");

        passFieldOne = new PasswordField("Пароль");
        passFieldOne.setSizeFull();

        passFieldTwo = new PasswordField("Подтверждение");
        passFieldTwo.setSizeFull();

        emailField = new TextField("Почта");
        emailField.setSizeFull();

        avatarUpload = new Upload();
        avatarUpload.setSizeFull();
        avatarUpload.setCaption("Аватарка");

        FormLayout commonWrap = new FormLayout(loginField, passCheckbox,
                passFieldOne, passFieldTwo, emailField, avatarUpload);
        commonWrap.setSizeFull();

        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(commonWrap, user != null ? "Общие" : "Регистрация");

        VerticalLayout wrap = new VerticalLayout(tabSheet);
        wrap.setSizeFull();
        wrap.setMargin(true);
        wrap.addStyleName(PANEL_SCROLLABLE);
        setContent(wrap);
    }

    public void initData(User user) {
        this.user = user;
        clearAll();
        initForm();
        UI.getCurrent().addWindow(this);
    }

    private void initForm() {
        if (user != null) loginField.setValue(user.getPassword());
        if (user != null) passFieldOne.setValue(user.getLogin());
        if (user != null) passFieldTwo.setValue(user.getPassword());
        if (user != null) emailField.setValue(user.getEmail());
    }

    private void clearAll() {
        loginField.clear();
        passCheckbox.clear();
        passFieldOne.clear();
        passFieldTwo.clear();
        emailField.clear();
    }
}
