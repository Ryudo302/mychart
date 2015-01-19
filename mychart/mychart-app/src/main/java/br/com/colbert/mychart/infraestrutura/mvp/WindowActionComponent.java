package br.com.colbert.mychart.infraestrutura.mvp;

import java.awt.Window;
import java.awt.event.*;
import java.util.Optional;

import org.mvp4j.adapter.*;

/**
 * Implementação de {@link ActionComponent} para {@link Window}.
 * 
 * @author Thiago Colbert
 * @since 19/01/2015
 */
public class WindowActionComponent extends ActionComponent {

	private static final Class<?> DEFAULT_EVENT_TYPE = WindowListener.class;

	private Window window;
	private String eventAction;

	private Optional<WindowListener> windowListener;
	private Optional<MouseListener> mouseListener;
	private Optional<KeyListener> keyListener;
	private Optional<ComponentListener> componentListener;
	private Optional<ContainerListener> containerListener;
	private Optional<FocusListener> focusListener;
	private Optional<HierarchyBoundsListener> hierarchyBoundsListener;
	private Optional<HierarchyListener> hierarchyListener;

	@Override
	public void bind() {
		Class<?> eventType = actionBinding.getEventType();
		eventAction = actionBinding.getEventAction();

		if (eventType == null) {
			eventType = DEFAULT_EVENT_TYPE;
		}

		if (eventType == WindowListener.class) {
			windowListener = Optional.of(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent event) {
					if (eventAction.equals("windowOpened") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void windowIconified(WindowEvent event) {
					if (eventAction.equals("windowIconified") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void windowDeiconified(WindowEvent event) {
					if (eventAction.equals("windowDeiconified") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void windowDeactivated(WindowEvent event) {
					if (eventAction.equals("windowDeactivated") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void windowClosing(WindowEvent event) {
					if (eventAction.equals("windowClosing") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void windowClosed(WindowEvent event) {
					if (eventAction.equals("windowClosed") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void windowActivated(WindowEvent event) {
					if (eventAction.equals("windowActivated") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addWindowListener(windowListener.get());
		} else if (eventType == MouseListener.class) {
			mouseListener = Optional.of(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent event) {
					if (eventAction.equals("mouseReleased") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void mousePressed(MouseEvent event) {
					if (eventAction.equals("mousePressed") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void mouseExited(MouseEvent event) {
					if (eventAction.equals("mouseExited") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void mouseEntered(MouseEvent event) {
					if (eventAction.equals("mouseEntered") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void mouseClicked(MouseEvent event) {
					if (eventAction.equals("mouseClicked") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});
			window.addMouseListener(mouseListener.get());

		}

		else if (eventType == KeyListener.class) {
			keyListener = Optional.of(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent event) {
					if (eventAction.equals("keyTyped") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void keyReleased(KeyEvent event) {
					if (eventAction.equals("keyReleased") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void keyPressed(KeyEvent event) {
					if (eventAction.equals("keyPressed") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addKeyListener(keyListener.get());
		} else if (eventType == ComponentListener.class) {
			componentListener = Optional.of(new ComponentListener() {

				@Override
				public void componentShown(ComponentEvent event) {
					if (eventAction.equals("componentShown") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void componentResized(ComponentEvent event) {
					if (eventAction.equals("componentResized") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void componentMoved(ComponentEvent event) {
					if (eventAction.equals("componentMoved") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void componentHidden(ComponentEvent event) {
					if (eventAction.equals("componentHidden") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addComponentListener(componentListener.get());
		} else if (eventType == ContainerListener.class) {
			containerListener = Optional.of(new ContainerListener() {

				@Override
				public void componentRemoved(ContainerEvent event) {
					if (eventAction.equals("componentRemoved") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void componentAdded(ContainerEvent event) {
					if (eventAction.equals("componentAdded") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addContainerListener(containerListener.get());
		} else if (eventType == FocusListener.class) {
			focusListener = Optional.of(new FocusListener() {

				@Override
				public void focusLost(FocusEvent event) {
					if (eventAction.equals("focusLost") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void focusGained(FocusEvent event) {
					if (eventAction.equals("focusGained")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addFocusListener(focusListener.get());
		} else if (eventType == HierarchyBoundsListener.class) {
			hierarchyBoundsListener = Optional.of(new HierarchyBoundsListener() {

				@Override
				public void ancestorResized(HierarchyEvent event) {
					if (eventAction.equals("ancestorResized") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}

				@Override
				public void ancestorMoved(HierarchyEvent event) {
					if (eventAction.equals("ancestorMoved") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addHierarchyBoundsListener(hierarchyBoundsListener.get());
		} else if (eventType == HierarchyListener.class) {
			hierarchyListener = Optional.of(new HierarchyListener() {

				@Override
				public void hierarchyChanged(HierarchyEvent event) {
					if (eventAction.equals("hierarchyChanged") || eventAction.equals("")) {
						actionBinding.callAction(event);
					}
				}
			});

			window.addHierarchyListener(hierarchyListener.get());
		}
	}

	@Override
	public void unbind() {
		windowListener.ifPresent(listener -> window.removeWindowListener(listener));
		mouseListener.ifPresent(listener -> window.removeMouseListener(listener));
		keyListener.ifPresent(listener -> window.removeKeyListener(listener));
		// TODO
	}

	@Override
	public void init(ActionBinding actionBinding) {
		super.init(actionBinding);
		window = (Window) actionBinding.getComponent();
	}
}
