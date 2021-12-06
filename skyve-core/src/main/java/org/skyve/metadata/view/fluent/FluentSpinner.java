package org.skyve.metadata.view.fluent;

import org.skyve.impl.metadata.view.widget.bound.input.Spinner;

public class FluentSpinner extends FluentTextField { // FluentChangeableInputWidget<FluentSpinner> implements
														// FluentAbsoluteWidth<FluentSpinner> {
	private Spinner spinner = null;

	public FluentSpinner() {
		spinner = new Spinner();
	}

	public FluentSpinner(Spinner spinner) {
		this.spinner = spinner;
	}

	public FluentSpinner from(@SuppressWarnings("hiding") Spinner spinner) {
		min(spinner.getMin());
		max(spinner.getMax());
		step(spinner.getStep());

		super.from(spinner);
		return this;
	}

	public FluentSpinner min(double min) {
		spinner.setMin(Double.valueOf(min));
		return this;
	}

	public FluentSpinner max(double max) {
		spinner.setMax(Double.valueOf(max));
		return this;
	}

	public FluentSpinner step(double step) {
		spinner.setStep(Double.valueOf(step));
		return this;
	}

	@Override
	public Spinner get() {
		return spinner;
	}
}
