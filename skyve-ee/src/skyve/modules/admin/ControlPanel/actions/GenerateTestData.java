package modules.admin.ControlPanel.actions;

import org.skyve.CORE;
import org.skyve.EXT;
import org.skyve.domain.messages.Message;
import org.skyve.domain.messages.MessageSeverity;
import org.skyve.domain.messages.ValidationException;
import org.skyve.metadata.controller.ServerSideAction;
import org.skyve.metadata.controller.ServerSideActionResult;
import org.skyve.metadata.customer.Customer;
import org.skyve.metadata.module.JobMetaData;
import org.skyve.metadata.module.Module;
import org.skyve.web.WebContext;

import modules.admin.ControlPanel.ControlPanelExtension;
import modules.admin.domain.ControlPanel;

public class GenerateTestData implements ServerSideAction<ControlPanelExtension> {

	private static final long serialVersionUID = -1015595549604693410L;

	@Override
	public ServerSideActionResult<ControlPanelExtension> execute(ControlPanelExtension bean, WebContext webContext)
			throws Exception {
		// validate required fields
		validateFields(bean);

		Customer customer = CORE.getCustomer();
		Module m = customer.getModule(ControlPanel.MODULE_NAME);
		JobMetaData job = m.getJob("jGenerateTestData");
		
		EXT.runOneShotJob(job, bean, CORE.getUser());
		webContext.growl(MessageSeverity.info, "Generate Test Data Job has been started");

		return new ServerSideActionResult<>(bean);
	}

	private void validateFields(ControlPanelExtension bean) {
		ValidationException ve = new ValidationException();

		if (bean.getTestModuleName() == null) {
			ve.getMessages().add(new Message(ControlPanel.testModuleNamePropertyName, "Module Name is required."));
		}

		if (bean.getTestDocumentNames().size() == 0) {
			ve.getMessages().add(new Message(ControlPanel.testDocumentNamesPropertyName, "At least one Document is required."));
		}

		if (bean.getTestNumberToGenerate() == null) {
			ve.getMessages().add(new Message(ControlPanel.testNumberToGeneratePropertyName, "Number to Generate is required."));
		}
		
		if (bean.getTestNumberToGenerate() < 1) {
			ve.getMessages().add(new Message(ControlPanel.testNumberToGeneratePropertyName, "Number to Generate must be greater than 0."));
		}
		
		if (bean.getTestNumberToGenerate() > 10000) {
			ve.getMessages().add(new Message(ControlPanel.testNumberToGeneratePropertyName, "Number to Generate must be less than 10,000."));
		}

		if (ve.getMessages().size() > 0) {
			throw ve;
		}
	}
}