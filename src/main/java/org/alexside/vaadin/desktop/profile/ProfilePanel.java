package org.alexside.vaadin.desktop.profile;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.alexside.entity.User;
import org.alexside.utils.MailUtils;
import org.imgscalr.Scalr;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    private FileUploader fileUploader;

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

        fileUploader = new FileUploader();

        avatarUpload = new Upload("Аватарка", fileUploader);
        avatarUpload.setSizeFull();
        avatarUpload.addSucceededListener(fileUploader);

        FormLayout commonWrap = new FormLayout(loginField, passCheckbox,
                passFieldOne, passFieldTwo, emailField, avatarUpload, fileUploader.getImage());

        Button mailButton = new Button("Отправить");
        mailButton.addClickListener(event -> {
            MailUtils.sendMail("Kibi - подтверждение регистрации", "Подтверждите регистрацию", "abalyshev@glosav.ru");
        });
        commonWrap.addComponentAsFirst(mailButton);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addTab(commonWrap, user != null ? "Общие" : "Регистрация");

        VerticalLayout wrap = new VerticalLayout(tabSheet);
        wrap.setSizeFull();
        wrap.setMargin(true);
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

    public static class FileUploader implements Upload.Receiver, Upload.SucceededListener {
        private File file;
        private final Embedded image;

        public FileUploader() {
            image = new Embedded("Uploaded Image");
            image.setHeight("80px");
            image.setWidthUndefined();
            image.setVisible(false);
            image.addStyleName("avatar_panel");
        }

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            FileOutputStream fos;
            try {
                String path = VaadinServlet.getCurrent().getServletContext().getRealPath("/VAADIN/tmp/uploads/");
                file = new File(path + filename);
                fos = new FileOutputStream(file);
            } catch (IOException e) {
                new Notification("Could not open file<br/>",  e.getMessage(),
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                return null;
            }
            return fos; // Return the output stream to write to
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                BufferedImage scaledImage = Scalr.resize(bufferedImage, 200);
                ImageIO.write(scaledImage, "png", file);
            } catch (IOException e) {
                new Notification("Could not resize file<br/>",  e.getMessage(),
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            }
            image.setSource(new FileResource(file));
            image.setVisible(true);
        }

        public Embedded getImage() { return image; }
    }
}
