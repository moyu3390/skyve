package org.skyve.domain.messages;

import java.util.Collections;
import java.util.List;

public class ReferentialConstraintViolationException extends DomainException implements MessageException {
	private static final long serialVersionUID = 4458169944531617791L;

	private static final String MESSAGE_KEY = "exception.referentialConstraintViolation";

	private List<Message> messages = null;
	
	public ReferentialConstraintViolationException(String documentAlias, String bizKey, String referencingDocumentAlias) {
		super(MESSAGE_KEY, documentAlias, bizKey, referencingDocumentAlias);
		messages = Collections.singletonList(new Message(getMessage()));
	}

	@Override
	public List<Message> getMessages() {
		return messages;
	}
}
