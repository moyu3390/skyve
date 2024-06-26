package org.skyve.metadata.view.fluent;

import org.skyve.impl.metadata.view.Inject;

public class FluentInject extends FluentWidget {
	private Inject inject = null;

	public FluentInject() {
		inject = new Inject();
	}

	public FluentInject(Inject inject) {
		this.inject = inject;
	}

	public FluentInject from(@SuppressWarnings("hiding") Inject inject) {

		script(inject.getScript());

		inject.getBindings().forEach(b -> addBinding(new FluentInjectBinding().from(b)));

		return this;
	}

	public FluentInject script(String script) {
		inject.setScript(script);
		return this;
	}

	public FluentInject addBinding(FluentInjectBinding binding) {
		inject.getBindings().add(binding.get());
		return this;
	}

	@Override
	public Inject get() {
		return inject;
	}
}
