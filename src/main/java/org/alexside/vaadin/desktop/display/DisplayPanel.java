package org.alexside.vaadin.desktop.display;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.alexside.entity.TItem;
import org.alexside.events.TItemRefreshEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.DataProvider;
import org.alexside.utils.EventUtils;
import org.alexside.utils.HtmlUtils;
import org.alexside.vaadin.misc.HeaderButton;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.alexside.utils.HtmlUtils.URL_TEST;
import static org.alexside.utils.ThemeUtils.PANEL_HEADER;

/**
 * Created by Alex on 04.11.2016.
 */
@SpringComponent
@ViewScope
public class DisplayPanel extends Panel {

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

        layout = new VerticalLayout();
        layout.setSizeFull();

        Label captionLabel = new Label("<b>Просмотр</b>", ContentMode.HTML);
        captionLabel.setCaptionAsHtml(true);

        nameField = new TextField();
        nameField.setWidth("360px");
        nameField.setHeight("60%");

        HeaderButton viewBtn = HeaderButton.createEyeButton();
        viewBtn.addClickListener(event -> {
            contentWrap.replaceComponent(editLayout, viewLayout);
            contentWrap.addComponentAsFirst(viewLayout);
            contentWrap.setExpandRatio(viewLayout, 10);
        });
        HeaderButton editBtn = HeaderButton.createCodeButton();
        editBtn.addClickListener(event -> {
            contentWrap.replaceComponent(viewLayout, editLayout);
            contentWrap.addComponentAsFirst(editLayout);
            contentWrap.setExpandRatio(editLayout, 10);
        });

        HorizontalLayout controlWrap = new HorizontalLayout(viewBtn, editBtn);
        controlWrap.setSpacing(true);

        HorizontalLayout topToolbar = new HorizontalLayout(captionLabel, nameField, controlWrap);
        topToolbar.setSizeFull();
        topToolbar.addStyleName(PANEL_HEADER);
        topToolbar.setComponentAlignment(nameField, Alignment.TOP_LEFT);
        topToolbar.setComponentAlignment(controlWrap, Alignment.TOP_RIGHT);
        topToolbar.setExpandRatio(captionLabel, 1);
        topToolbar.setExpandRatio(nameField, 10);
        topToolbar.setExpandRatio(controlWrap, 1);

        String html = HtmlUtils.loadHtml(URL_TEST);

        editTA = new TextArea();
        editTA.setSizeFull();
        editTA.setWordwrap(true);
        editTA.setValue(html);

        editLayout = new VerticalLayout(editTA);
        editLayout.setSizeFull();

        viewRTA = new RichTextArea();
        viewRTA.setSizeFull();
        viewRTA.setValue(html);
        //viewRTA.addStyleName("no-toolbar");
        viewLayout = new VerticalLayout(viewRTA);
        viewLayout.setSizeFull();

        //TabSheet.Tab viewTab = tabsheet.addTab(viewLayout, "", FontAwesome.EYE);
        //TabSheet.Tab editTab = tabsheet.addTab(editLayout, "", FontAwesome.CODE);

        TextField urlField = new TextField();
        urlField.setWidth("320px");
        Button urlButton = new Button(FontAwesome.UPLOAD);
        urlButton.setDescription("Загрузить");
        urlButton.addClickListener(event -> {
            viewRTA.setValue(HtmlUtils.loadHtml(urlField.getValue()));
        });

        Button saveButton = new Button("Сохранить");
        saveButton.addClickListener(event -> {
            if (editTi != null) {
                editTi.setName(nameField.getValue());
                if (editTi.isNotice()) editTi.setContent(editTA.getValue());
                dataProvider.saveTItem(editTi);
                EventUtils.post(new TItemRefreshEvent(editTi));
            }
        });

        HorizontalLayout bottomToolbar = new HorizontalLayout(urlField, urlButton, saveButton);
        bottomToolbar.setSizeFull();
        bottomToolbar.setSpacing(true);
        bottomToolbar.setComponentAlignment(urlField, Alignment.MIDDLE_LEFT);
        bottomToolbar.setComponentAlignment(urlButton, Alignment.MIDDLE_LEFT);
        bottomToolbar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        bottomToolbar.setExpandRatio(saveButton, 1);

        contentWrap = new VerticalLayout(viewLayout, bottomToolbar);
        contentWrap.setSizeFull();
        contentWrap.setMargin(true);
        contentWrap.setExpandRatio(viewLayout, 10);
        contentWrap.setExpandRatio(bottomToolbar, 1);
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

        layout.addComponents(topToolbar, contentWrap);
        layout.setExpandRatio(topToolbar, 1);
        layout.setExpandRatio(contentWrap, 10);

        setContent(layout);
    }

    @PreDestroy
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Subscribe
    public void onTItemSelection(TItemSelectionEvent event) {
        if (event.getItem() == null) return;
        resetPanel();
        editTi = event.getItem();
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
