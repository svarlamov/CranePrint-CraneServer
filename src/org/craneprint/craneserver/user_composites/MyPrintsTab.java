package org.craneprint.craneserver.user_composites;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.ui.Craneprint_craneserverUI;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MyPrintsTab extends CustomComponent {

	@AutoGenerated
	private VerticalLayout mainLayout;
	private Table printsTable;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MyPrintsTab() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
	}

	@AutoGenerated
	private void buildMainLayout() {
		// the main layout and components will be created here
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		// TODO: Get the actual session user!!!
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		printsTable = ui.getDBManager().getPrintsForUser("testUser");
		printsTable.setSizeFull();
		mainLayout.addComponent(printsTable);
	}
	
	public void refreshTable() {
		mainLayout.removeComponent(printsTable);
		// TODO: Get the actual session user!!!
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		printsTable = ui.getDBManager().getPrintsForUser("testUser");
		printsTable.setSizeFull();
		mainLayout.addComponent(printsTable);
	}

}
