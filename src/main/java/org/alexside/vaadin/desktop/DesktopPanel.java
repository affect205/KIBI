package org.alexside.vaadin.desktop;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.alexside.lang.Strings;
import org.alexside.vaadin.desktop.display.DisplayPanel;
import org.alexside.vaadin.desktop.profile.ProfileMenu;
import org.alexside.vaadin.desktop.qa.NoticeQAPanel;
import org.alexside.vaadin.desktop.qa.TagQAPanel;
import org.alexside.vaadin.misc.KibiPanel;
import org.alexside.vaadin.misc.KibiTree;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@ViewScope
public class DesktopPanel extends KibiPanel {

    @Autowired
    private KibiTree kibiTree;

    @Autowired
    private DisplayPanel viewPanel;

    @Autowired
    private TagQAPanel tagQAPanel;

    @Autowired
    private ProfileMenu profileMenu;

    @Autowired
    private StatusBar statusBar;

    @Autowired
    private NoticeQAPanel noticeQAPanel;

    @PostConstruct
    public void onInit() {
        setCaption(Strings.APP_TITLE);
        setSizeFull();
        setContentMargin(true);
        getBottomToolbar().setVisible(true);
        getBottomToolbar().setMargin(false);

        addToTopToolbar(profileMenu);

        HorizontalLayout qaLayout = new HorizontalLayout(noticeQAPanel, tagQAPanel);
        qaLayout.setSizeFull();
        qaLayout.setSpacing(true);
        qaLayout.setMargin(new MarginInfo(true, false, false, false));

        VerticalLayout viewLayout = new VerticalLayout(viewPanel, qaLayout);
        viewLayout.setSizeFull();
        viewLayout.setExpandRatio(viewPanel, 7);
        viewLayout.setExpandRatio(qaLayout, 2);

        HorizontalLayout contentLayout = new HorizontalLayout(kibiTree, viewLayout);
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);
        contentLayout.setExpandRatio(kibiTree, 1);
        contentLayout.setExpandRatio(viewLayout, 3);

        addToBottomToolbar(statusBar, Alignment.BOTTOM_LEFT);
        setContentAlt(contentLayout);
    }
}
