package org.skyve.domain.types.converters.timestamp;

public class MM_DD_YYYY_HH24_MI_SS extends AbstractTimestampConverter {
	public static final String PATTERN = "MM/dd/yyyy HH:mm:ss";

	@Override
	protected String getPattern() {
		return PATTERN;
	}
}
