package br.com.colbert.mychart.infraestrutura.mvp;

import java.awt.Window;

import javax.swing.JTable;

import org.mvp4j.adapter.ActionComponent;
import org.mvp4j.impl.swing.SwingAdapter;

/**
 * 
 * @author Thiago Colbert
 * @since 19/01/2015
 */
public class ExtendedSwingAdapter extends SwingAdapter {

	@Override
	public Class<? extends ActionComponent> getComponentAction(Class<?> componentKlass) {
		if (componentKlass.equals(JTable.class)) {
			return ExtendedJTableActionComponent.class;
		} else if (Window.class.isAssignableFrom(componentKlass)) {
			return WindowActionComponent.class;
		} else {
			return super.getComponentAction(componentKlass);
		}
	}
}
