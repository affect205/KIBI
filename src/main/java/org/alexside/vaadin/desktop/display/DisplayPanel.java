package org.alexside.vaadin.desktop.display;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.alexside.entity.TItem;
import org.alexside.events.StatusEvent;
import org.alexside.events.TItemQASelectionEvent;
import org.alexside.events.TItemRefreshEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.DataProvider;
import org.alexside.utils.EventUtils;
import org.alexside.utils.HtmlUtils;
import org.alexside.vaadin.misc.IconButton;
import org.alexside.vaadin.misc.KibiPanel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.alexside.vaadin.misc.IconButton.*;

/**
 * Created by Alex on 04.11.2016.
 */
@SpringComponent
@ViewScope
public class DisplayPanel extends KibiPanel {

    @Autowired
    protected DataProvider dataProvider;

    private VerticalLayout layout;
    private EventBus eventBus;

    private TextField nameField;
    private TextArea editTA;
    private RichTextArea viewRTA;

    private TItem editTi;

    private VerticalLayout editLayout;
    private VerticalLayout viewLayout;
    private VerticalLayout contentWrap;

    @PostConstruct
    public void onInit() {
        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        setSizeFull();
        getBottomToolbar().setVisible(true);

        layout = new VerticalLayout();
        layout.setSizeFull();

        nameField = new TextField();
        nameField.setWidth("360px");
        nameField.setHeight("60%");

        IconButton viewBtn = eyeButton();
        viewBtn.addClickListener(event -> {
            contentWrap.replaceComponent(editLayout, viewLayout);
            contentWrap.addComponentAsFirst(viewLayout);
            contentWrap.setExpandRatio(viewLayout, 10);
        });
        IconButton editBtn = codeButton();
        editBtn.addClickListener(event -> {
            contentWrap.replaceComponent(viewLayout, editLayout);
            contentWrap.addComponentAsFirst(editLayout);
            contentWrap.setExpandRatio(editLayout, 10);
        });

        HorizontalLayout controlWrap = new HorizontalLayout(viewBtn, editBtn);
        controlWrap.setSpacing(true);

        addToTopToolbar(nameField, Alignment.TOP_LEFT, 10);
        addToTopToolbar(controlWrap);

        //String html = HtmlUtils.loadHtml(URL_TEST);

        editTA = new TextArea();
        editTA.setSizeFull();
        editTA.setWordwrap(true);
        //editTA.setValue(html);

        editLayout = new VerticalLayout(editTA);
        editLayout.setSizeFull();

        viewRTA = new RichTextArea();
        viewRTA.setSizeFull();
        //viewRTA.setValue(html);
        //viewRTA.addStyleName("no-toolbar");
        viewLayout = new VerticalLayout(viewRTA);
        viewLayout.setSizeFull();

        Button saveButton = new Button("Сохранить");
        saveButton.addStyleName(ValoTheme.BUTTON_TINY);
        saveButton.addClickListener(event -> {
            try {
                if (editTi != null) {
                    editTi.setName(nameField.getValue());
                    if (editTi.isNotice()) {
                        if (viewRTA.isAttached()) {
                            editTi.setContent(viewRTA.getValue());
                        } else if (editTA.isAttached()) {
                            editTi.setContent(editTA.getValue());
                        }
                    }
                    dataProvider.saveTItem(editTi);
                    EventUtils.post(new TItemRefreshEvent(editTi));
                    EventUtils.post(new StatusEvent("Запись сохранена"));
                }
            } catch (Throwable e) {
                EventUtils.post(new StatusEvent(String.format("Ошибка сохранения: %s", e.getMessage())));
            }
        });

        TextField urlField = new TextField();
        urlField.setWidth("320px");

        Button loadButton = new Button(FontAwesome.SEND);

        HorizontalLayout urlPanel = new HorizontalLayout(urlField, loadButton);
        urlPanel.setSizeFull();
        urlPanel.setSpacing(true);
        urlPanel.setMargin(true);
        urlPanel.setExpandRatio(loadButton, 1);

        Window urlWindow = new Window("Загрузкка из интернета");
        urlWindow.setWidth("420px");
        urlWindow.setHeight("120px");
        urlWindow.setContent(urlPanel);
        urlWindow.setModal(true);

        loadButton.addClickListener(event -> {
            final String result = HtmlUtils.loadHtml(urlField.getValue());
            if (viewRTA.isAttached()) {
                viewRTA.setValue(result);
            } else if (editTA.isAttached()) {
                editTA.setValue(result);
            }
            urlWindow.close();
        });

        IconButton urlButton = urlButton();
        urlButton.setDescription("Загрузить");
        urlButton.addClickListener(event -> {
            UI.getCurrent().addWindow(urlWindow);
        });

        addToBottomToolbar(urlButton);
        addToBottomToolbar(saveButton, Alignment.BOTTOM_RIGHT);

        contentWrap = new VerticalLayout(viewLayout);
        contentWrap.setSizeFull();
        contentWrap.setExpandRatio(viewLayout, 10);
        contentWrap.addComponentDetachListener(event -> {
            Component c = event.getDetachedComponent();
            if (c != null) {
                if (c == viewLayout) {
                    editTA.setValue(viewRTA.getValue());
                } else if (c == editLayout) {
                    viewRTA.setValue(editTA.getValue());
                }
            }
        });

        setContentAlt(contentWrap);
    }

    @PreDestroy
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Subscribe
    public void onTItemSelection(TItemSelectionEvent event) {
        if (event.getItem() == null) return;
        selectTItem(event.getItem());
    }

    @Subscribe
    public void onTItemQASelectionEvent(TItemQASelectionEvent event) {
        if (event.getItem() == null) return;
        selectTItem(event.getItem());
    }

    private void selectTItem(TItem ti) {
        resetPanel();
        editTi = ti;
        nameField.setValue(editTi.getName());
        if (editTi.isCategory()) {
            viewRTA.setEnabled(false);
            editTA.setEnabled(false);
        } else if (editTi.isNotice()) {
            viewRTA.setValue(editTi.getContent());
            editTA.setValue(editTi.getContent());
        }
    }

    public void resetPanel() {
        editTi = null;
        nameField.clear();
        viewRTA.clear();
        editTA.clear();
        viewRTA.setEnabled(true);
        editTA.setEnabled(true);
    }
}
