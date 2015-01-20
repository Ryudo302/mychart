package br.com.colbert.mychart.infraestrutura.swing.worker;

import java.util.Objects;

import javax.enterprise.inject.Instance;

import br.com.colbert.base.aplicacao.validacao.*;
import br.com.colbert.base.dominio.*;

/**
 * <p>
 * Classe que representa um fluxo de trabalho sobre uma entidade, que pode envolver diversas etapas.
 * </p>
 * <p>
 * Por exemplo, para salvar uma entidade, é preciso validá-la e então salvá-la através do repositório.
 * </p>
 * 
 * @author Thiago Colbert
 * @since 20/01/2015
 */
public final class WorkflowEntidade {

	private WorkflowEntidade() {

	}

	/**
	 * Cria um novo fluxo de trabalho com a entidade e o repositório informados.
	 * 
	 * @param entidade
	 * @param repositorio
	 * @return objeto que fará a validação da entidade
	 */
	public static <T extends Entidade<?>, R extends Repository<T, ?>> NaoValidado<T, R> novo(T entidade, R repositorio) {
		return new NaoValidado<>(entidade, repositorio);
	}

	/**
	 * Primeira etapa do fluxo de trabalho, que fará a validação da entidade.
	 * 
	 * @author Thiago Colbert
	 * @since 20/01/2015
	 *
	 * @param <T>
	 *            tipo de entidade
	 * @param <R>
	 *            tipo de repositório
	 */
	public static class NaoValidado<T extends Entidade<?>, R extends Repository<T, ?>> {

		private T entidade;
		private R repositorio;

		private NaoValidado(T entidade, R repositorio) {
			this.entidade = Objects.requireNonNull(entidade, "Entidade nula");
			this.repositorio = Objects.requireNonNull(repositorio, "Repositório nulo");
		}

		/**
		 * Faz a validação da entidade utilizando os validadores informados.
		 * 
		 * @param validadores
		 * @return o repositório, para chamadas encadeadas
		 * @throws ValidacaoException
		 *             caso ocorra algum erro de validação
		 */
		public R validar(Instance<Validador<T>> validadores) throws ValidacaoException {
			for (Validador<T> validador : validadores) {
				validador.validar(entidade);
			}

			return repositorio;
		}
	}
}
