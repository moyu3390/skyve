package org.skyve.metadata.view.model.chart;

import org.skyve.domain.Bean;
import org.skyve.impl.metadata.view.model.chart.ChartBuilderMetaData;
import org.skyve.impl.metadata.view.model.chart.ChartBuilderOrderMetaData;
import org.skyve.impl.metadata.view.model.chart.ChartBuilderTopMetaData;
import org.skyve.impl.metadata.view.model.chart.NoBucketMetaData;

public class MetaDataChartModel extends ChartModel<Bean> {
	private ChartBuilderMetaData builder = null;
	
	public MetaDataChartModel(ChartBuilderMetaData builder) {
		this.builder = builder;
	}
	
	@Override
	public ChartData getChartData() {
		Bucket bucket = builder.getCategoryBucket();
		if (bucket instanceof NoBucketMetaData) {
			bucket = null;
		}
		
		String documentName = builder.getDocumentName();
		ChartBuilder result = (documentName != null) ?
								new ChartBuilder().with(builder.getModuleName(), documentName)
													.category(builder.getCategoryBinding(), bucket)
													.value(builder.getValueBinding(), builder.getValueFunction()) :
								new ChartBuilder().withQuery(builder.getModuleName(), builder.getQueryName())
													.category(builder.getCategoryBinding(), bucket)
													.value(builder.getValueBinding(), builder.getValueFunction());
		ChartBuilderTopMetaData top = builder.getTop();
		if (top != null) {
			result.top(top.getTop(), top.getBy(), top.getSort(), top.isIncludeOthers());
		}
		ChartBuilderOrderMetaData order = builder.getOrder();
		if (order != null) {
			result.orderBy(order.getBy(), order.getSort());
		}
		result.jFreeChartPostProcessorClassName(builder.getJFreeChartPostProcessorClassName());
		result.primeFacesChartPostProcessorClassName(builder.getPrimeFacesChartPostProcessorClassName());
		return result.build(builder.getTitle(), builder.getLabel());
	}
}
