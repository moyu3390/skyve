package modules.admin.ControlPanel.actions;

import java.util.List;

import org.skyve.CORE;
import org.skyve.domain.messages.Message;
import org.skyve.domain.messages.ValidationException;
import org.skyve.impl.util.XMLMetaData;
import org.skyve.impl.web.UserAgentType;
import org.skyve.metadata.controller.ServerSideAction;
import org.skyve.metadata.controller.ServerSideActionResult;
import org.skyve.metadata.repository.Repository;
import org.skyve.metadata.sail.language.Automation;
import org.skyve.metadata.sail.language.Automation.TestStrategy;
import org.skyve.metadata.user.User;
import org.skyve.web.WebContext;

import modules.admin.ControlPanel.ControlPanelExtension;
import modules.admin.domain.ControlPanel;
import modules.admin.domain.ControlPanel.SailTestStrategy;
import modules.admin.domain.ControlPanel.SailUserAgentType;

public abstract class GenerateSAIL implements ServerSideAction<ControlPanelExtension> {
	private static final long serialVersionUID = 7370653121212184868L;

	@Override
	public ServerSideActionResult<ControlPanelExtension> execute(ControlPanelExtension bean, WebContext webContext)
	throws Exception {
		bean.setResults(null);
		bean.setTabIndex(null);

		modules.admin.domain.User user = bean.getSailUser();
		String moduleName = bean.getSailModuleName();
		String uxui = bean.getSailUxUi();
		SailUserAgentType sailUserAgentType = bean.getSailUserAgentType();
		SailTestStrategy sailTestStrategy = bean.getSailTestStrategy();

		boolean error = false;
		Message message = new Message("Select a value");
		if (user == null) {
			error = true;
			message.addBinding(ControlPanel.sailUserPropertyName);
		}
		if (uxui == null) {
			error = true;
			message.addBinding(ControlPanel.sailUxUiPropertyName);
		}
		if (sailUserAgentType == null) {
			error = true;
			message.addBinding(ControlPanel.sailUserAgentTypePropertyName);
		}
		if (sailTestStrategy == null) {
			error = true;
			message.addBinding(ControlPanel.sailTestStrategyPropertyName);
		}
		if (error) {
			throw new ValidationException(message);
		}
		
		try {
			Repository r = CORE.getRepository();
			@SuppressWarnings("null")
			User u = r.retrieveUser(String.format("%s/%s", user.getBizCustomer(), user.getUserName()));
			@SuppressWarnings("null")
			UserAgentType userAgentType = UserAgentType.valueOf(sailUserAgentType.toCode());
			@SuppressWarnings("null")
			TestStrategy testStrategy = TestStrategy.valueOf(sailTestStrategy.toCode());
			if (moduleName != null) {
				Automation result = single(u, moduleName, uxui, userAgentType, testStrategy);
				if (result.getInteractions().isEmpty()) {
					bean.setResults("NOTHING WAS GENERATED");
				}
				else {
					bean.setResults(XMLMetaData.marshalSAIL(result));
				}
			}
			else {
				List<Automation> result = multiple(u, uxui, userAgentType, testStrategy);
				if (result.isEmpty()) {
					bean.setResults("NOTHING WAS GENERATED");
				}
				else {
					StringBuilder sb = new StringBuilder(4096);
					for (Automation automation : result) {
						sb.append(XMLMetaData.marshalSAIL(automation));
						sb.append('\n');
					}
					bean.setResults(sb.toString());
				}
			}
		}
		catch (Exception e) {
			bean.trapException(e);
		}
		bean.setTabIndex(Integer.valueOf(2));

		return new ServerSideActionResult<>(bean);
	}
	
	protected abstract Automation single(User user,
											String moduleName,
											String uxui,
											UserAgentType userAgentType,
											TestStrategy testStrategy)
	throws Exception;
	protected abstract List<Automation> multiple(User user,
													String uxui,
													UserAgentType userAgentType,
													TestStrategy testStrategy)
	throws Exception;
}
