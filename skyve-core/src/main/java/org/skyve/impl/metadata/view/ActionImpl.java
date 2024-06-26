package org.skyve.impl.metadata.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.skyve.impl.bind.BindUtil;
import org.skyve.impl.metadata.repository.view.actions.ActionMetaData;
import org.skyve.impl.metadata.repository.view.actions.AddAction;
import org.skyve.impl.metadata.repository.view.actions.BizExportAction;
import org.skyve.impl.metadata.repository.view.actions.BizImportAction;
import org.skyve.impl.metadata.repository.view.actions.CancelAction;
import org.skyve.impl.metadata.repository.view.actions.ClassAction;
import org.skyve.impl.metadata.repository.view.actions.CustomAction;
import org.skyve.impl.metadata.repository.view.actions.DefaultsAction;
import org.skyve.impl.metadata.repository.view.actions.DeleteAction;
import org.skyve.impl.metadata.repository.view.actions.NewAction;
import org.skyve.impl.metadata.repository.view.actions.OKAction;
import org.skyve.impl.metadata.repository.view.actions.ParameterizableAction;
import org.skyve.impl.metadata.repository.view.actions.PositionableAction;
import org.skyve.impl.metadata.repository.view.actions.RemoveAction;
import org.skyve.impl.metadata.repository.view.actions.ReportAction;
import org.skyve.impl.metadata.repository.view.actions.SaveAction;
import org.skyve.impl.metadata.repository.view.actions.ZoomOutAction;
import org.skyve.impl.web.AbstractWebContext;
import org.skyve.metadata.controller.ImplicitActionName;
import org.skyve.metadata.controller.ServerSideAction;
import org.skyve.metadata.customer.Customer;
import org.skyve.metadata.model.document.Document;
import org.skyve.metadata.view.Action;
import org.skyve.metadata.view.widget.bound.Parameter;
import org.skyve.report.ReportFormat;

public class ActionImpl implements Action {
	private static final long serialVersionUID = -133387187684800312L;

	private String name;
	private ImplicitActionName implicitName;
	private String resourceName;
	private Boolean clientValidation = Boolean.TRUE;
	private String displayName;
	private String toolTip;
	private String relativeIconFileName;
	private String iconStyleClass;
	private String confirmationText;
	private Boolean inActionPanel = Boolean.TRUE;
	private ActionShow show = ActionShow.both;
	private String disabledConditionName;
	private String invisibleConditionName;
	private List<Parameter> parameters = new ArrayList<>();
	private Map<String, String> properties = new TreeMap<>();

	@Override
	public List<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public String getName() {
		String result = name;
		if (result == null) {
			result = resourceName;
		}
		if (result == null) {
			result = implicitName.name();
		}

		return result;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ImplicitActionName getImplicitName() {
		return implicitName;
	}

	public void setImplicitName(ImplicitActionName implicitName) {
		this.implicitName = implicitName;
	}

	@Override
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public Boolean getClientValidation() {
		return clientValidation;
	}

	public void setClientValidation(Boolean clientValidation) {
		this.clientValidation = clientValidation;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getRelativeIconFileName() {
		return relativeIconFileName;
	}

	public void setRelativeIconFileName(String relativeIconFileName) {
		this.relativeIconFileName = relativeIconFileName;
	}

	@Override
	public String getIconStyleClass() {
		return iconStyleClass;
	}

	public void setIconStyleClass(String iconStyleClass) {
		this.iconStyleClass = iconStyleClass;
	}

	@Override
	public String getConfirmationText() {
		return confirmationText;
	}

	public void setConfirmationText(String confirmationText) {
		this.confirmationText = confirmationText;
	}

	@Override
	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	@Override
	public Boolean getInActionPanel() {
		return inActionPanel;
	}

	public void setInActionPanel(Boolean inActionPanel) {
		this.inActionPanel = inActionPanel;
	}

	@Override
	public ActionShow getShow() {
		return show;
	}

	public void setShow(ActionShow show) {
		this.show = show;
	}

	@Override
	public ServerSideAction<?> getServerSideAction(Customer customer, Document document) {
		if (resourceName == null) {
			throw new IllegalStateException("The ActionConfig " + getName() + " is an implicit action.");
		}

		return document.getServerSideAction(customer, resourceName, true);
	}

	@Override
	public String getInvisibleConditionName() {
		return invisibleConditionName;
	}

	@Override
	public void setInvisibleConditionName(String invisibleConditionName) {
		this.invisibleConditionName = invisibleConditionName;
	}

	// to enable JAXB XML marshaling
	@SuppressWarnings("static-method")
	String getVisibleConditionName() {
		return null;
	}

	@Override
	public void setVisibleConditionName(String visibleConditionName) {
		this.invisibleConditionName = BindUtil.negateCondition(visibleConditionName);
	}

	@Override
	public String getDisabledConditionName() {
		return disabledConditionName;
	}

	@Override
	public void setDisabledConditionName(String disabledConditionName) {
		this.disabledConditionName = disabledConditionName;
	}

	// to enable JAXB XML marshaling
	@SuppressWarnings("static-method")
	String getEnabledConditionName() {
		return null;
	}

	@Override
	public void setEnabledConditionName(String enabledConditionName) {
		this.disabledConditionName = BindUtil.negateCondition(enabledConditionName);
	}

	public ActionMetaData toRepositoryAction() {
		ActionMetaData result = null;
		if (implicitName == null) { // custom action
			result = new CustomAction();
			((CustomAction) result).setClientValidation(getClientValidation());
		}
		else {
			switch (implicitName) {
			case Add:
				result = new AddAction();
				break;
			case BizExport:
				result = new BizExportAction();
				break;
			case BizImport:
				result = new BizImportAction();
				break;
			case Cancel:
				result = new CancelAction();
				break;
			case DEFAULTS:
				result = new DefaultsAction();
				break;
			case Delete:
				result = new DeleteAction();
				break;
			case New:
				result = new NewAction();
				break;
			case OK:
				result = new OKAction();
				break;
			case Remove:
				result = new RemoveAction();
				break;
			case Report:
				ReportAction report = new ReportAction();
				String reportName = getResourceName();
				if (reportName == null) {
					reportName = getName();
				}
				report.setReportName(reportName);
				for (Parameter parameter : getParameters()) {
					String parameterName = parameter.getName();
					if (AbstractWebContext.MODULE_NAME.equals(parameterName)) {
						report.setModuleName(parameter.getValue());
					}
					else if (AbstractWebContext.DOCUMENT_NAME.equals(parameterName)) {
						report.setDocumentName(parameter.getValue());
					}
					else if (AbstractWebContext.REPORT_FORMAT.equals(parameterName)) {
						report.setReportFormat(ReportFormat.valueOf(parameter.getValue()));
					}
				}
				result = report;
				break;
			case Save:
				result = new SaveAction();
				break;
			case ZoomOut:
				result = new ZoomOutAction();
				break;
			default:
				throw new IllegalStateException(implicitName + " not catered for.");
			}
		}
		
		result.setConfirmationText(getConfirmationText());
		result.setDisabledConditionName(getDisabledConditionName());
		result.setDisplayName(getDisplayName());
		result.setInvisibleConditionName(getInvisibleConditionName());
		result.setRelativeIconFileName(getRelativeIconFileName());
		result.setIconStyleClass(getIconStyleClass());
		result.setToolTip(getToolTip());

		if (Boolean.FALSE.equals(inActionPanel) && (result instanceof PositionableAction)) {
			((PositionableAction) result).setInActionPanel(Boolean.FALSE);
		}

		if (result instanceof ClassAction) {
			((ClassAction) result).setClassName(getResourceName());
		}
		
		if (result instanceof ParameterizableAction) {
			((ParameterizableAction) result).getParameters().addAll(getParameters());
		}
		
		return result;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}
}
