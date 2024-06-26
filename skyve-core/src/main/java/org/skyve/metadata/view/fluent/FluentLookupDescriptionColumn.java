package org.skyve.metadata.view.fluent;

import org.skyve.impl.metadata.view.widget.bound.input.LookupDescriptionColumn;

public class FluentLookupDescriptionColumn {
	private LookupDescriptionColumn column = null;

	public FluentLookupDescriptionColumn() {
		column = new LookupDescriptionColumn();
	}

	public FluentLookupDescriptionColumn(LookupDescriptionColumn column) {
		this.column = column;
	}

	public FluentLookupDescriptionColumn from(@SuppressWarnings("hiding") LookupDescriptionColumn column) {
		name(column.getName());
		Boolean b = column.getFilterable();
		if (b != null) {
			filterable(b.booleanValue());
		}
		return this;
	}

	public FluentLookupDescriptionColumn filterable(boolean filterable) {
		column.setFilterable(filterable ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	public FluentLookupDescriptionColumn name(String name) {
		column.setName(name);
		return this;

	}

	public LookupDescriptionColumn get() {
		return column;
	}
}
