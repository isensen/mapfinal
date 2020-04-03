package com.mapfinal.map.layer;

import com.mapfinal.dispatcher.FeatureDispatcher;
import com.mapfinal.event.Event;
import com.mapfinal.map.Feature;
import com.mapfinal.map.MapContext;
import com.mapfinal.render.RenderEngine;
import com.mapfinal.resource.FeatureCollection;
import org.locationtech.jts.geom.Envelope;

/**
 * 参考ArcGIS FeatureLayer结合一下，特别是FeatureTable
 * https://www.cnblogs.com/arxive/p/7751880.html
 * @author yangyong
 *
 */
public class FeatureLayer extends AbstractLayer {
	/**
	 * 资源
	 */
	private FeatureCollection resource;
	/**
	 * 默认通过FeatureDispatcher调度器实现
	 */
	private FeatureDispatcher dispatcher;
	
	public FeatureLayer(FeatureCollection resource) {
		this.resource = resource;
		setName(resource.getName());
		setTitle(resource.getName());
		dispatcher = (FeatureDispatcher) resource.connection();
		System.out.println("FeatureDispatcher: " + resource.getName());
		setSpatialReference(resource.getSpatialReference());
	}
	/*
	public void addFeature(Feature feature) {
		if(resource!=null) {
			resource.addFeature(feature);
		}
	}

	public void deleteFeature(Feature feature) {
		if(resource!=null) {
			resource.deleteFeature(feature.getId());
		}
	}
	
	public void deleteFeature(Long featureId) {
		if(resource!=null) {
			resource.deleteFeature(featureId);
		}
	}*/

	@Override
	public boolean isDrawable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onRender(Event event, RenderEngine engine) {
		// TODO Auto-generated method stub
		if(!isVisible()) return;
		MapContext context = event.get("map");
		int zoom = (int) context.getZoom();
		if(zoom < getMinZoom() || zoom > getMaxZoom()) return;
		//System.out.println("feature layer render.");
		if(dispatcher!=null) {
			event.set("renderer", getRenderer());
			dispatcher.render(event, engine, getRenderer());
		}
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return resource.getEnvelope();
	}

	public FeatureCollection getResource() {
		return resource;
	}

	public void setResource(FeatureCollection resource) {
		this.resource = resource;
	}
}