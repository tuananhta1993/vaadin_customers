package my.vaadin.app;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	private CustomerService service = CustomerService.getInstance();
	private Grid grid = new Grid();
	private TextField filterText = new TextField();
	private CustomerForm customerForm = new CustomerForm(this);
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	//*******************
    	// Original version
    	//*******************
    	
//        final VerticalLayout layout = new VerticalLayout();
//        
//        final TextField name = new TextField();
//        name.setCaption("Type your name here:");
//
//        Button button = new Button("Click Me");
//        button.addClickListener( e -> {
//            layout.addComponent(new Label("Thanks " + name.getValue() 
//                    + ", it works!"));
//        });
//        
//        layout.addComponents(name, button);
//        layout.setMargin(true);
//        layout.setSpacing(true);
//        
//        setContent(layout);
    	
    	//*******************
    	// Label version
    	//*******************
//    	Label label = new Label("Hello Helsinki");
//    	setContent(label);
    	
    	//*******************
    	// Button version
    	//*******************
//    	Button button = new Button("Butt");
//    	button.addClickListener(e -> Notification.show("Hello Helsinki"));
//    	setContent(button);
    	
    	//*******************
    	// Button + text field version
    	//*******************
//    	TextField textField = new TextField("Name");
//    	Button button = new Button("Dispay text field value");
//    	button.addClickListener(e -> Notification.show("Hello " + textField.getValue()));
//    	
//    	VerticalLayout layout = new VerticalLayout();
//    	layout.setMargin(true);
//    	layout.setSpacing(true);
//    	layout.addComponents(textField, button);

//    	setContent(layout);
    	
    	//*******************
    	// Fake DB version
    	//*******************

    	
    	VerticalLayout layout = new VerticalLayout();
    	
    	filterText.setInputPrompt("filter by name ...");
    	filterText.addValueChangeListener(e -> {
    		grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, 
    				service.findAll(filterText.getValue())));
    	});
    	
    	Button clearFilterText = new Button(FontAwesome.TIMES);
    	clearFilterText.addClickListener(e -> {
//    		filterText.setValue("");
    		filterText.clear();
    		update_list();
    	});
    	grid.setColumns("firstName","lastName","email");
    	
    	HorizontalLayout main = new HorizontalLayout(grid,customerForm); 
    	main.setSpacing(true);
    	main.setSizeFull();
    	grid.setSizeFull();
    	main.setExpandRatio(grid, 1);
//    	HorizontalLayout filtering = new HorizontalLayout();
//    	filtering.addComponents(filterText,clearFilterText);
    	
    	CssLayout filtering = new CssLayout();
    	filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    	filtering.addComponents(filterText,clearFilterText);
    	
    	Button addCustomerBtn = new Button("Add new customer");
    	addCustomerBtn.addClickListener(e -> {
    		grid.select(null);
    		customerForm.setCustomer(new Customer());
    	});
    	
    	HorizontalLayout buttonsLayout = new HorizontalLayout();
    	buttonsLayout.addComponents(filtering,addCustomerBtn);
    	buttonsLayout.setMargin(true);
    	buttonsLayout.setSpacing(true);
    	
    	layout.addComponents(buttonsLayout, main);
    	
    	update_list();
    	
    	layout.setMargin(true);
    	layout.setSpacing(true);
    	layout.addComponents();
    	
    	setContent(layout);
    	customerForm.setVisible(false);
    	
    	grid.addSelectionListener(e -> {
    		if(e.getSelected().isEmpty()){
    			customerForm.setVisible(false);
    		}else{
    			Customer customer = (Customer) e.getSelected().iterator().next();
    			customerForm.setCustomer(customer);
    		}
    	});
    }

	public void update_list() {
		List<Customer> customers = service.findAll();
    	grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
