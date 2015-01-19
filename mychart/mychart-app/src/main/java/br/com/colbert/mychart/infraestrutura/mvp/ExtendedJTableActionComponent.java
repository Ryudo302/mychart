package br.com.colbert.mychart.infraestrutura.mvp;

import javax.swing.JTable;
import javax.swing.event.*;

import org.mvp4j.impl.swing.JTableActionComponent;

/**
 * 
 * @author Thiago Colbert
 * @since 19/01/2015
 */
public class ExtendedJTableActionComponent extends JTableActionComponent {

	private ListSelectionListener listSelectionListener;

	@Override
	public void bind() {
		Class<?> eventType = actionBinding.getEventType();
		String eventAction = actionBinding.getEventAction();

		if (eventType != null) {
			if (eventType == ListSelectionListener.class) {
				listSelectionListener = new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent event) {
						if (eventAction.equals("valueChanged") || eventAction.equals("")) {
							actionBinding.callAction(event);
						}
					}
				};

				((JTable) actionBinding.getComponent()).getSelectionModel().addListSelectionListener(listSelectionListener);
			}
		}

		super.bind();
	}
}
