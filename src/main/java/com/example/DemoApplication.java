package com.example;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class DemoApplication {

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
	@SpringUI(path = "")
	public static class VaadinUI extends UI {

		@Autowired
		MyService myService;

		@Override
		protected void init(VaadinRequest request) {
			Panel panel = new Panel("KIBI - система управления базой знаний");
			panel.setHeight("300px");
			panel.setWidth("500px");
			VerticalLayout layout = new VerticalLayout(new Label(myService.sayHi()));
			panel.setContent(layout);
			setContent(panel);
		}

	}
}
