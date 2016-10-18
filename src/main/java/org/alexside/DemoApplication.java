package org.alexside;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import org.alexside.gui.LoginPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@SpringBootApplication
public class DemoApplication {

	private Logger log = Logger.getLogger(DemoApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Service
	public static class MyService {
		public String sayHi() {
			return "Vaadin + Spring Демо";
		}

	}

	@Theme("valo")
	@SpringUI(path = "login")
	public static class VaadinUI extends UI {

		@Autowired
		MyService myService;

		@Override
		protected void init(VaadinRequest request) {
			Panel panel = new Panel("<b>KIBI</b> - система управления базой знаний");
			panel.setHeight("100%");
			panel.setWidth("100%");
			panel.setContent(new LoginPanel());
			setContent(panel);
		}

	}
}
