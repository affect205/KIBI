package org.alexside.vaadin.desktop.profile;

import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.alexside.entity.User;
import org.alexside.utils.DataProvider;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Alex on 11.12.2016.
 */
@SpringComponent
@ViewScope
public class ProfilePanel extends Window{

    @Autowired
    private DataProvider dataProvider;

    private TabSheet tabSheet;
    private TabSheet.Tab commonTab;
    private FormLayout commonWrap;
    private TextField loginField;
    private PasswordField passFieldOne;
    private PasswordField passFieldTwo;
    private CheckBox passCheckbox;
    private TextField emailField;
    private Upload avatarUpload;
    private VerticalLayout avatarIcon;
    private User user;
    private FileUploader fileUploader;

    private Optional<Consumer<User>> saveCallback;
    private Button saveButton;

    public ProfilePanel() {
        setWidth("60%");
        setHeight("60%");
        setModal(true);
    }

    @PostConstruct
    public void onInit() {
        user = dataProvider.getUserCache();
        saveCallback = Optional.empty();

        fileUploader = new FileUploader();

        avatarUpload = new Upload(null, fileUploader);
        avatarUpload.setSizeFull();
        avatarUpload.setButtonCaption("Загрузить");
        avatarUpload.addSucceededListener(fileUploader);
        avatarUpload.addStartedListener(fileUploader);
        avatarUpload.addProgressListener(fileUploader);

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

        avatarIcon = new VerticalLayout(fileUploader.getAvatar());
        avatarIcon.setSizeUndefined();

        HorizontalLayout avatarWrap = new HorizontalLayout(avatarIcon, avatarUpload);
        avatarWrap.setSizeUndefined();
        avatarWrap.setCaption("Аватарка");
        avatarWrap.setSpacing(true);

        commonWrap = new FormLayout(avatarWrap, loginField, passCheckbox,
                passFieldOne, passFieldTwo, emailField);

//        Button mailButton = new Button("Отправить");
//        mailButton.addClickListener(event -> {
//            MailUtils.sendMail("Kibi - подтверждение регистрации", "Подтверждите регистрацию", "abalyshev@glosav.ru");
//        });
//        commonWrap.addComponentAsFirst(mailButton);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        commonTab = tabSheet.addTab(commonWrap, user != null ? "Общие" : "Регистрация");

        saveButton = new Button("Сохранить");
        saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.addClickListener(event -> {
            if (saveProfile()) {
                close();
                saveCallback.ifPresent(c -> c.accept(user));
            }
        });

        HorizontalLayout footer = new HorizontalLayout(saveButton);
        footer.setSizeFull();
        footer.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        footer.setExpandRatio(saveButton, 1);

        VerticalLayout wrap = new VerticalLayout(tabSheet, footer);
        wrap.setSizeFull();
        wrap.setMargin(true);
        wrap.setExpandRatio(tabSheet, 10);
        wrap.setExpandRatio(footer, 1);
        setContent(wrap);
    }

    public boolean saveProfile() {
        if (!Objects.equals(passFieldOne.getValue(), passFieldTwo.getValue()))
            Notification.show("Пароль не подтвержден", Notification.Type.ERROR_MESSAGE);
        if (passFieldOne.getValue().trim().length() < 8)
            Notification.show("Пароль не может быть меньше 8 символов", Notification.Type.ERROR_MESSAGE);

        if (user == null) {
            user = new User(loginField.getValue(), passFieldOne.getValue().trim());
            user.setEmail(emailField.getValue());
            return true;
        } else if (user.getId() != null) {
            user.setLogin(loginField.getValue());
            user.setPassword(emailField.getValue());
            if (passCheckbox.getValue()) user.setPassword(passFieldOne.getValue().trim());
            return true;
        }
        return false;
    }

    public void initData() {
        user = dataProvider.getUserCache();
        initForm();
        UI.getCurrent().addWindow(this);
    }

    public void addSaveCallback(Consumer<User> calback) {
        this.saveCallback = Optional.ofNullable(calback);
    }

    private void initForm() {
        clearAll();
        commonTab.setCaption(user == null ? "Регистрация" : "Общие");
        if (user != null) loginField.setValue(user.getLogin());
        if (user != null) passFieldOne.setValue(user.getPassword());
        if (user != null) passFieldTwo.setValue(user.getPassword());
        if (user != null) emailField.setValue(user.getEmail());
        if (user != null) {
            passFieldOne.setEnabled(passCheckbox.getValue());
            passFieldTwo.setEnabled(passCheckbox.getValue());
            passCheckbox.addValueChangeListener(event -> {
                passFieldOne.setEnabled(passCheckbox.getValue());
                passFieldTwo.setEnabled(passCheckbox.getValue());
            });
        } else {
            passCheckbox.setEnabled(false);
            passFieldOne.setEnabled(true);
            passFieldOne.setEnabled(true);
        }
        avatarUpload.setVisible(user != null);
        avatarIcon.removeAllComponents();
        avatarIcon.addComponent(fileUploader.getAvatar());
    }

    private void clearAll() {
        loginField.clear();
        passCheckbox.clear();
        passFieldOne.clear();
        passFieldTwo.clear();
        emailField.clear();
    }

    private class FileUploader implements
            Upload.Receiver, Upload.SucceededListener, Upload.StartedListener, Upload.ProgressListener {
        private File file;
        private Label avatarLabel;
        private Embedded avatar;
        private String path;
        private final long UPLOAD_LIMIT = 1024 * 1024;

        public FileUploader() {
            path = VaadinServlet.getCurrent().getServletContext().getRealPath("/VAADIN/tmp/uploads/");

            avatar = new Embedded();
            avatar.setHeight("60px");
            avatar.setWidthUndefined();
            avatar.addStyleName("avatar_panel");

            avatarLabel = new Label(FontAwesome.USER.getHtml(), ContentMode.HTML);
            avatarLabel.addStyleName("avatar_panel");
            avatarLabel.setHeight("60px");
            avatarLabel.setWidth("60px");
        }

        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            FileOutputStream fos = null;
            try {
                if (!mimeType.contains("image")) throw new Exception("Загруженный файл не является изображением");
                if (user != null && user.getId() != null) {
                    file = new File(path + user.getId() + ".png");
                    fos = new FileOutputStream(file);
                }
            } catch (IOException e) {
                Notification.show("Не удалось сохранить файл", e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
                return null;
            } catch (Exception e) {
                Notification.show("Произошла ошибка",  e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
                return null;
            }
            return fos;
        }

        @Override
        public void uploadSucceeded(Upload.SucceededEvent event) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                BufferedImage scaledImage = Scalr.resize(bufferedImage, 200);
                ImageIO.write(scaledImage, "png", file);
            } catch (IOException e) {
                Notification.show("Не удается масштабировать изображение",  e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
            }
            avatar.setSource(new FileResource(file));
            avatarIcon.removeAllComponents();
            avatarIcon.addComponent(avatar);
        }

        @Override
        public void uploadStarted(Upload.StartedEvent event) {
            long bytes = event.getContentLength();
            if (bytes > UPLOAD_LIMIT) {
                Notification.show(String.format("Размер файла слишком велик: %dкб", (bytes/1024)),
                        Notification.Type.ERROR_MESSAGE);
                avatarUpload.interruptUpload();
            }
        }

        @Override
        public void updateProgress(long readBytes, long contentLength) {
            if (readBytes > UPLOAD_LIMIT) {
                Notification.show(String.format("Размер файла слишком велик: %d", (readBytes/1024)),
                        Notification.Type.ERROR_MESSAGE);
                avatarUpload.interruptUpload();
            }
        }

        public Component getAvatar() {
            if (user != null && user.getId() != null) {
                File file = new File(path + user.getId() + ".png");
                if (file.exists()) {
                    avatar.setSource(new FileResource(file));
                    return avatar;
                }
            }
            return avatarLabel;
        }
    }
}
