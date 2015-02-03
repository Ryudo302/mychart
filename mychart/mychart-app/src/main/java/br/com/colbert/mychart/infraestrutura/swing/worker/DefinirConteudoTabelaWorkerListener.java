package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import br.com.colbert.base.ui.FormView;

/**
 * {@link WorkerDoneListener} que, após a tarefa ser executada com sucesso, obtém o resultado da execução e define como sendo o
 * conteúdo da tabela da tela.
 *
 * @author Thiago Colbert
 * @since 24/01/2015
 */
public class DefinirConteudoTabelaWorkerListener<T> extends WorkerDoneAdapter {

	private static final long serialVersionUID = 8101853088405958153L;

	private final FormView<T> view;
	private Class<T> elementsClass;

	/**
	 * 
	 * @param view
	 * @throws IllegalArgumentException
	 *             caso o objeto informado não implemente {@link FormView}.
	 */
	@SuppressWarnings("unchecked")
	public DefinirConteudoTabelaWorkerListener(FormView<T> view) {
		this.view = view;

		Arrays.asList(view.getClass().getGenericInterfaces()).stream()
				.filter(type -> FormView.class == ((ParameterizedType) type).getRawType()).findFirst()
				.ifPresent(type -> elementsClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0]);
		if (elementsClass == null) {
			throw new IllegalArgumentException("A classe informada (" + view.getClass() + ") não implementa " + FormView.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doneWithSuccess(SwingWorker<?, ?> worker) {
		Object resultado = ((AbstractWorker<?, ?>) worker).getResult();
		if (resultado instanceof Collection) {
			Collection<?> elementos = (Collection<?>) resultado;
			List<?> elementosComTipoInvalido = elementos.stream()
					.filter(elemento -> !elementsClass.isAssignableFrom(elemento.getClass())).collect(Collectors.toList());
			if (!elementosComTipoInvalido.isEmpty()) {
				throw new IllegalArgumentException(
						"A coleção retornado pelo Worker possui um ou mais elementos cujo tipo é diferente do tipo esperado pela View ("
								+ elementsClass + "): " + elementosComTipoInvalido);
			}
			view.setConteudoTabela((Collection<T>) elementos);
		}
	}
}
