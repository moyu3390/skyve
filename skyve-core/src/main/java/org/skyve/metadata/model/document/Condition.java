package org.skyve.metadata.model.document;

import org.skyve.metadata.SerializableMetaData;
import org.skyve.metadata.model.Attribute.UsageType;

public interface Condition extends SerializableMetaData {
	public String getDocumentation();
	public String getDescription();
	public String getExpression();

	/**
	 * Informs the Skyve framework when to include and exclude the attributes.
	 * @return	the usage.
	 */
	public UsageType getUsage();
}
