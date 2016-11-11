package org.alexside.vaadin.desktop.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.alexside.entity.Notice;
import org.alexside.entity.TItem;
import org.alexside.enums.TreeKind;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.EventUtils;
import org.alexside.utils.HtmlUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.alexside.utils.HtmlUtils.URL_TEST;

/**
 * Created by Alex on 04.11.2016.
 */
@SpringComponent
@ViewScope
public class ViewPanel extends Panel {

    private VerticalLayout layout;
    private EventBus eventBus;
    private TabSheet tabsheet;

    private TextField nameField;
    private TextArea editTA;
    private RichTextArea viewRTA;

    @PostConstruct
    public void onInit() {
        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        setCaption("<b>Просмотр</b>");
        setSizeFull();

        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        nameField = new TextField();
        nameField.setWidth("100%");

        tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        editTA = new TextArea();
        editTA.setSizeFull();
        editTA.setWordwrap(true);
        VerticalLayout editLayout = new VerticalLayout(editTA);
        editLayout.setSizeFull();

        viewRTA = new RichTextArea();
        viewRTA.setSizeFull();
        //viewRTA.addStyleName("no-toolbar");
        viewRTA.setValue(HtmlUtils.loadHtml(URL_TEST));
        VerticalLayout viewLayout = new VerticalLayout(viewRTA);
        viewLayout.setSizeFull();

        TabSheet.Tab viewTab = tabsheet.addTab(viewLayout, "Просмотр", null);
        TabSheet.Tab editTab = tabsheet.addTab(editLayout, "Редактирование", null);

        TextField urlField = new TextField();
        urlField.setWidth("320px");
        Button urlButton = new Button(FontAwesome.UPLOAD);
        urlButton.setDescription("Загрузить");
        urlButton.addClickListener(event -> {
            viewRTA.setValue(HtmlUtils.loadHtml(urlField.getValue()));
        });

        Button saveButton = new Button("Сохранить");

        HorizontalLayout bottomToolbar = new HorizontalLayout(urlField, urlButton, saveButton);
        bottomToolbar.setSizeFull();
        bottomToolbar.setSpacing(true);
        bottomToolbar.setComponentAlignment(urlField, Alignment.MIDDLE_LEFT);
        bottomToolbar.setComponentAlignment(urlButton, Alignment.MIDDLE_LEFT);
        bottomToolbar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        bottomToolbar.setExpandRatio(saveButton, 1);

        layout.addComponents(nameField, tabsheet, bottomToolbar);
        layout.setExpandRatio(nameField, 1);
        layout.setExpandRatio(tabsheet, 7);
        layout.setExpandRatio(bottomToolbar, 1);

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
        TItem ti = event.getItem();
        nameField.setValue(ti.getName());
        if (ti.getKind() == TreeKind.CATEGORY) {
            tabsheet.setEnabled(false);
        } else if (ti.getKind() == TreeKind.NOTICE) {
            Notice notice = (Notice)ti;
            viewRTA.setValue(notice.getContent());
            editTA.setValue(notice.getContent());
        }
    }

    public void resetPanel() {
        nameField.clear();
        editTA.clear();
        viewRTA.clear();
        tabsheet.setEnabled(true);
    }
}
