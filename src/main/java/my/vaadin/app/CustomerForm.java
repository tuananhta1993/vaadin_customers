package my.vaadin.app;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout {
	private TextField firstName = new TextField("First Name");
	private TextField lastName = new TextField("Last Name");
	private TextField email = new TextField("Email");
	private NativeSelect status = new NativeSelect("Status");
	private PopupDateField birthDate = new PopupDateField("Birthdate");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	private CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private MyUI myUI;
	
	public CustomerForm(MyUI myUI)
	{
		this.myUI=myUI;
		status.addItems(CustomerStatus.values());
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> {
			this.save();
		});
		
		delete.setStyleName(ValoTheme.BUTTON_DANGER);
		delete.addClickListener(e -> this.delete());
		
		setSizeUndefined();
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(save, delete);
		buttons.setMargin(true);
		buttons.setSpacing(true);
		
		addComponents(firstName, lastName, email, status, birthDate, buttons);
		
	}
	
	public void setCustomer(Customer customer)
	{
		this.customer=customer;
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);
		
		delete.setVisible(customer.isPersisted());
		setVisible(true);
		firstName.selectAll();
		
	}
	
	private void save()
	{
		service.save(customer);
		myUI.update_list();
		setVisible(false);
	}
	
	private void delete()
	{
		service.delete(customer);
		myUI.update_list();
		setVisible(false);
	}
}
