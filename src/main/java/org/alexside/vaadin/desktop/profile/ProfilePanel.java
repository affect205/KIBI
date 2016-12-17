package org.alexside.vaadin.desktop.profile;

import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.alexside.entity.User;
import org.alexside.service.UserService;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.DataProvider;
import org.alexside.utils.MailUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Alex on 11.12.2016.
 */
@SpringComponent
@UIScope
public class ProfilePanel extends Window{

    @Autowired
    private UserService userService;

    private FormLayout commonWrap;
    private TextField loginField;
    private PasswordField passFieldOne;
    private PasswordField passFieldTwo;
    private CheckBox passCheckbox;
    private TextField emailField;
    private Upload avatarUpload;
    private VerticalLayout avatarWrap;
    private User user;
    private FileUploader fileUploader;;

    private Optional<Consumer<Void>> callback;
    private Button saveButton;

    public ProfilePanel() {
        setWidth("70%");
        setHeight("70%");
        setModal(true);
    }

    @PostConstruct
    public void onInit() {
        user = AuthUtils.getUser(userService);
        callback = Optional.empty();

        fileUploader = new FileUploader();

        avatarUpload = new Upload("Аватарка", fileUploader);
        avatarUpload.setSizeFull();
        avatarUpload.addSucceededListener(fileUploader);
        avatarUpload.setVisible(/*user != null*/true);

        loginField = new TextField("Логин");
        loginField.setSizeFull();
        loginField.setWidth("420px");

        passCheckbox = new CheckBox("Изменить пароль");

        passFieldOne = new PasswordField("Пароль");
        passFieldOne.setSizeFull();
        passFieldOne.setWidth("420px");

        passFieldTwo = new PasswordField("Подтверждение");
        passFieldTwo.setSizeFull();
        passFieldTwo.setWidth("420px");

        emailField = new TextField("Почта");
        emailField.setSizeFull();
        emailField.setWidth("420px");

        avatarWrap = new VerticalLayout(fileUploader.getAvatar());
        avatarWrap.setSizeUndefined();

        commonWrap = new FormLayout(avatarUpload, avatarWrap, loginField,
                passCheckbox, passFieldOne, passFieldTwo, emailField);

        Button mailButton = new Button("Отправить");
        mailButton.addClickListener(event -> {
            MailUtils.sendMail("Kibi - подтверждение регистрации", "Подтверждите регистрацию", "abalyshev@glosav.ru");
        });
        commonWrap.addComponentAsFirst(mailButton);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addTab(commonWrap, user != null ? "Общие" : "Регистрация");

        saveButton = new Button("Сохранить");
        saveButton.addStyleName(ValoTheme.BUTTON_SMALL);
        saveButton.addClickListener(event -> callback.ifPresent(c -> c.accept(null)));

        HorizontalLayout footer = new HorizontalLayout(saveButton);
        footer.setSizeFull();
        footer.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        footer.setExpandRatio(saveButton, 1);

        VerticalLayout wrap = new VerticalLayout(tabSheet, footer);
        wrap.setSizeFull();
        wrap.setMargin(true);
        wrap.setExpandRatio(tabSheet, 1);
        setContent(wrap);
    }

    public void initData() {
        user = AuthUtils.getUser(userService);
        clearAll();
        initForm();
        UI.getCurrent().addWindow(this);
    }

    public void addCallback(Consumer<Void> calback) {
        this.callback = Optional.ofNullable(calback);
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

    private class FileUploader implements Upload.Receiver, Upload.SucceededListener {
        private File file;
        private final Embedded image;

        public FileUploader() {
            image = new Embedded();
            image.setHeight("60px");
            image.setWidthUndefined();
            image.addStyleName("avatar_panel");
        }

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            FileOutputStream fos = null;
            try {
                String path = VaadinServlet.getCurrent().getServletContext().getRealPath("/VAADIN/tmp/uploads/");
                if (!mimeType.contains("image")) throw new Exception("Загруженный файл не является изображением");
                if (user != null && user.getId() != null) {
                    file = new File(path + user.getId() + ".png");
                    fos = new FileOutputStream(file);
                }
            } catch (IOException e) {
                Notification.show("Не удалось сохранить файл<br/>", e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
                return null;
            } catch (Exception e) {
                Notification.show("Произошла ошибка<br/>",  e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
                return null;
            }
            return fos;
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                BufferedImage scaledImage = Scalr.resize(bufferedImage, 200);
                ImageIO.write(scaledImage, "png", file);
            } catch (IOException e) {
                new Notification("Не удается масштабировать изображение<br/>",  e.getMessage(),
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            }
            image.setSource(new FileResource(file));
            avatarWrap.removeAllComponents();
            avatarWrap.addComponent(image);
        }

        public Component getAvatar() {
            Label label = new Label(String.format("%s", FontAwesome.USER.getHtml()), ContentMode.HTML);
            label.addStyleName("avatar_panel");
            label.setHeight("60px");
            label.setWidth("60px");
            return user == null ? label : image;
        }
    }
}
