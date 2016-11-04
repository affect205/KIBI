package org.alexside.vaadin.desktop.notice;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.alexside.utils.HtmlUtils;
import org.alexside.utils.SpringUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static org.alexside.utils.HtmlUtils.URL_TEST;

/**
 * Created by Alex on 04.11.2016.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class ViewPanel extends Panel {

    private VerticalLayout layout;

    @PostConstruct
    public void onInit() {
        setCaption("<b>Просмотр</b>");
        setSizeFull();

        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        TextField nameField = new TextField();
        nameField.setWidth("100%");

        TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        TextArea editTA = new TextArea();
        editTA.setSizeFull();
        editTA.setWordwrap(true);
        VerticalLayout editLayout = new VerticalLayout(editTA);
        editLayout.setSizeFull();

        RichTextArea viewRTA = new RichTextArea();
        viewRTA.setSizeFull();
        viewRTA.setValue(HtmlUtils.loadHtml(URL_TEST));
        VerticalLayout viewLayout = new VerticalLayout(viewRTA);
        viewLayout.setSizeFull();

        TabSheet.Tab viewTab = tabsheet.addTab(viewLayout, "Просмотр", null);
        TabSheet.Tab editTab = tabsheet.addTab(editLayout, "Редактирование", null);

        layout.addComponents(nameField, tabsheet);
        layout.setExpandRatio(nameField, 1);
        layout.setExpandRatio(tabsheet, 7);

        setContent(layout);
    }
}
